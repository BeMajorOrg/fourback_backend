package com.fourback.bemajor.domain.friend.service;

import com.fourback.bemajor.domain.friend.dto.*;
import com.fourback.bemajor.domain.friend.entity.Friend;
import com.fourback.bemajor.domain.friend.entity.FriendApply;
import com.fourback.bemajor.domain.friend.entity.FriendId;
import com.fourback.bemajor.domain.friend.repository.FriendApplyRepository;
import com.fourback.bemajor.domain.friend.repository.FriendRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FriendService {

    private final FriendRepository friendRepo;
    private final FriendApplyRepository friendApplyRepo;
    private final UserRepository userRepo;

    @Transactional
    public AddFriendApplyResponse addFriendApply(long userId, FriendRequest.Apply request) {
        UserEntity user = userRepo.findById(userId).orElse(null);
        UserEntity friend = userRepo.findById(request.friendId()).orElse(null);

        if(friendApplyRepo.checkDuplicateFriendApply(user.getId(), friend.getId()) != null) {
            throw new IllegalArgumentException("");
        };

        if(friendRepo.checkDuplicateFriend(user.getId(), friend.getId()) != null) {
            throw new IllegalArgumentException("");
        };

        FriendApply friendApply = FriendApply.builder().user(user).friend(friend).build();
        friendApplyRepo.save(friendApply);

        AddFriendApplyResponse res = AddFriendApplyResponse.builder().applyNumber(friendApply.getId()).build();
        return res;
    }

    @Transactional
    public AcceptFriendApplyResponse acceptFriendApply(Long applyId) {
        FriendApply friendApply = friendApplyRepo.findById(applyId).orElseThrow(RuntimeException::new);

        if(friendRepo.checkDuplicateFriend(friendApply.getUser().getId(), friendApply.getFriend().getId()) != null) {
            throw new IllegalArgumentException("");
        };

        FriendId friendId = FriendId.builder()
                .accountId(friendApply.getUser().getId())
                .friendAccountId(friendApply.getFriend().getId())
                .build();

        Friend friend = Friend.builder().id(friendId).build();
        friendRepo.save(friend);
        friendApplyRepo.delete(friendApply);

        AcceptFriendApplyResponse res = AcceptFriendApplyResponse.builder().isSuccess(true).build();
        return res;
    }

    @Transactional(readOnly = true)
    public CountFriendApplyResponse countApplies(Long userId) {
                return CountFriendApplyResponse.builder()
                        .count(friendApplyRepo.countAllByFriend_Id(userId))
                        .build();
    }

    public GetFriendAppliesResponse getReceiveApplies(Long userId) {
        List<FriendApply> Applies = friendApplyRepo.findAllByFriend_Id(userId);
        List<GetFriendApplyResponse> FriendAppliesResult = new ArrayList<>();

        for(FriendApply c : Applies) {
            FriendAppliesResult.add(GetFriendApplyResponse.from(c));
        }
        GetFriendAppliesResponse res = GetFriendAppliesResponse.builder()
                .result(FriendAppliesResult)
                .build();
        return res;
    }

    public GetFriendListResponse getFriendList(Long userId) {

        List<FriendResponse> friendResList = new ArrayList<>();
        List<Friend> friendList = friendRepo.findFriendListOrderByUserIdDesc(userId);

        for(Friend f : friendList) {
            if(userId.equals(f.getId().getAccountId())) {
                UserEntity friend = userRepo.getById(f.getId().getFriendAccountId());
                FriendResponse result = FriendResponse.fromUser(friend);
                result.setUserId(friend.getId());
                friendResList.add(result);

            } else if (userId.equals(f.getId().getFriendAccountId())) {
                UserEntity user = userRepo.getById(f.getId().getAccountId());
                FriendResponse result = FriendResponse.fromUser(user);
                result.setUserId(user.getId());
                friendResList.add(result);
            }

        }

        GetFriendListResponse res = GetFriendListResponse.builder()
                .result(friendResList)
                .size(friendResList.size())
                .build();

        return res;
    }

    public GetUserListForInvitationResponse getFriendInvitationList(Long userId) {
        List<UserEntity> users = userRepo.findAll();
        List<UserForInvitationResponse> userList = new ArrayList<>();

        for(UserEntity user : users) {
            if(userId.equals(user.getId()) == false) {
                UserForInvitationResponse result = UserForInvitationResponse.fromUser(user);
                userList.add(result);
            }
        }

        GetUserListForInvitationResponse res = GetUserListForInvitationResponse.
                builder().
                result(userList).
                size(userList.size()).
                build();

        return res;
    }

    public DeleteFriendResponse deleteFriend(long userId, FriendRequest.Delete request) {
        Friend f = friendRepo.findFriendByUserIdAndFriendId(userId, request.friendId());

        friendRepo.delete(f);

        DeleteFriendResponse res = DeleteFriendResponse.builder()
                .isSuccess(true)
                .build();

        return res;
    }


}
