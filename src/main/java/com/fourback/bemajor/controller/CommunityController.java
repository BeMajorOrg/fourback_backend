package com.fourback.bemajor.controller;

import com.fourback.bemajor.dto.*;
import com.fourback.bemajor.jwt.JWTUtil;
import com.fourback.bemajor.service.BoardService;
import com.fourback.bemajor.service.FavoriteService;
import com.fourback.bemajor.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


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
    public String postCreate(@RequestBody PostDto postDto,Principal principal){
        String oauth2Id = principal.getName();
        postService.create(postDto,oauth2Id);
        return "ok";
    }


    @GetMapping("/api/post")
    public List<PostListDto> posts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
            @RequestParam(value = "boardId", defaultValue = "0") Long boardId
    ) {

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,
                "postDate"));

        return postService.posts(pageRequest,boardId);
    }



    @GetMapping("/api/post/search")
    public List<PostListDto> postSearch(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
            @RequestParam(value = "keyword") String keyword
    ) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC,
                "postDate"));
        return postService.postSearch(pageRequest,keyword);
    }

    @GetMapping("/api/board")
    public List<BoardDto>  boards(Principal principal) {
        String oauth2Id = principal.getName();

        return boardService.boards(oauth2Id);
    }

    @PostMapping("/api/board/favorite")
    public String favorite(@RequestBody FavoriteDto favoriteDto,Principal principal) {
        String oauth2Id = principal.getName();
        favoriteService.add(favoriteDto,oauth2Id);
        return "ok";
    }







}
