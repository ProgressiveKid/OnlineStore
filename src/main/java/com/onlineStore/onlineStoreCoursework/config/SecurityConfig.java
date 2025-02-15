package com.onlineStore.onlineStoreCoursework.config;

import com.onlineStore.onlineStoreCoursework.security.CustomAuthenticationSuccessHandler;
import com.onlineStore.onlineStoreCoursework.security.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    public SecurityConfig(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/uploads/**") // Отключаем CSRF для H2 Console
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/auth/login", "/auth/register", "/uploads/**", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // Страница логина
                        .loginProcessingUrl("/auth/login")
                        .successHandler(new CustomAuthenticationSuccessHandler()) // Кастомная логика успешного логина
                        .failureUrl("/auth/login?error=true")
                )

                .headers(headers -> headers
                        .frameOptions().sameOrigin() // Разрешаем использование iframe с той же страницы
                ).logout()
                .logoutUrl("/auth/logout")
                .permitAll()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");;
        http.csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
