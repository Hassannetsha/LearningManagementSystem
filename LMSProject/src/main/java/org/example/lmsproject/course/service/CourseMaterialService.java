package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.course.repository.CourseMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

@Service
public class CourseMaterialService {


    @Value("${upload.directory}")
    String uploadDirectory;

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    public CourseMaterial getByFilename(String filename) {
        return courseMaterialRepository.findByFilename(filename);
    }

    public String uploadMaterial(Course course, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isEmpty()) {
            throw new RuntimeException("Filename cannot be empty");
        }

        try {
            File directory = new File(uploadDirectory);
            if (!directory.exists() && !directory.mkdirs()) {
                System.out.println("Failed to create directory: {}"+ uploadDirectory);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create directory: " + uploadDirectory);
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + filename);
            Files.write(path, bytes);

            CourseMaterial courseMaterial = new CourseMaterial(filename, path.toString());
            courseMaterial.setCourse(course);
            courseMaterialRepository.save(courseMaterial);

            return "File uploaded successfully";

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while uploading the file: " + e.getMessage());
        }
    }
    public Optional<byte[]> getMaterial(String filename) {
        CourseMaterial courseMaterial = courseMaterialRepository.findByFilename(filename);
        if (courseMaterial == null) {
            return Optional.empty();
        }

        Path filePath = Paths.get(courseMaterial.getPath()).toAbsolutePath().normalize();

        if (!Files.exists(filePath)) {
            System.out.println("File not found on the server: {}" +  filePath);
            return Optional.empty();
        }
        try {
            byte[] fileContent = Files.readAllBytes(filePath);
//            Resource file = new UrlResource(filePath.toUri());

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return Optional.of(fileContent);

        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public String deleteMaterial(String filename) {
        CourseMaterial courseMaterial = courseMaterialRepository.findByFilename(filename);
        if (courseMaterial == null) {
            return "Invalid file name";
        }
        File file = new File(courseMaterial.getPath());
        boolean deleted = file.delete();
        if (!deleted) {
            return "Failed to delete file";
        }
        courseMaterialRepository.delete(courseMaterial);
        return "File deleted successfully";
    }


}
