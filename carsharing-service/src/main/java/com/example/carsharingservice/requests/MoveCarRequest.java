package com.example.carsharingservice.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveCarRequest {
    private float newLng;
    private float newLat;
    private Long carId;
}
