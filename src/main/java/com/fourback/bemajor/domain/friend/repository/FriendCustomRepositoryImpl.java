package com.fourback.bemajor.domain.friend.repository;

import com.fourback.bemajor.domain.comment.entity.Comment;
import com.fourback.bemajor.domain.friend.entity.Friend;
import com.fourback.bemajor.domain.friend.entity.FriendApply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;


import java.util.List;

import static com.fourback.bemajor.domain.friend.entity.QFriend.friend;
import static com.fourback.bemajor.domain.friend.entity.QFriendApply.friendApply;

@Repository
    public class FriendCustomRepositoryImpl implements FriendCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public FriendCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Friend findFriendByUserIdAndFriendId(Long userId, Long friendId) {
        return (Friend) jpaQueryFactory.from(friend)
                .where(
                        (friend.id.accountId.eq(userId).and(friend.id.friendAccountId.eq(friendId)))
                                .or(friend.id.accountId.eq(friendId).and(friend.id.friendAccountId.eq(userId)))
                )
                .fetchOne();
    }

    @Override
    public List<Friend> findFriendListOrderByUserIdDesc(Long userId) {
        return (List<Friend>) jpaQueryFactory.from(friend)
                .where(
                        friend.id.accountId.eq(userId)
                                .or(friend.id.friendAccountId.eq(userId)) // accountId나 friendAccountId 중 하나라도 일치하면 가져오기
                )
                .orderBy(friend.id.friendAccountId.desc())
                .fetch();
    }


    @Override
    public Friend checkDuplicateFriend(Long userId, Long friendId) {
        Friend result = (Friend) jpaQueryFactory.from(friend)
                .where(
                        // 첫 번째 경우: accountId == friendId, friendAccountId == userId
                        friend.id.accountId.eq(friendId).and(friend.id.friendAccountId.eq(userId))
                                // 두 번째 경우: accountId == userId, friendAccountId == friendId
                                .or(friend.id.accountId.eq(userId).and(friend.id.friendAccountId.eq(friendId)))
                )
                .fetchOne();

        if (result == null) {
            System.out.println("No duplicate friend application found.");
        } else {
            System.out.println("Duplicate friend application found.");
        }

        return result;
    }
}
