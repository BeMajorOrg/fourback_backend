package com.fourback.bemajor.domain.studyGroupNotification.controller;

import com.fourback.bemajor.domain.studyGroupNotification.service.StudyGroupNotificationService;
import com.fourback.bemajor.global.common.util.ResponseUtil;
import com.fourback.bemajor.global.security.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studyGroup/notification/{studyGroupId}")
public class StudyGroupNotificationController {
    private final StudyGroupNotificationService studyGroupNotificationService;

    @PostMapping
    public ResponseEntity<?> enableNotifications(
            @PathVariable("studyGroupId") Long studyGroupId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        studyGroupNotificationService
                .enableRealTimeNotification(studyGroupId, userDetails.getUserId());
        return ResponseUtil.onSuccess();
    }

    @DeleteMapping
    public ResponseEntity<?> disableNotifications(
            @PathVariable("studyGroupId") Long studyGroupId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        studyGroupNotificationService
                .disableNotification(studyGroupId, userDetails.getUserId());
        return ResponseUtil.onSuccess();
    }
}
