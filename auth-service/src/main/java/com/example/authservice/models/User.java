package com.example.authservice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "users",
uniqueConstraints = {
        @UniqueConstraint(columnNames = "login"),
        @UniqueConstraint(columnNames = "email")
})
@Setter
@Getter
public class User {
    @Id
    @NotBlank
    @Size(max = 20)
    private String login;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(max = 120)
    private String password;
    public User(String username, String email, String password) {
        this.login = username;
        this.email = email;
        this.password = password;
    }
    public User() {

    }

}
