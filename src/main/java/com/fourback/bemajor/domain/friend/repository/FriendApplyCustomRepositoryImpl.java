package com.fourback.bemajor.domain.friend.repository;

import com.fourback.bemajor.domain.friend.entity.Friend;
import com.fourback.bemajor.domain.friend.entity.FriendApply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.fourback.bemajor.domain.friend.entity.QFriend.friend;
import static com.fourback.bemajor.domain.friend.entity.QFriendApply.friendApply;

@Repository
    public class FriendApplyCustomRepositoryImpl implements FriendApplyCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public FriendApplyCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public FriendApply checkDuplicateFriendApply(Long userId, Long friendId) {
        FriendApply result = (FriendApply) jpaQueryFactory.from(friendApply)
                .where(friendApply.user.userId.eq(userId))
                .where(friendApply.friend.userId.eq(friendId))
                .fetchOne();

        // 결과가 null이면 아무것도 가져오지 않은 것
        if (result == null) {
            System.out.println("No duplicate friend application found.");
        } else {
            System.out.println("Duplicate friend application found.");
        }

        return result;
    }
}
