package com.tundalabs.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email cannot be Blank")
    @Email(message = "Provide a valid Email")
    private String email;

    @NotBlank(message = "Password is Required")
    private String password;

}
