package com.fourback.bemajor.global.config;


import com.fourback.bemajor.global.common.service.RedisService;
import com.fourback.bemajor.global.security.custom.CustomAuthenticationEntryPoint;
import com.fourback.bemajor.global.security.custom.CustomLogoutFilter;
import com.fourback.bemajor.global.security.jwt.JWTExceptionFilter;
import com.fourback.bemajor.global.security.jwt.JWTFilter;
import com.fourback.bemajor.global.security.jwt.JWTUtil;
import com.fourback.bemajor.global.security.jwt.ReissueTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer
                        -> corsCustomizer.configurationSource(this.corsCustomizer()));
        http
                .csrf(AbstractHttpConfigurer::disable);
        http
                .formLogin(AbstractHttpConfigurer::disable);
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .anyRequest().authenticated());
        http
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));
        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(redisService, jwtUtil), LogoutFilter.class)
                .addFilterBefore(new JWTExceptionFilter(), JWTFilter.class);
        http
                .addFilterAfter(new ReissueTokenFilter(jwtUtil), JWTFilter.class);
        return http.build();
    }

    private CorsConfigurationSource corsCustomizer() {
        return request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setMaxAge(3600L);
            configuration.setExposedHeaders(List.of("access", "refresh"));
            return configuration;
        };
    }
}
