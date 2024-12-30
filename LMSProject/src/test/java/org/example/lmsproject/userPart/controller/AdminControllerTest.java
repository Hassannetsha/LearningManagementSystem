package org.example.lmsproject.userPart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lmsproject.Notification.Services.MailboxService;
import org.example.lmsproject.Notification.TextMappers.NotificationAndEmailMapper;
import org.example.lmsproject.Notification.TextMappers.ResponseNotification;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.model.Response;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.repository.RequestRepository;
import org.example.lmsproject.userPart.service.AdminService;
import org.example.lmsproject.userPart.service.ResponseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


class AdminControllerTest {

    private MockMvc mockMvc;
    @Mock
    private AdminService adminService;
    @InjectMocks
    private AdminController adminController;
    private ObjectMapper objectMapper;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private MailboxService mailboxService;

    @Mock
    private ResponseService responseService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddNewUser() throws Exception {
        User userRequest = new User();
        userRequest.setUsername("menna");
        userRequest.setEmail("menna@example.com");
        userRequest.setRole(User.Role.ROLE_STUDENT);
        userRequest.setPassword("password123");

        User createdUser = new User();
        createdUser.setUsername("menna");

        when(adminService.createUserByRole(any(User.class))).thenReturn(createdUser);
        when(adminService.addUser(any(User.class))).thenReturn("User added to the database");

        mockMvc.perform(post("/admin/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect( content().string("menna Added Successfully\nUser added to the database"));

        verify(adminService, times(1)).createUserByRole(any(User.class));
        verify(adminService, times(1)).addUser(any(User.class));
    }

    @Test
    void testResponse_UserAdded() throws Exception {
        // Arrange: Mock request and response objects
        Response responseRequest = new Response();
        responseRequest.setId(1L);
        responseRequest.setState(1);

        Request mockRequest = new Request();
        mockRequest.setId(1L);
        mockRequest.setUsername("nada");
        mockRequest.setEmail("nada@example.com");
        mockRequest.setRole(User.Role.ROLE_ADMIN);
        mockRequest.setPassword("securepassword");

        User newUser = new User();
        newUser.setUsername(mockRequest.getUsername());
        newUser.setEmail(mockRequest.getEmail());
        newUser.setRole(mockRequest.getRole());
        newUser.setPassword(mockRequest.getPassword());

        // Mock service behavior
        when(adminService.getRequestByID(responseRequest.getId())).thenReturn(mockRequest);
        when(adminService.createUserByRole(any(User.class))).thenReturn(newUser);
        when(adminService.addUser(any(User.class))).thenReturn("Admin user added");
        doNothing().when(requestRepository).delete(mockRequest);
        doNothing().when(mailboxService).addNotification(eq(newUser.getId()), any(ResponseNotification.class));

        // Act: Perform the POST request
        mockMvc.perform(post("/admin/response")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseRequest)))
                .andExpect(status().isOk()) // Assert: HTTP 200
                .andExpect(content().string("Response processed successfully.")); // Assert: Response content
    }


    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setUsername("updated_user");
        updatedUser.setEmail("updated55@example.com");
        when(adminService.updateUser(eq(userId), any(User.class))).thenReturn("User updated successfully");
        mockMvc.perform(put("/admin/updateUser/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect( content().string("User updated successfully"));
        verify(adminService, times(1)).updateUser(eq(userId), any(User.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;
        when(adminService.deleteUser(userId)).thenReturn("User deleted successfully");
        mockMvc.perform(delete("/admin/deleteUser/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
        verify(adminService, times(1)).deleteUser(userId);
    }
}
