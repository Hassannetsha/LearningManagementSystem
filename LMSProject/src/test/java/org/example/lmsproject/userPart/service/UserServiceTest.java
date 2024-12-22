package org.example.lmsproject.userPart.service;

import static org.junit.jupiter.api.Assertions.*;

import org.example.lmsproject.userPart.model.*;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    // Test for loadUserByUsername
    @Test
    public void test_loadUserByUsername_userFound() {
        // Arrange
        String username = "testUser";
        User mockUser = new Admin("testUser", "password", "email@example.com");
        when(userRepository.findByusername(username)).thenReturn(Optional.of(mockUser));
        // Act
        User_Details userDetails = (User_Details) userService.loadUserByUsername(username);
        // Assert
        assertNotNull(userDetails);
        assertEquals(mockUser.getUsername(), userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());
        verify(userRepository, times(1)).findByusername(username); // Ensure method is called once
    }

    // Test for loadUserByUsername when user not found
    @Test
    public void test_loadUserByUsername_userNotFound() {
        // Arrange
        String username = "nonExistingUser";
        when(userRepository.findByusername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });

        assertEquals("User not found with id: " + username, thrown.getMessage());
    }

    // Test for findAllUsers
    @Test
    public void test_findAllUsers() {
        // Arrange
        User user1 = new Student("user1", "password1", "email1@example.com");
        User user2 = new Instructor("user2", "password2", "email2@example.com");
        when(userRepository.findAll()).thenReturn(java.util.List.of(user1, user2));
        // Act
        var users = userService.findAllUsers();
        // Assert
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        verify(userRepository, times(1)).findAll();
    }

    // Test for getUser
    @Test
    public void test_getUser_userFound() {
        // Arrange
        Long userId = 1L;
        User mockUser = new Admin("testUser", "password", "email@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        // Act
        User user = userService.getUser(userId);
        // Assert
        assertNotNull(user);
        assertEquals(mockUser.getUsername(), user.getUsername());
        verify(userRepository, times(1)).findById(userId);
    }

    // Test for getUser when user not found
    @Test
    public void test_getUser_userNotFound() {
        // Arrange
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // Act
        User user = userService.getUser(userId);
        // Assert
        assertNull(user);
        verify(userRepository, times(1)).findById(userId);
    }
}
