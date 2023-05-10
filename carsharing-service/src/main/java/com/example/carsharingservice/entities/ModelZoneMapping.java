package com.example.carsharingservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="model_zone_mapping",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "model")
})

public class ModelZoneMapping {
    @Id
    @Enumerated(EnumType.STRING)
    private EModel model;
    private int zoneId;
}
