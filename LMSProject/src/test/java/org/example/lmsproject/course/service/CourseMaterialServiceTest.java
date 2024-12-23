package org.example.lmsproject.course.service;

import org.example.lmsproject.course.model.Course;
import org.example.lmsproject.course.model.CourseMaterial;
import org.example.lmsproject.course.repository.CourseMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseMaterialServiceTest {

    @InjectMocks
    private CourseMaterialService courseMaterialService;

    @Mock
    private CourseMaterialRepository courseMaterialRepository;

    @BeforeEach
    void setUp() {
        // Set the upload directory for testing
        courseMaterialService.uploadDirectory = "test-uploads/";
    }

    @Test
    void testUploadMaterial_Success() throws IOException {
        Course course = new Course();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());

        Path uploadPath = Paths.get(courseMaterialService.uploadDirectory);
        if (Files.exists(uploadPath)) {
            Files.walk(uploadPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        Files.createDirectories(uploadPath);

        when(courseMaterialRepository.save(any(CourseMaterial.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = courseMaterialService.uploadMaterial(course, file);

        Path filePath = Paths.get(courseMaterialService.uploadDirectory + file.getOriginalFilename());
        assertTrue(Files.exists(filePath));
        assertEquals("test content", new String(Files.readAllBytes(filePath)));
        assertEquals("File uploaded successfully", result);

        verify(courseMaterialRepository, times(1)).save(any(CourseMaterial.class));
    }

    @Test
    void testUploadMaterial_EmptyFilename() {
        Course course = new Course();
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", "test content".getBytes());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            courseMaterialService.uploadMaterial(course, file);
        });

        assertEquals("Filename cannot be empty", exception.getMessage());
    }

    @Test
    void testGetMaterialSuccess() throws Exception {
        String filename = "test.txt";
        Path filePath = Paths.get(courseMaterialService.uploadDirectory + filename);
        byte[] fileContent = "test content".getBytes();

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, fileContent);

        CourseMaterial courseMaterial = new CourseMaterial(filename, filePath.toString());
        when(courseMaterialRepository.findByFilename(anyString())).thenReturn(courseMaterial);

        Optional<byte[]> result = courseMaterialService.getMaterial(filename);

        assertTrue(result.isPresent());
        assertArrayEquals(fileContent, result.get());
    }


    @Test
    void testDeleteMaterialSuccess() {
        String filename = "test.txt";
        CourseMaterial courseMaterial = new CourseMaterial(filename, "path/to/test.txt");

        when(courseMaterialRepository.findByFilename(filename)).thenReturn(courseMaterial);


        try (MockedConstruction<File> mockedFile = mockConstruction(File.class, (mock, context) -> {
            when(mock.getPath()).thenReturn(courseMaterial.getPath());
            when(mock.delete()).thenReturn(true);
        })) {
            String result = courseMaterialService.deleteMaterial(filename);

            assertEquals("File deleted successfully", result);

            verify(courseMaterialRepository, times(1)).delete(courseMaterial);
        }
    }

    @Test
    void testDeleteMaterialFileNotFound() {
        String filename = "test.txt";

        when(courseMaterialRepository.findByFilename(filename)).thenReturn(null);

        String result = courseMaterialService.deleteMaterial(filename);

        assertEquals("Invalid file name", result);

        verify(courseMaterialRepository, never()).delete(any(CourseMaterial.class));
    }

    @Test
    void testDeleteMaterialDeletionFailed() {
        String filename = "test.txt";
        CourseMaterial courseMaterial = new CourseMaterial(filename, "path/to/test.txt");

        when(courseMaterialRepository.findByFilename(filename)).thenReturn(courseMaterial);


        try (MockedConstruction<File> mockedFile = mockConstruction(File.class, (mock, context) -> {
            when(mock.getPath()).thenReturn(courseMaterial.getPath());
            when(mock.delete()).thenReturn(false);
        })) {
            String result = courseMaterialService.deleteMaterial(filename);

            assertEquals("Failed to delete file", result);

            verify(courseMaterialRepository, never()).delete(any(CourseMaterial.class));
        }
    }


}
