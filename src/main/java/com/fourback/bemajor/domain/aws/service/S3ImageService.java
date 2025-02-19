package com.fourback.bemajor.domain.aws.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fourback.bemajor.domain.aws.dto.PreSignedUrlResponseDto;
import com.fourback.bemajor.domain.aws.util.ImageMimeTypeUtil;
import com.fourback.bemajor.global.exception.kind.FilenameInvalidException;

import com.fourback.bemajor.global.exception.kind.S3ContentTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

/**
 * S3 업로드 서비스
 */
@Service
@RequiredArgsConstructor
public class S3ImageService {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public PreSignedUrlResponseDto generatePreSignedUrl(String imageType) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(imageType);

    String preSignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    String imageSavedUrl = amazonS3.getUrl(bucket, generatePresignedUrlRequest.getKey()).toString();

    return PreSignedUrlResponseDto.of(preSignedUrl, imageSavedUrl);
  }

  private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String imageType) {
    String savedFileName = UUID.randomUUID() + "." + imageType;
    String mimeType = ImageMimeTypeUtil.getImageMimeType(imageType);

    if (mimeType == null) {
      throw new S3ContentTypeException("이미지 타입이 아닙니다.");
    }

    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, savedFileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
            .withContentType(mimeType);

    generatePresignedUrlRequest.addRequestParameter(
            Headers.S3_CANNED_ACL,
            CannedAccessControlList.PublicRead.toString()
    );

    return generatePresignedUrlRequest;
  }

  public void deleteFile(String imageUrl) {
    if (imageUrl != null) {
      amazonS3.deleteObject(bucket, imageUrl);
    }
  }

  public void deleteFiles(List<String> imageUrls) {
    if (imageUrls != null && !imageUrls.isEmpty()) {
      List<KeyVersion> keyVersions = imageUrls.stream()
              .map(KeyVersion::new).toList();
      DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket)
              .withKeys(keyVersions);
      amazonS3.deleteObjects(deleteObjectsRequest);
    }
  }
}
