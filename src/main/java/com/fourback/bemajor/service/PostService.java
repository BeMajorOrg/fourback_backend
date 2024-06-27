package com.fourback.bemajor.service;


import com.fourback.bemajor.domain.*;
import com.fourback.bemajor.dto.PostDto;
import com.fourback.bemajor.dto.PostListDto;

import com.fourback.bemajor.dto.PostUpdateDto;
import com.fourback.bemajor.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;
    private static String UPLOAD_DIR = "uploads/";

    @Transactional
    public Long create(PostDto postDto, String oauth2Id, MultipartFile[] imageFiles) {
        Post post = new Post();
        Optional<Board> optionalBoard = boardRepository.findById((postDto.getBoardId()));
        Board board = optionalBoard.orElse(new Board());
        User user = userRepository.findByOauth2Id(oauth2Id).orElse(null);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setBoard(board);
        post.setUser(user);
        postRepository.save(post);
        if (imageFiles != null) {
            for (MultipartFile imageFile : imageFiles) {
                String originalFilename = imageFile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                String uniqueFilename = UUID.randomUUID().toString() + extension;
                Path filePath = Paths.get(UPLOAD_DIR, uniqueFilename);

                try {
                    if (!Files.exists(filePath.getParent())) {
                        Files.createDirectories(filePath.getParent());
                    }
                    Files.write(filePath, imageFile.getBytes());

                    Image image = new Image();
                    image.setPost(post);
                    image.setFilePath(filePath.toString());
                    image.setFileName(uniqueFilename);
                    imageRepository.save(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }

        return post.getId();
    }

    public List<PostListDto> posts(PageRequest pageRequest, Long boardId) {
        Page<Post> pagePost = postRepository.findAllWithPost(boardId,pageRequest);
        List<PostListDto> postListDtos = pagePost.stream()
                .map(p -> {
                    List<Image> imageList = imageRepository.findByPostId(p.getId());

                    String postDate;
                    LocalDateTime currentTime = LocalDateTime.now();
                    Duration duration = Duration.between(p.getCreatedDate(), currentTime);
                    long minutes = duration.toMinutes();
                    if (minutes < 1) {
                        postDate = "방금 전";
                    } else if (minutes < 60) {
                        postDate = minutes + "분 전";
                    } else {
                        long hours = duration.toHours();
                        if (hours < 24) {
                            postDate = hours + "시간 전";
                        } else {
                            long days = duration.toDays();
                            postDate = days + "일 전";
                        }
                    }
                    return new PostListDto(p, postDate,imageList);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    public List<PostListDto> postSearch(PageRequest pageRequest, String keyword) {
        Page<Post> posts = postRepository.findAllSearchPost(keyword,pageRequest);
        List<PostListDto> postListDtos = posts.stream()
                .map(p -> {
                    List<Image> imageList = imageRepository.findByPostId(p.getId());

                    String postDate;
                    LocalDateTime currentTime = LocalDateTime.now();
                    Duration duration = Duration.between(p.getCreatedDate(), currentTime);
                    long minutes = duration.toMinutes();
                    if (minutes < 1) {
                        postDate = "방금 전";
                    } else if (minutes < 60) {
                        postDate = minutes + "분 전";
                    } else {
                        long hours = duration.toHours();
                        if (hours < 24) {
                            postDate = hours + "시간 전";
                        } else {
                            long days = duration.toDays();
                            postDate = days + "일 전";
                        }
                    }
                    return new PostListDto(p, postDate,imageList);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    @Transactional
    public ResponseEntity<String> update(Long postId,String title, String content, MultipartFile[] images) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();
        post.setTitle(title);
        post.setContent(content);
        if (images != null) {
            for (MultipartFile imageFile : images) {
                String originalFilename = imageFile.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
                String uniqueFilename = UUID.randomUUID().toString() + extension;
                Path filePath = Paths.get(UPLOAD_DIR, uniqueFilename);

                try {
                    if (!Files.exists(filePath.getParent())) {
                        Files.createDirectories(filePath.getParent());
                    }
                    Files.write(filePath, imageFile.getBytes());

                    Image image = new Image();
                    image.setPost(post);
                    image.setFilePath(filePath.toString());
                    image.setFileName(uniqueFilename);
                    imageRepository.save(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


        }
        return ResponseEntity.ok("post update");
    }


    public PostUpdateDto updatePostGet(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();
        String postDate;
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(post.getUpdatedDate(), currentTime);
        long minutes = duration.toMinutes();
        if (minutes < 1) {
            postDate = "방금 전";
        } else if (minutes < 60) {
            postDate = minutes + "분 전";
        } else {
            long hours = duration.toHours();
            if (hours < 24) {
                postDate = hours + "시간 전";
            } else {
                long days = duration.toDays();
                postDate = days + "일 전";
            }
        }
        List<Image> imageList = imageRepository.findByPostId(post.getId());
        PostUpdateDto postUpdateDto = new PostUpdateDto(post,postDate,imageList);
        return postUpdateDto;
    }

    @Transactional
    public void delete(Long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
        List<Image> images = imageRepository.findByPostId(postId);
        for (Image image : images) {
            Path filePath = Paths.get(image.getFilePath());
            try {

                // 파일 시스템에서 파일 삭제
                Files.deleteIfExists(filePath);

                // 데이터베이스에서 이미지 기록 삭제
                imageRepository.delete(image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        postRepository.delete(post);


    }
}
