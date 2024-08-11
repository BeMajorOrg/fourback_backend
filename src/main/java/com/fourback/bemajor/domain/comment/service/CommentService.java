package com.fourback.bemajor.domain.comment.service;

import com.fourback.bemajor.domain.comment.dto.*;
import com.fourback.bemajor.domain.comment.entity.Comment;
import com.fourback.bemajor.domain.comment.entity.CommentType;
import com.fourback.bemajor.domain.comment.entity.FavoriteComment;
import com.fourback.bemajor.domain.community.entity.Post;
import com.fourback.bemajor.domain.user.entity.User;
import com.fourback.bemajor.domain.comment.repository.FavoriteCommentRepository;
import com.fourback.bemajor.domain.community.repository.PostRepository;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fourback.bemajor.domain.comment.repository.CommentRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final FavoriteCommentRepository favoriteCommentRepo;
    private final UserRepository userRepo;

    private String DataDiff(Comment c) {
        String postDate;
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(c.getCreatedDate(), currentTime);

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
        return postDate;
    }

    private boolean isFavorite(Comment c, User user) {
        FavoriteComment fc = favoriteCommentRepo.findByCommentAndUser(c, user);
        if(fc != null) {
            return fc.isFavorite();
        }
        else {
            return false;
        }
    }

    private boolean userCheck(String userName, String authId) {
        if(authId.equals(userName)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void refreshCommentCount(Post p) {
        p.setCommentCount(commentRepo.countByPost(p));
        postRepo.save(p);
    }

    public AddCommentResponse addComment(CommentRequest.Add request, String oauth2Id) {
        Post p = postRepo.getById(request.postId());
        User user = userRepo.findByOauth2Id(oauth2Id).orElse(null);
        Comment pc;

        if(request.parentCommentId() == (long) -1)
            pc = null;
        else
            pc = commentRepo.getById(request.parentCommentId());

        Comment c = Comment.builder().
                content(request.content()).
                post(p).
                parent(pc).
                goodCount(0).
                user(user).
                build();
        commentRepo.save(c);
        refreshCommentCount(p);
        AddCommentResponse res = AddCommentResponse.builder().commentId(c.getId()).build();

        return res;
    }

    public GetCommentResponse getComment(Long CommentID, String oauth2Id) {

        User user = userRepo.findByOauth2Id(oauth2Id).orElse(null);
        Comment c = commentRepo.findById(CommentID).get();
        CommentResult result = CommentResult.fromComment(c);

        result.setPostId(c.getPost().getId());
        result.setParentId(c.getParent().getId());
        result.setDateDiff(DataDiff(c));
        result.setFavorite(isFavorite(c, user));

        GetCommentResponse res = GetCommentResponse.builder()
                .result(result)
                .build();

        return res;
    }

    public GetCommentListResponse getCommentList(Post post, String oauth2Id) {

        List<CommentResult> commentResList = new ArrayList<CommentResult>();
        User user = userRepo.findByOauth2Id(oauth2Id).orElse(null);
        List<Comment> commentList = commentRepo.findCommentListOrderByIDAsc(post.getId());


        for(Comment c : commentList) {
            List<Comment> replyList = commentRepo.findCommentReplies(post.getId(), c.getId());
            List<CommentResult> commentReplyResList = new ArrayList<CommentResult>();

            for(Comment rc : replyList) {
                CommentResult replyresult = CommentResult.fromComment(rc);

                replyresult.setPostId(rc.getPost().getId());
                replyresult.setParentId(rc.getParent().getId());
                replyresult.setDateDiff(DataDiff(c));
                replyresult.setFavorite(isFavorite(rc, user));
                replyresult.setStatus(rc.getStatus().getValue());
                replyresult.setUserCheck(userCheck(rc.getUser().getOauth2Id(), user.getOauth2Id()));
                commentReplyResList.add(replyresult);
            }
            GetCommentListResponse replyRes = GetCommentListResponse.builder()
                    .result(commentReplyResList)
                    .size(commentReplyResList.size())
                    .build();
            long i;
            i = c.getParent()==null ? 0 : c.getParent().getId();

            CommentResult result = CommentResult.fromComment(c);
            result.setPostId(c.getPost().getId());
            result.setParentId(i);
            result.setDateDiff(DataDiff(c));
            result.setReply(replyRes);
            result.setFavorite(isFavorite(c, user));
            result.setStatus(c.getStatus().getValue());
            result.setUserCheck(userCheck(c.getUser().getOauth2Id(), user.getOauth2Id()));
            commentResList.add(result);
        }

        GetCommentListResponse res = GetCommentListResponse.builder()
                .result(commentResList)
                .size(commentResList.size())
                .build();

        return res;
    }

    public PutCommentResponse putComment(CommentRequest.Put request) {
        Comment c = commentRepo.findById(request.commentId()).get();

        c.setContent(request.content());
        commentRepo.save(c);

        PutCommentResponse res = PutCommentResponse.builder()
                .build();
        return res;
    }

    public DeleteCommentResponse deleteComment(Long CommentID) {
        Comment c = commentRepo.findById(CommentID).get();

        c.setStatus(CommentType.DELETED);
        commentRepo.save(c);

        DeleteCommentResponse res = DeleteCommentResponse.builder()
                .build();

        return res;
    }


}
