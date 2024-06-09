package com.fourback.bemajor.service;


import com.fourback.bemajor.domain.Image;
import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.dto.PostDto;
import com.fourback.bemajor.dto.PostListDto;
import com.fourback.bemajor.domain.Board;

import com.fourback.bemajor.domain.Post;
import com.fourback.bemajor.repository.BoardRepository;

import com.fourback.bemajor.repository.ImageRepository;
import com.fourback.bemajor.repository.PostRepository;
import com.fourback.bemajor.repository.UserRepository;
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
                    return new PostListDto(p, postDate);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    public List<PostListDto> postSearch(PageRequest pageRequest, String keyword) {
        Page<Post> posts = postRepository.findAllSearchPost(keyword,pageRequest);
        List<PostListDto> postListDtos = posts.stream()
                .map(p -> {
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
                    return new PostListDto(p, postDate);
                }).collect(Collectors.toList());
        return postListDtos;
    }



}
