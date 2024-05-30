package com.app.tournify.services;

import com.app.tournify.database.repositories.TeamRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String uploadImage(MultipartFile image, String filename) {
        String filePath = null;

        if (!image.isEmpty()) {

            try {

                // create directory if it doesn't exist
                File directory = new File(uploadPath);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                File serverFile = new File(directory.getAbsolutePath() + File.separator + filename);

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
