package com.fourback.bemajor.domain.comment.repository;

import com.fourback.bemajor.domain.comment.entity.Comment;
import com.fourback.bemajor.domain.comment.entity.FavoriteComment;
import com.fourback.bemajor.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fourback.bemajor.domain.QFavoriteComment.favoriteComment;

@Repository
    public class FavoriteCommentCustomRepositoryImpl implements FavoriteCommentCustomRepository {
        private final JPAQueryFactory jpaQueryFactory;

        public FavoriteCommentCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
            this.jpaQueryFactory = jpaQueryFactory;
        }

        @Override
        public FavoriteComment findByCommentAndUser(Comment comment, User user) {
            return (FavoriteComment) jpaQueryFactory.from(favoriteComment)
                    .where(favoriteComment.comment.eq(comment))
                    .where(favoriteComment.user.eq(user))
                    .fetchOne();
        }

    @Override
    public List<FavoriteComment> findFavoriteCommentListByComment(Comment comment) {
        return (List<FavoriteComment>) jpaQueryFactory.from(favoriteComment)
                .where(favoriteComment.comment.eq(comment))
                .where(favoriteComment.isFavorite.isTrue())
                .fetch();
    }

    @Override
    public List<FavoriteComment> findFavoriteCommentListByUser(User user) {
        return (List<FavoriteComment>) jpaQueryFactory.from(favoriteComment)
                .where(favoriteComment.user.eq(user))
                .fetch();
    }


}
