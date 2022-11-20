package com.tp.pry20220169.resource.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 8, message = "password is too weak")
    //TODO: create contrains to validate at least one capital letter, one special character like "@" and at least one numbers
    private String password;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
