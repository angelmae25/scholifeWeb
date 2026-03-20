package com.scholife1.config;

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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public routes — no login needed
                        .requestMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")      // Spring Security handles POST here
                        .defaultSuccessUrl("/dashboard/index", true)
                        .failureUrl("/auth/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")              // Spring Security handles POST here
                        .logoutSuccessUrl("/auth/login?logout") // redirect after logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}