package com.fourback.bemajor.controller;

import com.fourback.bemajor.domain.Post;
import com.fourback.bemajor.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fourback.bemajor.repository.PostRepository;
import com.fourback.bemajor.service.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("comment")
    public class CommentController {

    private final CommentService commentService;
    private final PostRepository postRepo;

    @PostMapping("/")
    public ResponseEntity<AddCommentResponse> addComment(
            @RequestBody CommentRequest.Add request
    ) {

        AddCommentResponse res;

        res = commentService.addComment(request);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{postID}/list")
    public ResponseEntity<GetCommentListResponse> getPostCommentList(
            @PathVariable(value = "postID") long postID) {

        Post post = postRepo.findById(postID).get();

        GetCommentListResponse res = null;

            res = commentService.getCommentList(post);

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/{commentID}")
    public ResponseEntity<GetCommentResponse> getComment (
            @PathVariable(value = "commentID") long commentID
    ) {

        GetCommentResponse res = null;
        res = commentService.getComment(commentID);

        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/{commentID}")
    public ResponseEntity<PutCommentResponse> putComment(
            @PathVariable(value = "commentID") long commentID,
            @RequestBody CommentRequest.Put request
            ) {

        PutCommentResponse res = null;
        res = commentService.putComment(commentID, request);

        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/{commentID}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @PathVariable(value = "commentID") long commentID
    ) {

        DeleteCommentResponse res = null;
        res = commentService.deleteComment(commentID);

        return ResponseEntity.ok().body(res);
    }

}
