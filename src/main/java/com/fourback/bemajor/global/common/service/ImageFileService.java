package com.fourback.bemajor.global.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageFileService {
    private static final String UPLOAD_DIR = "uploads/";

    public void deleteImageFile(String fileName) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.deleteIfExists(path);
    }

    public String saveImageFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(
                originalFilename.lastIndexOf('.'));
        String uniqueFileName = UUID.randomUUID() + extension;
        Path filePath = Paths.get(UPLOAD_DIR, uniqueFileName);
        if (!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        Files.write(filePath, file.getBytes());
        return uniqueFileName;
    }
}
