package com.fourback.bemajor.domain.studyGroupNotification.controller;

import com.fourback.bemajor.domain.studyGroupNotification.service.StudyGroupNotificationService;
import com.fourback.bemajor.global.common.util.ResponseUtil;
import com.fourback.bemajor.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studyGroup/notification")
public class StudyGroupNotificationController {
    private final StudyGroupNotificationService studyGroupNotificationService;

    @PostMapping("/{studyGroupId}")
    public ResponseEntity<?> enableNotifications(
            @PathVariable("studyGroupId") Long studyGroupId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long response = studyGroupNotificationService
                .enableNotification(studyGroupId, userDetails.getUserId());
        return ResponseUtil.onSuccess(response);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> disableNotifications(
            @PathVariable("notificationId") Long notificationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        studyGroupNotificationService
                .disableNotification(notificationId, userDetails.getUserId());
        return ResponseUtil.onSuccess();
    }
}
