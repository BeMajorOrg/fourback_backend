package com.fourback.bemajor.domain.aws.controller;

import com.fourback.bemajor.domain.aws.dto.PreSignedUrlResponseDto;
import com.fourback.bemajor.domain.aws.service.S3ImageService;
import com.fourback.bemajor.global.common.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class S3ImageController {
    private final S3ImageService s3ImageService;

    @GetMapping
    public ResponseEntity<?> getPreSignedUrl(@RequestParam("imageType") String imageType) {
        PreSignedUrlResponseDto responseDto = s3ImageService.generatePreSignedUrl(imageType);

        return ResponseUtil.onSuccess(responseDto);
    }
}
