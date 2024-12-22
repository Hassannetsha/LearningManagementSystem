package org.example.lmsproject.userPart.service;

import org.example.lmsproject.userPart.model.AuthRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private AuthService authService;

    @Test
    void testVerify_successfulAuthentication() {
        AuthRequest authRequest = new AuthRequest("validUsername", "validPassword");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken("validUsername")).thenReturn("Valid_Jwt_Token");
        String token = authService.verify(authRequest);
        assertEquals("Valid_Jwt_Token", token);
        verify(authenticationManager,times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService,times(1)).generateToken("validUsername");
    }

    @Test
    void testVerify_unsuccessfulAuthentication() {
        AuthRequest authRequest = new AuthRequest("invalidUsername", "invalidPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("Invalid user request!"));
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            authService.verify(authRequest);
        });
        assertEquals("Invalid user request!", thrown.getMessage());
        verify(authenticationManager,times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
    }
}
