package com.mintyn.cardservice.response;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class LogInResponse {

    String email;
    String message;
    String token;
}
