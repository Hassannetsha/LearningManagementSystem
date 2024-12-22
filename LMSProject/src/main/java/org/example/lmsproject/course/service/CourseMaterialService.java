package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.course.repository.CourseMaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class CourseMaterialService {

    private static final Logger logger = LoggerFactory.getLogger(CourseMaterialService.class);

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    public CourseMaterial getByFilename(String filename) {
        return courseMaterialRepository.findByFilename(filename);
    }

    public ResponseEntity<String> uploadMaterial(Course course, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid file name");
        }

        try {
            File directory = new File(uploadDirectory);
            if (!directory.exists() && !directory.mkdirs()) {
                logger.error("Failed to create directory: {}", uploadDirectory);
                return ResponseEntity.status(500).body("Failed to create upload directory");
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + filename);
            Files.write(path, bytes);

            CourseMaterial courseMaterial = new CourseMaterial(filename, path.toString());
            courseMaterial.setCourse(course);
            courseMaterialRepository.save(courseMaterial);

            return ResponseEntity.ok("File uploaded successfully");

        } catch (IOException e) {
            logger.error("Error occurred while uploading the file: {}", e.getMessage());
            return ResponseEntity.status(500).body("Internal server error occurred while uploading file");
        }
    }
    public ResponseEntity<byte[]> getMaterial(String filename) {


        CourseMaterial courseMaterial = courseMaterialRepository.findByFilename(filename);
        if (courseMaterial == null) {
            return ResponseEntity.status(404).body(null); // Not found
        }

        Path filePath = Paths.get(courseMaterial.getPath()).toAbsolutePath().normalize();

        if (!Files.exists(filePath)) {
            logger.error("File not found on the server: {}", filePath);
            return ResponseEntity.status(404).body(null); // Not found
        }

        try {
            byte[] fileContent = Files.readAllBytes(filePath);
//            Resource file = new UrlResource(filePath.toUri());

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default to binary stream if type can't be determined
            }
            System.out.println(filePath.toString());
            System.out.println("Content type: " + contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(fileContent);

        } catch (IOException e) {
            logger.error("Error occurred while retrieving the file: {}", e.getMessage());
            return ResponseEntity.status(500).body(null); // Internal server error
        }
    }

    public ResponseEntity<String> deleteMaterial(String filename) {
        CourseMaterial courseMaterial = courseMaterialRepository.findByFilename(filename);
        if (courseMaterial == null) {
            return ResponseEntity.status(404).body("Invalid file name");
        }
        File file = new File(courseMaterial.getPath());
        boolean deleted = file.delete();
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete file");
        }
        courseMaterialRepository.delete(courseMaterial);
        return ResponseEntity.ok("File deleted successfully");
    }


}
