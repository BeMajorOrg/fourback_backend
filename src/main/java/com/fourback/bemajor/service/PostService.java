package com.fourback.bemajor.service;

import com.fourback.bemajor.dto.PostDto;
import com.fourback.bemajor.dto.PostListDto;
import com.fourback.bemajor.domain.Board;
import com.fourback.bemajor.domain.Member;
import com.fourback.bemajor.domain.Post;
import com.fourback.bemajor.repository.BoardRepository;
import com.fourback.bemajor.repository.MemberRepository;
import com.fourback.bemajor.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long create(PostDto postDto) {

        Optional<Board> optionalBoard = boardRepository.findById(postDto.getBoardId());
        Board board = optionalBoard.orElse(new Board()); // 게시판 있으면 가져오고 아니면 새로 생성
        Member member = memberRepository.findById(postDto.getMemberId()).orElse(null);

        Post post = Post.builder().
                title(postDto.getTitle()).
                content(postDto.getContent()).
                postDate(LocalDateTime.now()).
                board(board).
                member(member).
                build();

        postRepository.save(post);
        return post.getId();
    }

    public List<PostListDto> posts(PageRequest pageRequest, Long boardId) {
        Page<Post> pagePost = postRepository.findAllWithPost(boardId,pageRequest);
        List<PostListDto> postListDtos = pagePost.stream()
                .map(p -> {
                    String postDate;
                    LocalDateTime currentTime = LocalDateTime.now();
                    Duration duration = Duration.between(p.getPostDate(), currentTime);
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
                    Duration duration = Duration.between(p.getPostDate(), currentTime);
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
