package com.fourback.bemajor.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fourback.bemajor.exception.FilenameInvalidException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

/**
 * S3 업로드 서비스
 */
@Service
@RequiredArgsConstructor
public class S3UploadService {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String saveFile(MultipartFile multipartFile) throws IOException {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(multipartFile.getSize());
    metadata.setContentType(multipartFile.getContentType());
    String originalFilename = multipartFile.getOriginalFilename();
    if (originalFilename == null) {
      throw new FilenameInvalidException("filename is null");
    }
    String extension = extractExtension(originalFilename);
    String fileName = UUID.randomUUID() + "." + extension;
    amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
    return amazonS3.getUrl(bucket, fileName).toString();
  }

  private String extractExtension(String originalFileName) {
    return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
  }
}