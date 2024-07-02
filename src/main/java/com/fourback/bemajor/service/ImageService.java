package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.Image;
import com.fourback.bemajor.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Transactional
    public ResponseEntity<String> delete(List<String> fileNames) {
        boolean qwe = false;
        for (String fileName : fileNames) {
            Optional<Image> optionalImage = imageRepository.findByFileName(fileName);

            if (optionalImage.isPresent()) {
                Image image = optionalImage.get();

                Path filePath = Paths.get(image.getFilePath());


                try {

                    // 파일 시스템에서 파일 삭제
                    Files.deleteIfExists(filePath);

                    // 데이터베이스에서 이미지 기록 삭제
                    imageRepository.delete(image);


                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            }
        }

        return ResponseEntity.ok("Image deleted successfully");

    }

//    public void delete() {
//        Path filePath = Paths.get(image.getFilePath());
//        Files.deleteIfExists(filePath);
//    }

}
