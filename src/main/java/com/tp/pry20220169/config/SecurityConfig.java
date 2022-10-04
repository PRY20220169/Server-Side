package com.tp.pry20220169.config;

import com.tp.pry20220169.domain.model.Role;
import com.tp.pry20220169.filter.CustomAuthorizationFilter;
import com.tp.pry20220169.util.JwtCenter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static javax.swing.text.html.FormSubmitEvent.MethodType.POST;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtCenter jwtCenter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO: Add endpoints that required auth
        http.cors();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/security/users/login/**", "/security/token/refresh", "/security/users/register", "/swagger.html/**", "/swagger.json/**", "/swagger-ui/**").permitAll();//not auth needed
        //Add paths that require auth (token) and role rpa
        http.authorizeRequests().antMatchers(GET, "/security/roles/**").hasAnyAuthority(Role.ROLE_RPA);
        http.authorizeRequests().antMatchers("/api/collections/**, /api/users/**/account/collections/**").hasAnyAuthority(Role.ROLE_USER);
        // Add paths that require auth but no role
        // http.authorizeRequests().antMatchers(GET, "/security/roles/**").authenticated();
        http.authorizeRequests().anyRequest().permitAll();//.authenticated() to close the rest of endpoints
        http.addFilterBefore(new CustomAuthorizationFilter(jwtCenter), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
