package com.mintyn.cardservice.service;


import com.mintyn.cardservice.request.LogInRequest;
import com.mintyn.cardservice.response.LogInResponse;
import org.springframework.http.ResponseEntity;

public interface LogInService {
    ResponseEntity<LogInResponse> signIn(LogInRequest logInRequest);
}
