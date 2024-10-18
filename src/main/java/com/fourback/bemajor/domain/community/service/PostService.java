package com.fourback.bemajor.domain.community.service;


import com.fourback.bemajor.domain.aws.service.S3UploadService;
import com.fourback.bemajor.domain.comment.entity.Comment;
import com.fourback.bemajor.domain.comment.entity.FavoriteComment;
import com.fourback.bemajor.domain.comment.repository.CommentRepository;
import com.fourback.bemajor.domain.comment.repository.FavoriteCommentRepository;
import com.fourback.bemajor.domain.community.entity.Board;
import com.fourback.bemajor.domain.community.entity.FavoritePost;
import com.fourback.bemajor.domain.community.entity.Post;
import com.fourback.bemajor.domain.community.repository.BoardRepository;
import com.fourback.bemajor.domain.community.repository.FavoritePostRepository;
import com.fourback.bemajor.domain.community.repository.PostRepository;
import com.fourback.bemajor.domain.community.entity.ImageEntity;
import com.fourback.bemajor.domain.community.repository.ImageRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.domain.community.dto.PostDto;
import com.fourback.bemajor.domain.community.dto.PostListDto;

import com.fourback.bemajor.domain.community.dto.PostUpdateDto;

import com.fourback.bemajor.global.exception.kind.NotAuthorizedException;
import com.fourback.bemajor.global.exception.kind.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final FavoritePostRepository favoritePostRepository;
    private final FavoriteCommentRepository favoriteCommentRepository;
    private final CommentRepository commentRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public Long create(PostDto postDto, Long userId, MultipartFile[] images) throws IOException {
        Post post = new Post();
        Optional<Board> optionalBoard = boardRepository.findById((postDto.getBoardId()));
        Board board = optionalBoard.orElse(new Board());
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setBoard(board);
        post.setUser(userEntity);
        postRepository.save(post);
        this.saveImage(post, images);
        return post.getId();
    }

    public List<PostListDto> posts(PageRequest pageRequest, Long boardId, Long userId) {

        Page<Post> pagePost = postRepository.findAllWithPost(boardId, pageRequest);
        Optional<UserEntity> byOauth2Id = userRepository.findById(userId);
        UserEntity userEntity = byOauth2Id.get();
        List<PostListDto> postListDtos = pagePost.stream()
                .map(p -> {
                    boolean userCheck = false;
                    if (p.getUser().getUserId().equals(userEntity.getUserId())) {
                        userCheck = true;
                    }
                    List<ImageEntity> imageList = imageRepository.findByPostId(p.getId());
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
                    Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(userEntity, p);
                    boolean postGood = false;
                    if (optionalFavoritePost.isPresent()) {
                        postGood = true;
                    }
                    return new PostListDto(p, postDate, imageList, postGood, userCheck);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    public List<PostListDto> posts2(PageRequest pageRequest, Long boardId, Long userId) {
        Optional<UserEntity> byOauth2Id = userRepository.findById(userId);
        UserEntity userEntity = byOauth2Id.get();
        Page<Post> pagePost = null;

        if (boardId == 1) {
            pagePost = postRepository.findAllMyPost(userEntity.getUserId(), pageRequest);
        } else if (boardId == 2) {
            pagePost = commentRepository.findCommentPosts(userEntity.getUserId(), pageRequest);
        } else if (boardId == 3) {
            pagePost = favoritePostRepository.findFavoritePosts(userEntity.getUserId(), pageRequest);
        } else if (boardId == 4) {
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.minusWeeks(1);
            pagePost = postRepository.findPopularPosts(startDate, endDate, pageRequest);
        }


        List<PostListDto> postListDtos = pagePost.stream()
                .map(p -> {
                    boolean userCheck = false;
                    if (p.getUser().getUserId().equals(userEntity.getUserId())) {
                        userCheck = true;
                    }
                    List<ImageEntity> imageList = imageRepository.findByPostId(p.getId());

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
                    Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(userEntity, p);
                    boolean postGood = false;
                    if (optionalFavoritePost.isPresent()) {
                        postGood = true;
                    }
                    return new PostListDto(p, postDate, imageList, postGood, userCheck);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    public List<PostListDto> postSearch(PageRequest pageRequest, String keyword, Long userId) {
        Page<Post> posts = postRepository.findAllSearchPost(keyword, pageRequest);
        Optional<UserEntity> byOauth2Id = userRepository.findById(userId);
        UserEntity userEntity = byOauth2Id.get();
        List<PostListDto> postListDtos = posts.stream()
                .map(p -> {
                    boolean userCheck = false;
                    if (p.getUser().getUserId().equals(userEntity.getUserId())) {
                        userCheck = true;
                    }
                    List<ImageEntity> imageList = imageRepository.findByPostId(p.getId());

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
                    Optional<FavoritePost> optionalFavoritePost = favoritePostRepository.findByUserAndPost(userEntity, p);
                    boolean postGood = false;
                    if (optionalFavoritePost.isPresent()) {
                        postGood = true;
                    }
                    return new PostListDto(p, postDate, imageList, postGood, userCheck);
                }).collect(Collectors.toList());
        return postListDtos;
    }

    @Transactional
    public ResponseEntity<String> update(Long postId, String title, String content, MultipartFile[] images) throws IOException {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();
        post.setTitle(title);
        post.setContent(content);
        this.saveImage(post, images);
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
        List<ImageEntity> imageList = imageRepository.findByPostId(post.getId());
        PostUpdateDto postUpdateDto = new PostUpdateDto(post, postDate, imageList);
        return postUpdateDto;
    }

    @Transactional
    public void delete(Long postId) throws IOException {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = optionalPost.get();

        favoritePostRepository.deleteByPostId(postId);
        List<Comment> comments = commentRepository.findByPostId(postId);
        for (Comment comment : comments) {
            Optional<FavoriteComment> byCommentId = favoriteCommentRepository.findByCommentId(comment.getId());
            if (byCommentId.isPresent()) {
                FavoriteComment favoriteComment = byCommentId.get();
                favoriteCommentRepository.delete(favoriteComment);
            }

            List<Comment> byParentId = commentRepository.findByParentId(comment.getId());
            for (Comment comment1 : byParentId) {
                Optional<FavoriteComment> byCommentId1 = favoriteCommentRepository.findByCommentId(comment1.getId());
                if (byCommentId1.isPresent()) {
                    FavoriteComment favoriteComment1 = byCommentId1.get();
                    favoriteCommentRepository.delete(favoriteComment1);
                }
                commentRepository.delete(comment1);
            }

            commentRepository.delete(comment);
        }

        List<ImageEntity> images = imageRepository.findByPostId(postId);
        s3UploadService.deleteFiles(images.stream().map(ImageEntity::getImageUrl).toList());
        imageRepository.deleteAllInBatch(images);
        postRepository.delete(post);
    }

    @Transactional
    public ResponseEntity<String> viewCountUp(Long postId) {
        Post post = postRepository.findById(postId).get();
        post.setViewCount(post.getViewCount() + 1);
        return ResponseEntity.ok("ok");

    }
    @Transactional
    public void deleteImage(Long userId, List<String> imageUrls, Long postId) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new NotFoundException("no such study group. can't delete"));
        if(post.getUser().getUserId().equals(userId))
            throw new NotAuthorizedException("not authorized. can't delete in post image");
        List<ImageEntity> images = imageRepository.findAllByImageUrlsWithPost(imageUrls);
        if(!images.stream().allMatch(image -> image.getPost().getId().equals(postId))){
            throw new NotAuthorizedException("not authorized. can't delete in post image");
        }
        s3UploadService.deleteFiles(imageUrls);
        imageRepository.deleteAllInBatch(images);
    }

    private void saveImage(Post post, MultipartFile[] images) throws IOException {
        if (images != null) {
            for (MultipartFile imageFile : images) {
                String imageUrl = s3UploadService.saveFile(imageFile);
                ImageEntity image = ImageEntity.of(post, imageUrl);
                imageRepository.save(image);
            }
        }
    }
}
