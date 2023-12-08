package com.example.authservice.services;


import com.example.authservice.jwt.JwtProvider;
import com.example.authservice.models.User;
import com.example.authservice.payload.request.LoginRequest;
import com.example.authservice.payload.request.SignupRequest;
import com.example.authservice.payload.response.JwtResponse;
import com.example.authservice.payload.response.MessageResponse;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;


    public JwtResponse login(@NonNull LoginRequest authRequest) throws AuthException {
        final User user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (authRequest.getPassword().equals(user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            return new JwtResponse(accessToken, authRequest.getLogin());
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getLogin(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword());
        userService.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public String validateAccessToken(String token) {
        if(jwtProvider.validateAccessToken(token)) {
            return jwtProvider.getAccessClaims(token).get("login").toString();
        }
        else {
            return "";
        }
    }

}