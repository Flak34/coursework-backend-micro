package com.example.authservice.payload.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    private String login;
    private final String type = "Bearer";
}
