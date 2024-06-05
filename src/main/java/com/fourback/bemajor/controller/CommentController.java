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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CommentController {

    private final CommentService commentService;
    private final PostRepository postRepo;

    @PostMapping("/api/comment")
    public ResponseEntity<AddCommentResponse> addComment(@RequestBody CommentRequest.Add request) {
        AddCommentResponse res = this.commentService.addComment(request);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/comment/{postID}/list")
    public ResponseEntity<GetCommentListResponse> getPostCommentList(@PathVariable("postID") long postID) {
        Post post = (Post)this.postRepo.findById(postID).get();
        GetCommentListResponse res = null;
        res = this.commentService.getCommentList(post);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/api/comment/{commentID}")
    public ResponseEntity<GetCommentResponse> getComment(@PathVariable("commentID") long commentID) {
        GetCommentResponse res = null;
        res = this.commentService.getComment(commentID);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/api/comment/{commentID}")
    public ResponseEntity<PutCommentResponse> putComment(@PathVariable("commentID") long commentID, @RequestBody CommentRequest.Put request) {
        PutCommentResponse res = null;
        res = this.commentService.putComment(commentID, request);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/api/comment/{commentID}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(@PathVariable("commentID") long commentID) {
        DeleteCommentResponse res = null;
        res = this.commentService.deleteComment(commentID);
        return ResponseEntity.ok().body(res);
    }

}
