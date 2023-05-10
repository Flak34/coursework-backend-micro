package com.example.carsharingservice.controllers;

import com.example.carsharingservice.entities.Car;
import com.example.carsharingservice.entities.Drive;
import com.example.carsharingservice.entities.Zone;
import com.example.carsharingservice.requests.FinishDriveRequest;
import com.example.carsharingservice.requests.MoveCarRequest;
import com.example.carsharingservice.requests.StartDriveRequest;
import com.example.carsharingservice.services.CarsharingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carsharing")
public class CarsharingController {

    @Autowired
    private CarsharingService carsharingService;

    @GetMapping("/cars")
    public List<Car> getFreeCars() {
        return carsharingService.getFreeCars();
    }

    @PostMapping("cars/move")
    public ResponseEntity<?> moveCar(@RequestBody MoveCarRequest request) {
        return carsharingService.moveCar(request.getNewLng(), request.getNewLat(), request.getCarId());
    }

    @PostMapping("/drives/start")
    public ResponseEntity<?> createDrive(@RequestHeader("login") String userLogin, @RequestBody StartDriveRequest request) {
        return carsharingService.createDrive(userLogin, request.getCarId());
    }

    @PostMapping("/drives/finish")
    public ResponseEntity<?> finishDrive(@RequestBody FinishDriveRequest request) {
        return carsharingService.finishDrive(request.getDriveId());
    }

    @GetMapping("/drives")
    public List<Drive> getUserDrives(@RequestHeader("login") String userLogin) {
        return carsharingService.getUserDrives(userLogin);
    }

    @GetMapping("/drive")
    public ResponseEntity<?> getCurrentDrive(@RequestHeader("login") String userLogin) {
        return carsharingService.getCurrentDrive(userLogin);
    }

    @GetMapping("/zones/zone")
    public Zone getZone(@RequestParam Integer zoneId) {
        return carsharingService.getZone(zoneId);
    }

    @GetMapping("/zones/cars")
    public List<Car> getCarsByZone(@RequestParam Integer zoneId) {
        return carsharingService.getCarsByZone(zoneId);
    }

}
