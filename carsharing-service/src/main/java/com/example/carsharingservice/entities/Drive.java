package com.example.carsharingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.time.Period;


@Entity
@Getter
@Setter
@Table(name="drives")
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    @OneToOne
    @JoinColumn(name="car_id")
    private Car car;
    private LocalDateTime start;
    private LocalDateTime finish;
    private Period duration;
    private double distance;
}
