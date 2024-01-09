package com.mintyn.cardservice.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SignUpResponse {

    private String username;
    private String email;
    private String message;
}
