package com.fourback.bemajor.domain.community.controller;

import com.fourback.bemajor.domain.community.dto.*;
import com.fourback.bemajor.domain.community.service.BoardService;
import com.fourback.bemajor.global.common.service.FavoriteService;
import com.fourback.bemajor.domain.community.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CommunityController {
    private final PostService postService;
    private final BoardService boardService;
    private final FavoriteService favoriteService;


    @ResponseBody
    @PostMapping("/api/post")
    public String postCreate(@RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("boardId") Long boardId,
                             Principal principal,
                             @RequestParam(value = "images", required = false) MultipartFile[] images) throws IOException {
        String oauth2Id = principal.getName();
        PostDto postDto = new PostDto();
        postDto.setTitle(title);
        postDto.setContent(content);
        postDto.setBoardId(boardId);
        postService.create(postDto,oauth2Id,images);
        return "ok";
    }


    @GetMapping("/api/post")
    public List<PostListDto> posts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "boardId", defaultValue = "1") Long boardId,
            Principal principal
    ) {

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,
                "createdDate"));
        String oauth2Id = principal.getName();

        return postService.posts(pageRequest,boardId,oauth2Id);
    }

    @GetMapping("/api/post2")
    public List<PostListDto> posts2(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(value = "boardId", defaultValue = "1") Long boardId,
            Principal principal
    ) {
        Sort sort = null;



        if(boardId == 1) {
            sort = Sort.by(Sort.Direction.DESC, "createdDate");
        } else if(boardId == 2 || boardId == 3) {
            sort = Sort.by(Sort.Direction.DESC, "post.createdDate");
        } else if(boardId == 4) {
            sort = Sort.by(Sort.Direction.DESC, "goodCount");
        }

        PageRequest pageRequest = PageRequest.of(page, pageSize,
                sort);
        String oauth2Id = principal.getName();

        return postService.posts2(pageRequest,boardId,oauth2Id);
    }

    @GetMapping("/api/post/{id}")
    public PostUpdateDto post(@PathVariable("id") Long postId) {

        return postService.updatePostGet(postId);
    }

    @PatchMapping("/api/post/{id}")
    public ResponseEntity<String> updatePost(@PathVariable("id") Long postId,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content,
                                             @RequestParam(value = "images", required = false) MultipartFile[] images) throws IOException {
        return postService.update(postId,title,content,images);

    }

    @PatchMapping("/api/post/{id}/view")
    public ResponseEntity<String> viewCountUp(@PathVariable("id") Long postId)  {
        return postService.viewCountUp(postId);

    }

    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long postId) {
        try {
            postService.delete(postId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete post");
        }

    }

    @GetMapping("/api/post/search")
    public List<PostListDto> postSearch(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
            @RequestParam(value = "keyword") String keyword,
            Principal principal
    ) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,
                "createdDate"));
        String oauth2Id = principal.getName();
        return postService.postSearch(pageRequest,keyword,oauth2Id);
    }

    @GetMapping("/api/board")
    public List<BoardDto>  boards(Principal principal) {
        String oauth2Id = principal.getName();

        return boardService.boards(oauth2Id);
    }

    @PostMapping("/api/board/favorite")
    public String favoriteBoard(@RequestBody FavoriteDto favoriteDto, Principal principal) {
        String oauth2Id = principal.getName();
        favoriteService.add(favoriteDto,oauth2Id);
        return "ok";
    }

    @PostMapping("/api/post/{id}/favorite")
    public String favoritePost(@PathVariable("id") Long postId,Principal principal) {
        String oauth2Id = principal.getName();
        favoriteService.post(postId,oauth2Id);
        return "ok";
    }
}
