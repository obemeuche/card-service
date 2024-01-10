package com.mintyn.cardservice;


import com.mintyn.cardservice.config.JwtUtils;
import com.mintyn.cardservice.entity.User;
import com.mintyn.cardservice.repository.UserRepository;
import com.mintyn.cardservice.request.LogInRequest;
import com.mintyn.cardservice.response.LogInResponse;
import com.mintyn.cardservice.service.LogInService;
import com.mintyn.cardservice.service.serviceImpl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogInServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LogInService signInService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signIn_SuccessfulSignIn_ReturnsResponseEntity() {
        LogInRequest logInRequest = new LogInRequest();
        logInRequest.setEmail("test@example.com");
        logInRequest.setPassword("password");

        User mockUser = new User();
        mockUser.setEmail(logInRequest.getEmail());

        UserDetails userDetails = mock(UserDetails.class);
        when(userRepository.findByEmail(logInRequest.getEmail())).thenReturn(Optional.of(mockUser));
        when(userService.loadUserByUsername(logInRequest.getEmail())).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("mockToken");

        ResponseEntity<LogInResponse> responseEntity = signInService.signIn(logInRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("LOGIN SUCCESSFUL!", responseEntity.getBody().getMessage());
        assertNotNull(responseEntity.getBody().getToken());
    }
}
