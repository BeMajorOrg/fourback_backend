package com.fourback.bemajor.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import com.fourback.bemajor.domain.Comment;
import java.util.List;
import static com.fourback.bemajor.domain.QComment.comment;

@Repository
    public class CommentCustomRepositoryImpl implements CommentCustomRepository {
        private final JPAQueryFactory jpaQueryFactory;

        public CommentCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
            this.jpaQueryFactory = jpaQueryFactory;
        }

        @Override
        public List<Comment> findCommentListOrderByIDAsc(Long postID) {
            return (List<Comment>) jpaQueryFactory.from(comment)
                    .where(comment.post.id.eq(postID))
                    .where(comment.parent.isNull())
                    .orderBy(comment.id.asc())
                    .fetch();
        }

    @Override
    public List<Comment> findCommentReplies(Long postID, Long CommentId) {
        return (List<Comment>) jpaQueryFactory.from(comment)
                .where(comment.post.id.eq(postID))
                .where(comment.parent.id.eq(CommentId))
                .orderBy(comment.id.asc())
                .fetch();
    }
}
