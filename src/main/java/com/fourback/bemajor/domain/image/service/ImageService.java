package com.fourback.bemajor.domain.image.service;

import com.fourback.bemajor.domain.image.entity.Image;
import com.fourback.bemajor.domain.image.entity.UserImage;
import com.fourback.bemajor.domain.image.repository.ImageRepository;
import com.fourback.bemajor.domain.user.entity.UserEntity;
import com.fourback.bemajor.domain.user.repository.UserRepository;
import com.fourback.bemajor.global.exception.ExceptionEnum;
import com.fourback.bemajor.global.exception.kind.NotFoundElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final UserRepository userRepository;
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
            imageFileService.saveImageFile(filePath, file);
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
    public String save(Long userId, MultipartFile file) throws IOException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(),
                "This is not in DB", HttpStatus.LOCKED));
        UserImage image = new UserImage();
        userEntity.setUserImage(image);
        image.setUser(userEntity);
        this.saveImage(image, file);
        return image.getFileName();
    }
}
