package com.app.tournify.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class UploadImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String uploadImage(MultipartFile image) {

        String filePath = null;

        if (!image.isEmpty()) {
            String fileName = image.getOriginalFilename();

            try {
                File directory = new File(uploadPath);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File serverFile = new File(directory.getAbsolutePath() + File.separator + fileName);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));

                stream.write(image.getBytes());
                stream.close();

                filePath = serverFile.getAbsolutePath();

            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image due to: " + e.getMessage());
            }
        }
        return filePath;
    }
}
