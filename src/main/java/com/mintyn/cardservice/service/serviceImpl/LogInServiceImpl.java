package com.mintyn.cardservice.service.serviceImpl;

import com.mintyn.cardservice.config.JwtUtils;
import com.mintyn.cardservice.entity.User;
import com.mintyn.cardservice.repository.UserRepository;
import com.mintyn.cardservice.request.LogInRequest;
import com.mintyn.cardservice.response.LogInResponse;
import com.mintyn.cardservice.service.LogInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogInServiceImpl implements LogInService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<LogInResponse> signIn(LogInRequest logInRequest) {

        //checks if user exists
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(logInRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND!")));

        try{
            authenticationManager.authenticate( new UsernamePasswordAuthenticationToken
                    (logInRequest.getEmail(), logInRequest.getPassword())
            );
        } catch (BadCredentialsException ex){
            throw new UsernameNotFoundException("Invalid Credential");
        }
        final UserDetails userDetails = userService.loadUserByUsername(logInRequest.getEmail());
        String jwt = jwtUtils.generateToken(userDetails);

        LogInResponse logInResponse = LogInResponse
                .builder()
                .email(logInRequest.getEmail())
                .message("LOGIN SUCCESSFUL!")
                .token(jwt)
                .build();

        return ResponseEntity.ok().body(logInResponse);
    }
}
