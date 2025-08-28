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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas de autenticación
                        .requestMatchers("/auth/**").permitAll()

                        // Pacientes
                        .requestMatchers(HttpMethod.POST, "/pacientes/**").permitAll() // Registro paciente
                        .requestMatchers(HttpMethod.GET, "/pacientes/**").hasAnyRole("PACIENTE", "ADMIN", "ODONTOLOGO")
                        .requestMatchers(HttpMethod.PATCH, "/pacientes/**").hasAnyRole("PACIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pacientes/**").hasRole("ADMIN")

                        // Odontólogos
                        .requestMatchers(HttpMethod.POST, "/odontologos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/odontologos/**").hasAnyRole("ODONTOLOGO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/odontologos/**").hasAnyRole("ODONTOLOGO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/odontologos/**").hasRole("ADMIN")

                        // Turnos
                        .requestMatchers(HttpMethod.POST, "/turnos/**").hasAnyRole("PACIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/turnos/**").hasAnyRole("PACIENTE", "ODONTOLOGO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/turnos/**").hasAnyRole("ODONTOLOGO", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/turnos/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
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

