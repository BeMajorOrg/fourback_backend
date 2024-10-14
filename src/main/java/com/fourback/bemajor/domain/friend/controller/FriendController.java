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

        AddFriendApplyResponse res = friendService.addFriendApply(request);
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
    public ResponseEntity<List<String>> getFriendInvitationList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<String> res = friendService.getFriendInvitationList(customUserDetails.getUserId());
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/friend/apply/{userId}")
    public ResponseEntity<GetFriendAppliesResponse> getFriendApplies(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        GetFriendAppliesResponse res = friendService.getReceiveApplies(userId);
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/friend/apply/count/{userId}")
    public ResponseEntity<CountFriendApplyResponse> countFriendApply(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        CountFriendApplyResponse res = friendService.countApplies(userId);
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @GetMapping("/api/friend/{userId}")
    public ResponseEntity<GetFriendListResponse> getFriendList(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        GetFriendListResponse res = friendService.getFriendList(userId);
        return ResponseEntity.ok().body(res);
    }

    @ResponseBody
    @DeleteMapping("/api/friend/apply")
    public ResponseEntity<DeleteFriendResponse> deleteFriend(
            @RequestBody FriendRequest.Delete request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        DeleteFriendResponse res = friendService.deleteFriend(request);
        return ResponseEntity.ok().body(res);
    }

}
