package com.tp.pry20220169.resource.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AuthenticationRequest {
    private String username;
    private String password;
}
