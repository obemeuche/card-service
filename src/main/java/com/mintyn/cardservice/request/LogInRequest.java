package com.mintyn.cardservice.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogInRequest {

    @NotEmpty(message = "Invalid password")
    @Size(min = 8, max = 13, message = "Invalid password")
    @Pattern(regexp = "^[0-9A-Za-z \\\\\\\\s.,-]+$", message = "Invalid password")
    private String password;

    @Email(message = "Invalid email address")
    private String email;
}
