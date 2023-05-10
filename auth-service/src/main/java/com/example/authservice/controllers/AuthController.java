package com.example.authservice.controllers;

import com.example.authservice.payload.request.LoginRequest;
import com.example.authservice.payload.request.SignupRequest;
import com.example.authservice.payload.response.JwtResponse;
import com.example.authservice.payload.response.MessageResponse;
import com.example.authservice.services.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest authRequest) {
        try {
            final JwtResponse jwtResponse = authService.login(authRequest);
            return ResponseEntity.ok(jwtResponse);
        }
        catch (AuthException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Неправильное имя пользователя или пароль!"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        return authService.validateAccessToken(token);
    }

}