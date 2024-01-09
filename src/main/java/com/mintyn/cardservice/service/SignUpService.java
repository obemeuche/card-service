package com.mintyn.cardservice.service;


import com.mintyn.cardservice.request.SignUpRequest;
import com.mintyn.cardservice.response.SignUpResponse;
import org.springframework.http.ResponseEntity;

public interface SignUpService {
    ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest);

}
