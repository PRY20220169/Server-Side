package com.tp.pry20220169;

import com.tp.pry20220169.domain.model.Role;
import com.tp.pry20220169.domain.model.User;
import com.tp.pry20220169.domain.service.UserService;
import com.tp.pry20220169.util.JwtCenter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableJpaAuditing
public class Pry20220169Application {

    public static void main(String[] args) {
        SpringApplication.run(Pry20220169Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public JwtCenter getJwtCenter() {
        return new JwtCenter();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            if (userService.getAllUsers(Pageable.ofSize(10)).isEmpty()) {
                userService.saveRole(new Role(null, "ROLE_USER"));
                userService.saveRole(new Role(null, "ROLE_MANAGER"));
                userService.saveRole(new Role(null, "ROLE_ADMIN"));
                userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));
                userService.saveRole(new Role(null, "ROLE_RPA"));
                userService.createUser(new User("diego", "1234"));
                userService.createUser(new User("ashlyn", "1234"));
                userService.createUser(new User("anibal", "1234"));
                userService.createUser(new User("jim", "1234"));

                userService.addRoleToUser("diego", "ROLE_USER");
                userService.addRoleToUser("diego", "ROLE_MANAGER");
                userService.addRoleToUser("diego", "ROLE_ADMIN");
                userService.addRoleToUser("diego", "ROLE_SUPER_ADMIN");
                userService.addRoleToUser("ashlyn", "ROLE_USER");
                userService.addRoleToUser("ashlyn", "ROLE_SUPER_ADMIN");
                userService.addRoleToUser("anibal", "ROLE_USER");
                userService.addRoleToUser("anibal", Role.ROLE_RPA);
                userService.addRoleToUser("jim", "ROLE_USER");
            }
        };
    }
}
