package com.fourback.bemajor.domain.user.controller;

import com.fourback.bemajor.domain.user.dto.request.UserLoginRequestDto;
import com.fourback.bemajor.domain.user.dto.request.UserUpdateRequestDto;
import com.fourback.bemajor.domain.user.dto.response.UserInquiryResponseDto;
import com.fourback.bemajor.domain.user.service.UserService;
import com.fourback.bemajor.global.common.util.ResponseUtil;
import com.fourback.bemajor.global.security.custom.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserLoginRequestDto requestDto) {
        List<Pair<String, String>> tokens = userService.save(requestDto);
        return ResponseUtil.onSuccess(tokens);

    }

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInquiryResponseDto responseDto = userService.get(userDetails.getUserId());
        return ResponseUtil.onSuccess(responseDto);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequestDto requestDto,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.update(requestDto, userDetails.getUserId());
        return ResponseUtil.onSuccess();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.delete(userDetails.getUserId());
        return ResponseUtil.onSuccess();
    }

    @PostMapping("/image")
    public ResponseEntity<?> saveImage(@RequestParam("imageUrl") String imageUrl,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        String savedImageUrl = userService.saveImage(imageUrl, userDetails.getUserId());
        return ResponseUtil.onSuccess(imageUrl);
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteImage(userDetails.getUserId());
        return ResponseUtil.onSuccess();
    }
}
