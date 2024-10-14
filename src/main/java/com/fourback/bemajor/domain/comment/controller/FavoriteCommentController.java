//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.fourback.bemajor.domain.comment.controller;

import com.fourback.bemajor.domain.comment.dto.AddFavoriteCommentResponse;
import com.fourback.bemajor.domain.comment.dto.DeleteFavoriteCommentResponse;
import com.fourback.bemajor.global.security.custom.CustomUserDetails;
import com.fourback.bemajor.global.common.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json;charset=UTF-8")
public class FavoriteCommentController {

    private final FavoriteService favoriteService;

    @ResponseBody
    @PostMapping("/api/comment/favorite")
    public ResponseEntity<AddFavoriteCommentResponse> addFavoriteComment(
            @RequestParam(value = "commentID") long commentID,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long userId = customUserDetails.getUserId();
        AddFavoriteCommentResponse res = favoriteService.addFavoriteComment(commentID, userId);
        return ResponseEntity.ok().body(res);
    }


    @ResponseBody
    @DeleteMapping("/api/comment/favorite")
    public ResponseEntity<DeleteFavoriteCommentResponse> deleteFavoriteComment(
            @RequestParam(value = "commentID") long commentID,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long userId = customUserDetails.getUserId();
        DeleteFavoriteCommentResponse res = favoriteService.deleteFavoriteComment(commentID, userId);
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/comment/favorite")
    public ResponseEntity<Boolean> getFavoriteComment(
            @RequestParam(value = "commentID") long commentID,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long userId = customUserDetails.getUserId();
        return ResponseEntity.ok().body(favoriteService.getFavoriteComment(commentID, userId));
    }
}


