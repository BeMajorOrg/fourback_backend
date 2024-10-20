package com.fourback.bemajor.domain.user.controller;

import com.fourback.bemajor.domain.user.dto.request.FcmTokenUpdateDto;
import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserResponseDto;
import com.fourback.bemajor.domain.user.service.UserService;
import com.fourback.bemajor.global.common.util.ResponseUtil;
import com.fourback.bemajor.global.security.custom.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        List<Pair<String, String>> tokens = userService.save(userLoginRequestDto);
        return ResponseUtil.onSuccess(tokens);

    }

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UserResponseDto userResponseDto = userService.get(customUserDetails.getUserId());
        return ResponseUtil.onSuccess(userResponseDto);
    }

//    @GetMapping("/{email}")
//    public ResponseEntity<?> getUserByEmail(
//            @PathVariable("email") String email,
//            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        UserResponseDto userResponseDto = userService.getByEmail(email);
//        return Response.onSuccess(userResponseDto);
//    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.update(userUpdateRequestDto, customUserDetails.getUserId());
        return ResponseUtil.onSuccess();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails)
            throws IOException {
        userService.delete(customUserDetails.getUserId());
        return ResponseUtil.onSuccess();
    }

    @PostMapping("/image")
    public ResponseEntity<?> saveImage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @RequestParam("file") MultipartFile file) throws IOException {
        String filename = userService.saveImage(customUserDetails.getUserId(), file);
        return ResponseUtil.onSuccess(filename);
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        userService.deleteImage(customUserDetails.getUserId());
        return ResponseUtil.onSuccess();
    }

    @PatchMapping
    public ResponseEntity<?> updateFcmToken(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                            @RequestBody FcmTokenUpdateDto fcmTokenUpdateDto) {
        userService.updateFcmToken(customUserDetails.getUserId(), fcmTokenUpdateDto);
        return ResponseUtil.onSuccess();
    }
}

