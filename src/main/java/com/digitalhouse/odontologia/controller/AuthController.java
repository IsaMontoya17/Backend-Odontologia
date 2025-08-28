package com.digitalhouse.odontologia.controller;

import com.digitalhouse.odontologia.dto.AuthRequest;
import com.digitalhouse.odontologia.service.impl.JwtService;
import com.digitalhouse.odontologia.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails user = userDetailService.loadUserByUsername(request.getEmail());
            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error de autenticación: " + e.getMessage());
        }
    }


}


