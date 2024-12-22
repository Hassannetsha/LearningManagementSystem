package org.example.lmsproject.userPart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lmsproject.userPart.model.AuthRequest;
import org.example.lmsproject.userPart.model.Request;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.AdminService;
import org.example.lmsproject.userPart.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("mariam");
        authRequest.setPassword("password123");
        when(authService.verify(any(AuthRequest.class))).thenReturn("Login successful");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));

        // Verify service interaction
        verify(authService, times(1)).verify(any(AuthRequest.class));
    }

    @Test
    void testSignup() throws Exception {
        Request userRequest = new Request();
        userRequest.setUsername("omnia");
        userRequest.setEmail("omnia24@example.com");
        userRequest.setRole(User.Role.ROLE_STUDENT);
        userRequest.setPassword("password123");
        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Request sent\n"));
        verify(adminService, times(1)).sendRequest(any(Request.class));
    }

    @Test
    void testGetRequests() throws Exception {
        List<Request> mockRequests = new ArrayList<>();
        Request request1 = new Request();
        request1.setId(1L);
        request1.setUsername("zinab");
        request1.setEmail("zinab@example.com");
        request1.setRole(User.Role.ROLE_STUDENT);
        request1.setPassword("password123");

        Request request2 = new Request();
        request2.setId(2L);
        request2.setUsername("nada");
        request2.setEmail("nada@example.com");
        request2.setRole(User.Role.ROLE_ADMIN);
        request2.setPassword("securepassword");

        mockRequests.add(request1);
        mockRequests.add(request2);

        when(adminService.getRequests()).thenReturn(mockRequests);
        mockMvc.perform(get("/admin/requests"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockRequests)));
        verify(adminService, times(1)).getRequests();
    }
}
