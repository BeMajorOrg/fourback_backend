//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.fourback.bemajor.controller;

import com.fourback.bemajor.domain.Post;
import com.fourback.bemajor.dto.AddCommentResponse;
import com.fourback.bemajor.dto.CommentRequest;
import com.fourback.bemajor.dto.DeleteCommentResponse;
import com.fourback.bemajor.dto.GetCommentListResponse;
import com.fourback.bemajor.dto.GetCommentResponse;
import com.fourback.bemajor.dto.PutCommentResponse;
import com.fourback.bemajor.repository.PostRepository;
import com.fourback.bemajor.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
            Principal principal) {

        String oauth2Id = principal.getName();
        AddCommentResponse res = commentService.addComment(request,oauth2Id);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/comment/list")
    public ResponseEntity<GetCommentListResponse> getCommentList(
            @RequestParam(value = "postID", defaultValue = "0") long postID,
            Principal principal) {

        Post post = postRepo.getById(postID);

        String oauth2Id = principal.getName();
        GetCommentListResponse res = commentService.getCommentList(post, oauth2Id);;

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/comment")
    public ResponseEntity<GetCommentResponse> getComment(
            @RequestParam(value = "commentID") long commentID,
            Principal principal) {

        GetCommentResponse res = null;

        String oauth2Id = principal.getName();
        res = this.commentService.getComment(commentID, oauth2Id);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/api/comment")
    public ResponseEntity<PutCommentResponse> putComment(
            @RequestParam(value = "commentID") long commentID, @RequestBody CommentRequest.Put request) {
        PutCommentResponse res = null;
        res = this.commentService.putComment(commentID, request);
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
