//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.fourback.bemajor.domain.comment.controller;

import com.fourback.bemajor.domain.community.entity.Post;
import com.fourback.bemajor.domain.comment.dto.AddCommentResponse;
import com.fourback.bemajor.domain.comment.dto.CommentRequest;
import com.fourback.bemajor.domain.comment.dto.DeleteCommentResponse;
import com.fourback.bemajor.domain.comment.dto.GetCommentListResponse;
import com.fourback.bemajor.domain.comment.dto.GetCommentResponse;
import com.fourback.bemajor.domain.comment.dto.PutCommentResponse;
import com.fourback.bemajor.domain.community.repository.PostRepository;
import com.fourback.bemajor.domain.comment.service.CommentService;
import com.fourback.bemajor.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CommentController {

    private final CommentService commentService;
    private final PostRepository postRepo;

    @ResponseBody
    @PostMapping("/api/comment")
    public ResponseEntity<AddCommentResponse> addComment(
            @RequestBody CommentRequest.Add request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long userId = customUserDetails.getUserId();
        AddCommentResponse res = commentService.addComment(request,userId);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/comment/list")
    public ResponseEntity<GetCommentListResponse> getCommentList(
            @RequestParam(value = "postID", defaultValue = "0") long postID,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Post post = postRepo.getById(postID);

        Long userId = customUserDetails.getUserId();
        GetCommentListResponse res = commentService.getCommentList(post, userId);;

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/comment")
    public ResponseEntity<GetCommentResponse> getComment(
            @RequestParam(value = "commentID") long commentID,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        GetCommentResponse res = null;

        Long userId = customUserDetails.getUserId();
        res = this.commentService.getComment(commentID, userId);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/api/comment")
    public ResponseEntity<PutCommentResponse> putComment(
            @RequestBody CommentRequest.Put request) {

        PutCommentResponse res = null;
        res = this.commentService.putComment(request);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/api/comment")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @RequestParam(value = "commentID") long commentID) {
        DeleteCommentResponse res = null;
        res = this.commentService.deleteComment(commentID);
        return ResponseEntity.ok().body(res);
    }

}
