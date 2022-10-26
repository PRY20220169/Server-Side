package com.tp.pry20220169.domain.service;

import com.tp.pry20220169.domain.model.Role;
import com.tp.pry20220169.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    Page<User> getAllUsers(Pageable pageable);

    User getUserById(Long userId);

    User getUserByUsername(String username);

    User createUser(User user);

    User updateUser(Long userId, User userDetails);

    ResponseEntity<?> deleteUser(Long userId);

    Page<Role> getRoles(Pageable pageable);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);
}
