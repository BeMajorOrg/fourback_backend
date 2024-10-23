package com.fourback.bemajor.domain.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fourback.bemajor.global.exception.kind.FilenameInvalidException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
  private final String SPLIT_STRING = ".com/";

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

  public void deleteFile(String imageUrl) {
    if(imageUrl!=null) {
      String fileName = imageUrl.substring(imageUrl.lastIndexOf(SPLIT_STRING) + SPLIT_STRING.length());
      amazonS3.deleteObject(bucket, fileName);
    }
  }

  public void deleteFiles(List<String> imageUrls) {
    if(imageUrls!=null && !imageUrls.isEmpty()) {
      List<KeyVersion> keyVersions = imageUrls.stream()
              .map(url -> url.substring(url.lastIndexOf(SPLIT_STRING) + SPLIT_STRING.length()))
              .map(KeyVersion::new).toList();
      DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket)
              .withKeys(keyVersions);
      amazonS3.deleteObjects(deleteObjectsRequest);
    }
  }

  private String extractExtension(String originalFileName) {
    return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
  }
}