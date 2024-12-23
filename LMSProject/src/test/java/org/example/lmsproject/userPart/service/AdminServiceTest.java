package org.example.lmsproject.userPart.service;

import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.userPart.model.*;
import org.example.lmsproject.userPart.repository.RequestRepository;
import org.example.lmsproject.userPart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestRepository requestRepo;
    @Mock
    private MailboxService mailboxService;
    @InjectMocks
    private AdminService adminService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserByRole_AdminRole() {
        User user = new User("adminUser", "password", "email@example.com", User.Role.ROLE_ADMIN);
        User createdUser = adminService.createUserByRole(user);
        assertInstanceOf(Admin.class, createdUser);
        assertEquals("adminUser", createdUser.getUsername());
        assertEquals(User.Role.ROLE_ADMIN, createdUser.getRole());
    }

    @Test
    public void testCreateUserByRole_InstructorRole() {
        User user = new User("instructorUser", "password", "email@example.com", User.Role.ROLE_INSTRUCTOR);
        User createdUser = adminService.createUserByRole(user);
        assertInstanceOf(Instructor.class, createdUser);
        assertEquals("instructorUser", createdUser.getUsername());
        assertEquals(User.Role.ROLE_INSTRUCTOR, createdUser.getRole());
    }

    @Test
    public void testCreateUserByRole_StudentRole() {
        User user = new User("studentUser", "password", "email@example.com", User.Role.ROLE_STUDENT);
        User createdUser = adminService.createUserByRole(user);
        assertInstanceOf(Student.class, createdUser);
        assertEquals("studentUser", createdUser.getUsername());
        assertEquals(User.Role.ROLE_STUDENT, createdUser.getRole());
    }

    // Test for addUser
    @Test
    public void testAddUser() {
        User user = new User("newUser", "password", "newEmail@example.com", User.Role.ROLE_STUDENT);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        String result = adminService.addUser(user);
        assertEquals("User added successfully", result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserWithNullUser() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createUserByRole(null);
        });
        assertEquals("User cannot be null", thrown.getMessage());
    }

    // Test for updateUser
    @Test
    public void testUpdateUser_UserFound() {
        Long userId = 1L;
        User existingUser = new User("existingUser", "password", "email@example.com", User.Role.ROLE_ADMIN);
        User updatedUser = new User("updatedUser", "newPassword", "newEmail@example.com", User.Role.ROLE_ADMIN);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(updatedUser.getPassword())).thenReturn("encodedNewPassword");
        String result = adminService.updateUser(userId, updatedUser);
        assertEquals("User updated successfully: updatedUser", result);
        assertEquals("updatedUser", existingUser.getUsername());
        assertEquals("encodedNewPassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        Long userId = 999L;
        User updatedUser = new User("updatedUser", "newPassword", "newEmail@example.com", User.Role.ROLE_ADMIN);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        String result = adminService.updateUser(userId, updatedUser);
        assertEquals("User with ID 999 not found.", result);
    }

    // Test for deleteUser
    @Test
    public void testDeleteUser_UserFound() {
        Long userId = 1L;
        User user = new User("userToDelete", "password", "email@example.com", User.Role.ROLE_ADMIN);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        String result = adminService.deleteUser(userId);
        assertEquals("User deleted successfully: userToDelete", result);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        String result = adminService.deleteUser(userId);
        assertEquals("User with ID 999 not found.", result);
    }

    // Test for sendRequest
    @Test
    public void testSendRequest_ValidRequest() {
        // Arrange
        Request request = new Request(1L, "username", "password", "email@example.com", User.Role.ROLE_STUDENT);
        Admin admin1 =new Admin("admin1", "admin1@example.com", "ROLE_ADMIN");
        Admin admin2 =new Admin("admin2", "admin2@example.com", "ROLE_ADMIN");
        admin1.setId(1L);
        admin2.setId(2L);
        List<Admin> mockAdmins = List.of(admin1, admin2);
        when(userRepository.findByusername("username")).thenReturn(Optional.empty());
        when(adminService.getAllAdmins()).thenReturn(mockAdmins);
        // Act
        adminService.sendRequest(request);
        // Assert
        verify(requestRepo, times(1)).save(request); // Verify request is saved
        verify(mailboxService, times(1))
                .addBulkNotifications(eq(List.of(1L, 2L)), eq(request)); // Verify notifications are sent to all admins
    }


    @Test
    public void testSendRequest_InvalidRequest() {
        Request invalidRequest = new Request(1L, null, "password", "email@example.com", User.Role.ROLE_STUDENT);
        assertThrows(IllegalArgumentException.class, () -> adminService.sendRequest(invalidRequest));
    }

    // Test for getRequests
    @Test
    public void testGetRequests() {
        Request request1 = new Request(1L, "user1", "password", "email1@example.com", User.Role.ROLE_STUDENT);
        Request request2 = new Request(2L, "user2", "password", "email2@example.com", User.Role.ROLE_INSTRUCTOR);
        when(requestRepo.findAll()).thenReturn(List.of(request1, request2));
        var requests = adminService.getRequests();
        assertEquals(2, requests.size());
        assertTrue(requests.contains(request1));
        assertTrue(requests.contains(request2));
    }

    // Test for getRequestByID
    @Test
    public void testGetRequestByID() {
        Long requestId = 1L;
        Request request = new Request(1L, "user1", "password", "email@example.com", User.Role.ROLE_STUDENT);
        when(requestRepo.findByid(requestId)).thenReturn(request);
        Request result = adminService.getRequestByID(requestId);
        assertEquals(request, result);
    }

}