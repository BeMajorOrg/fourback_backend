package com.fourback.bemajor.domain.image.service;

import com.fourback.bemajor.domain.image.entity.ImageEntity;
import com.fourback.bemajor.domain.image.repository.ImageRepository;
import com.fourback.bemajor.global.common.service.ImageFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageFileService imageFileService;

    private static final String UPLOAD_DIR = "uploads/";

    public Resource get(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        return new UrlResource(filePath.toUri());
    }

    @Transactional
    public void save(ImageEntity image, MultipartFile file) throws IOException {
        String uniqueFilename = imageFileService.saveImageFile(file);
        String filePath = UPLOAD_DIR + uniqueFilename;
        image.setFilePath(filePath);
        image.setFileName(uniqueFilename);
        imageRepository.save(image);
    }

    @Transactional
    public void delete(List<String> fileNames) throws IOException {
        for (String fileName : fileNames) {
            Optional<ImageEntity> oi = imageRepository.findByFileName(fileName);
            if (oi.isEmpty()) continue;
            ImageEntity image = oi.get();
            imageFileService.deleteImageFile(image.getFilePath());
            imageRepository.delete(image);
        }
    }
}
