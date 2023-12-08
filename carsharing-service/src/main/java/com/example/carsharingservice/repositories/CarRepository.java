package com.example.carsharingservice.repositories;

import com.example.carsharingservice.entities.Car;

import com.example.carsharingservice.entities.EModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findCarsByTaken(Boolean taken);

    @Modifying
    @Query("update Car car set car.taken = true where car.id = ?1 and car.taken != true")
    int takeCar(Long id);


    @Modifying
    @Query("update Car car set car.taken = false where car.id = ?1")
    int releaseCar(Long id);

    @Modifying
    @Query("update Car car set car.lng = ?1, car.lat = ?2 where car.id = ?3")
    int moveCar(float newLng, float newLat, Long id);


    List<Car> findAllByModel(EModel model);
}
