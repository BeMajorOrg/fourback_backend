package com.fourback.bemajor.service;

import com.fourback.bemajor.domain.Image;
import com.fourback.bemajor.domain.User;
import com.fourback.bemajor.domain.UserImage;
import com.fourback.bemajor.exception.NotFoundElementException;
import com.fourback.bemajor.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private static final String UPLOAD_DIR = "uploads/";
    private final UserService userService;
    private final ImageFileService imageFileService;

    public Image findByFileName(String fileName) {
        Optional<Image> oi = imageRepository.findByFileName(fileName);
        if (oi.isEmpty()) {
            throw new NotFoundElementException(1, "That is not in DB", HttpStatus.NOT_FOUND);
        }
        return oi.get();
    }

    @Transactional
    public Resource get(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
        return new UrlResource(filePath.toUri());
    }

    @Transactional
    public void saveImage(Image image, MultipartFile file) throws IOException {
        if (file != null) {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String uniqueFilename = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(UPLOAD_DIR, uniqueFilename);
            imageFileService.saveImageFile(filePath,file);
            image.setFilePath(filePath.toString());
            image.setFileName(uniqueFilename);
        }
        imageRepository.save(image);
    }

    @Transactional
    public void delete(List<String> fileNames) throws IOException {
        for (String fileName : fileNames) {
            Image image = findByFileName(fileName);
            imageFileService.deleteImageFile(image.getFilePath());
            imageRepository.delete(image);
        }
    }

    @Transactional
    public void save(String oauth2Id, MultipartFile file) throws IOException {
        if (file != null) {
            User user = userService.findByOauth2Id(oauth2Id);
            UserImage image = new UserImage();
            user.setUserImage(image);
            image.setUser(user);
            this.saveImage(image, file);
        }
    }
}
