package com.mintyn.cardservice;


import com.mintyn.cardservice.repository.UserRepository;
import com.mintyn.cardservice.request.SignUpRequest;
import com.mintyn.cardservice.response.SignUpResponse;
import com.mintyn.cardservice.service.serviceImpl.SignUpServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SignUpServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private SignUpServiceImpl signUpService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void signUp_SuccessfulSignUp_ReturnsResponseEntity() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setConfirmPassword("password");

        when(userRepository.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(signUpRequest.getPassword())).thenReturn("hashedPassword");

        ResponseEntity<SignUpResponse> responseEntity = signUpService.signUp(signUpRequest);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("USER CREATED SUCCESSFULLY!", responseEntity.getBody().getMessage());
    }
}
