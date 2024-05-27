package com.fourback.bemajor.service;

import com.fourback.bemajor.dto.*;
import com.fourback.bemajor.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fourback.bemajor.domain.Comment;
import com.fourback.bemajor.domain.Post;
import com.fourback.bemajor.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;

    public AddCommentResponse addComment(CommentRequest.Add request) {
        Post p = postRepo.findById(request.postId()).get();
        Comment pc = commentRepo.findById(request.parentCommentId()).get();

        Comment c = Comment.builder().
                content(request.content()).
                post(p).
                parent(pc).
                goodCount(0).
                build();
        commentRepo.save(c);

        AddCommentResponse res = AddCommentResponse.builder().commentId(c.getId()).build();

        return res;
    }

    public GetCommentResponse getComment(Long CommentID) {

        Comment c = commentRepo.findById(CommentID).get();
        CommentResult result = CommentResult.fromComment(c);

        GetCommentResponse res = GetCommentResponse.builder()
                .result(result)
                .build();

        return res;
    }

    public GetCommentListResponse getCommentList(Post post) {

        ArrayList<CommentResult> commentResList = new ArrayList<CommentResult>();
        List<Comment> commentList = commentRepo.findCommentListOrderByIDAsc(post.getId());

        for(Comment c : commentList) {
            List<Comment> replyList = commentRepo.findCommentReplies(post.getId(), c.getId());
            ArrayList<CommentResult> commentReplyResList = new ArrayList<CommentResult>();

            for(Comment rc : replyList) {
                CommentResult replyresult = CommentResult.fromComment(rc);
                    commentReplyResList.add(replyresult);
            }
            GetCommentListResponse replyRes = GetCommentListResponse.builder()
                    .result(commentReplyResList)
                    .build();

            CommentResult result = CommentResult.fromComment(c);
            result.setReply(replyRes);
            commentResList.add(result);
        }


        GetCommentListResponse res = GetCommentListResponse.builder()
                .result(commentResList)
                .build();

        return res;
    }

    public PutCommentResponse putComment(Long CommentID, CommentRequest.Put request) {
        Comment c = commentRepo.findById(CommentID).get();

        c.setContent(request.content());
        commentRepo.save(c);

        PutCommentResponse res = PutCommentResponse.builder()
                .build();

        return res;
    }

    public DeleteCommentResponse deleteComment(Long CommentID) {
        Comment c = commentRepo.findById(CommentID).get();

        c.setContent(" "); // 일단 DB에서 완전히 삭제하지 않고 content 공백으로 변경
        commentRepo.save(c);

        DeleteCommentResponse res = DeleteCommentResponse.builder()
                .build();

        return res;
    }

}
