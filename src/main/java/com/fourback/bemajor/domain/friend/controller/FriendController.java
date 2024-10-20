//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.fourback.bemajor.domain.friend.controller;

import com.fourback.bemajor.domain.friend.dto.*;
import com.fourback.bemajor.domain.friend.service.FriendService;
import com.fourback.bemajor.global.security.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json;charset=UTF-8")
public class FriendController {

    private final FriendService friendService;

    @ResponseBody
    @PostMapping("/api/friend/apply")
    public ResponseEntity<AddFriendApplyResponse> addFriendApply(
            @RequestBody FriendRequest.Apply request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        AddFriendApplyResponse res = friendService.addFriendApply(customUserDetails.getUserId(), request);
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @PostMapping("/api/friend/apply/{applyId}")
    public ResponseEntity<AcceptFriendApplyResponse> acceptFriend(
            @PathVariable("applyId") Long applyId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        AcceptFriendApplyResponse res = friendService.acceptFriendApply(applyId);
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/friend/invitation-list")
    public ResponseEntity<GetUserListForInvitationResponse> getFriendInvitationList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        GetUserListForInvitationResponse res = friendService.getFriendInvitationList(customUserDetails.getUserId());
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/friend/apply")
    public ResponseEntity<GetFriendAppliesResponse> getFriendApplies(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        GetFriendAppliesResponse res = friendService.getReceiveApplies(customUserDetails.getUserId());
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/friend/apply/count")
    public ResponseEntity<CountFriendApplyResponse> countFriendApply(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        CountFriendApplyResponse res = friendService.countApplies(customUserDetails.getUserId());
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/friend")
    public ResponseEntity<GetFriendListResponse> getFriendList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        GetFriendListResponse res = friendService.getFriendList(customUserDetails.getUserId());
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @DeleteMapping("/api/friend")
    public ResponseEntity<DeleteFriendResponse> deleteFriend(
            @RequestBody FriendRequest.Delete request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        DeleteFriendResponse res = friendService.deleteFriend(customUserDetails.getUserId(), request);
        return ResponseEntity.ok().body(res);
    }

}
