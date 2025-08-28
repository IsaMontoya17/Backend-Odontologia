package com.digitalhouse.odontologia.config;

import com.digitalhouse.odontologia.service.impl.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, UserDetailServiceImpl userDetailService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pacientes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/pacientes/**").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/pacientes/**").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/pacientes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/odontologos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/odontologos/**").hasAnyRole("ODONTOLOGO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/odontologos/**").hasRole("ODONTOLOGO")
                        .requestMatchers(HttpMethod.DELETE, "/odontologos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/turnos/**").hasRole("PACIENTE")
                        .requestMatchers(HttpMethod.GET, "/turnos/**").hasAnyRole("ODONTOLOGO", "ADMIN", "PACIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/turnos/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider(userDetailService)) // aquí cambias null por userDetailService
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

