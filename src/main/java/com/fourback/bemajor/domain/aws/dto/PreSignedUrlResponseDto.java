package com.fourback.bemajor.domain.aws.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PreSignedUrlResponseDto {
    private String preSingedUrl;
    private String imageSaveUrl;

    public static PreSignedUrlResponseDto of(String preSingedUrl, String imageSaveUrl){
        return PreSignedUrlResponseDto.builder()
                .preSingedUrl(preSingedUrl)
                .imageSaveUrl(imageSaveUrl)
                .build();
    }
}
