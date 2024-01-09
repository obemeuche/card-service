package com.mintyn.cardservice.service.serviceImpl;

import com.mintyn.cardservice.entity.User;
import com.mintyn.cardservice.exceptions.DatabaseException;
import com.mintyn.cardservice.exceptions.EmailAlreadyExists;
import com.mintyn.cardservice.exceptions.PasswordNotMatchingException;
import com.mintyn.cardservice.repository.UserRepository;
import com.mintyn.cardservice.request.SignUpRequest;
import com.mintyn.cardservice.response.SignUpResponse;
import com.mintyn.cardservice.service.SignUpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SignUpServiceImpl implements SignUpService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<SignUpResponse> signUp(SignUpRequest signUpRequest) {

        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            throw new PasswordNotMatchingException("PASSWORDS DOES NOT MATCH!");
        }

        boolean emailExists;

        try {
            emailExists = userRepository.findByEmail(signUpRequest.getEmail()).isPresent();
        }catch (Exception e){
            log.info("UNABLE TO CONNECT TO THE DATABASE. REASON: " + e);
            throw new DatabaseException("UNABLE TO CONNECT TO THE DATABASE");
        }

        if (emailExists) {
            throw new EmailAlreadyExists("EMAIL ALREADY EXISTS!");
        }

        User user = User
                .builder()
                .email(signUpRequest.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .build();

        try {
            userRepository.save(user);
        }catch (Exception e){
            log.info("UNABLE TO SAVE USER DETAILS INTO THE DATABASE. REASON: " + e);
            throw new DatabaseException("UNABLE TO SAVE USER DETAILS INTO THE DATABASE");
        }

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .email(signUpRequest.getEmail())
                .message("USER CREATED SUCCESSFULLY!")
                .build();

        return  ResponseEntity.ok().body(signUpResponse);
    }

}
