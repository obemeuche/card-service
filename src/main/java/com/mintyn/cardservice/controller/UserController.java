package com.mintyn.cardservice.controller;

import com.mintyn.cardservice.request.LogInRequest;
import com.mintyn.cardservice.request.SignUpRequest;
import com.mintyn.cardservice.service.LogInService;
import com.mintyn.cardservice.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserController {

    private final SignUpService signUpService;

    private final LogInService logInService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequest signUpRequest) {
        return signUpService.signUp(signUpRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LogInRequest logInRequest) {
        return logInService.signIn(logInRequest);
    }

}
