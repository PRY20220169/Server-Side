package com.tp.pry20220169.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.pry20220169.domain.model.Account;
import com.tp.pry20220169.domain.model.Role;
import com.tp.pry20220169.domain.model.User;
import com.tp.pry20220169.domain.service.AccountService;
import com.tp.pry20220169.domain.service.UserService;
import com.tp.pry20220169.resource.SaveAccountResource;
import com.tp.pry20220169.resource.security.AuthenticationRequest;
import com.tp.pry20220169.resource.security.AuthenticationResponse;
import com.tp.pry20220169.resource.security.RegisterRequest;
import com.tp.pry20220169.resource.security.RoleToUserForm;
import com.tp.pry20220169.util.JwtCenter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@Slf4j
public class SecurityController {

    private final JwtCenter jwtCenter;
    private final ModelMapper mapper;
    private final AccountService accountService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(Pageable pageable) {
        return ResponseEntity.ok().body(userService.getAllUsers(pageable));
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginForm, HttpServletRequest request) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        log.info("Username is: {}", username);
        log.info("Password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception exception) {
            log.error("Error logging in {}", exception.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error_message", exception.getMessage());
            return ResponseEntity.status(FORBIDDEN).body(error);
        }

        User user = userService.getUserByUsername(username);
        String access_token = jwtCenter.generateAccessToken(user, request.getContextPath());
        String refresh_token = jwtCenter.generateRefreshToken(user, request.getContextPath());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(access_token, refresh_token, user.getUsername(), "success", HttpStatus.OK, user.getId());
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> saveUser(@RequestBody @Valid RegisterRequest registerRequest, HttpServletRequest request) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users").toUriString());
        // could use a mapper
        User user = new User(registerRequest.getUsername(), registerRequest.getPassword());
        Long userId = userService.createUser(user).getId();
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        SaveAccountResource resource = new SaveAccountResource();
        resource.setFirstName(registerRequest.getFirstName());
        resource.setLastName(registerRequest.getLastName());

        accountService.createAccount(userId, convertToEntity(resource));
        AuthenticationRequest authRequest = new AuthenticationRequest(username, password);

        return login(authRequest, request);
    }

    @GetMapping("/roles")
    public ResponseEntity<Page<Role>> getRoles(Pageable pageable) {
        return ResponseEntity.ok().body(userService.getRoles(pageable));
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/roles/addToUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = jwtCenter.verifyAndDecodeToken(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUserByUsername(username);
                String access_token = jwtCenter.generateAccessToken(user, request.getRequestURL().toString());
                AuthenticationResponse authenticationResponse = new AuthenticationResponse(access_token, refresh_token, username, "success", HttpStatus.OK, user.getId());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);

            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
        return ResponseEntity.ok().build();
    }

    private Account convertToEntity(SaveAccountResource resource) { return mapper.map(resource, Account.class); }
}



