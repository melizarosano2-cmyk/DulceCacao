package com.choco.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeRequests((requests) -> requests
                                                .antMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**",
                                                                "/images/**", "/product/**", "/cart/**",
                                                                "/verify-email**", "/verification-sent",
                                                                "/forgot-password**", "/reset-password**",
                                                                "/reset-sent",
                                                                "/access-denied",
                                                                "/api/v1/products/**", // Public API
                                                                "/swagger-ui/**", "/v3/api-docs/**") // Swagger Docs
                                                .permitAll()
                                                .antMatchers("/api/v1/analytics/**").hasAnyRole("ADMIN", "EMPLOYEE") // Protected
                                                                                                                     // API
                                                .antMatchers("/admin/**").hasRole("ADMIN")
                                                .antMatchers("/employee/**").hasAnyRole("EMPLOYEE", "ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin((form) -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout((logout) -> logout.permitAll())
                                .exceptionHandling((exception) -> exception
                                                .accessDeniedPage("/access-denied"));


                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
