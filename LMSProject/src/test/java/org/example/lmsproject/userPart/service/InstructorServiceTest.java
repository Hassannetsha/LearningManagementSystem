package org.example.lmsproject.userPart.service;

import org.example.lmsproject.userPart.model.Instructor;
import org.example.lmsproject.userPart.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InstructorServiceTest {

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private InstructorService instructorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_find_by_id_exist() {
        // Arrange
        long instructorId = 1L;
        Instructor instructor = new Instructor( "nada" , "123456789", "nada@example.com");
        instructor.setId(instructorId);
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.of(instructor));
        // Act
        Instructor result = instructorService.findById(instructorId);
        // Assert
        assertNotNull(result);
        assertEquals(instructorId, result.getId());
        assertEquals("nada", result.getUsername());
        verify(instructorRepository, times(1)).findById(instructorId);
    }

    @Test
    void test_find_b_id_notExist() {
        // Arrange
        long instructorId = 1L;
        when(instructorRepository.findById(instructorId)).thenReturn(Optional.empty());
        // Act
        Instructor result = instructorService.findById(instructorId);
        // Assert
        assertNull(result);
        verify(instructorRepository, times(1)).findById(instructorId);
    }

    @Test
    void test_find_by_username_exist() {
        // Arrange
        String username = "zinab";
        Instructor instructor = new Instructor(username,"123456789", "zinab@example.com");
        when(instructorRepository.findByUsername(username)).thenReturn(Optional.of(instructor));
        // Act
        Instructor result = instructorService.findByUsername(username);
        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(instructorRepository, times(1)).findByUsername(username);
    }

    @Test
    void test_find_by_username_notExist() {
        // Arrange
        String username = "nada";
        when(instructorRepository.findByUsername(username)).thenReturn(Optional.empty());
        // Act
        Instructor result = instructorService.findByUsername(username);
        // Assert
        assertNull(result);
        verify(instructorRepository, times(1)).findByUsername(username);
    }

}