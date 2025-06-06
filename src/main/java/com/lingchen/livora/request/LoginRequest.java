package com.lingchen.livora.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Invalid login credentials")
    private String email;

    @NotBlank(message = "Invalid login credentials")
    private String password;
}
