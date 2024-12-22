package org.example.lmsproject.userPart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.lmsproject.userPart.model.User;
import org.example.lmsproject.userPart.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("zinab");
        User user2 = new User();
        user2.setUsername("nada");
        List<User> users = Arrays.asList(user1, user2);
        when(userService.findAllUsers()).thenReturn(users);
        mockMvc.perform(get("/admin/getUsers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("zinab, nada"));
    }

    @Test
    void testGetUser() throws Exception {
        Long userId = 1L;

        User user = new User();
        user.setUsername("nada");
        user.setEmail("nada@example.com");
        user.setRole(User.Role.ROLE_STUDENT);
        user.setPassword("password123");

        when(userService.getUser(userId)).thenReturn(user);

        mockMvc.perform(get("/api/getUser/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User: " + user.toString()));
    }
}
