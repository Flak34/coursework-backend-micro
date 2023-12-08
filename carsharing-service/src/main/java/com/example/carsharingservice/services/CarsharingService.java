package com.example.carsharingservice.services;


import com.example.carsharingservice.entities.Car;
import com.example.carsharingservice.entities.Drive;
import com.example.carsharingservice.entities.ModelZoneMapping;
import com.example.carsharingservice.entities.Zone;
import com.example.carsharingservice.repositories.CarRepository;
import com.example.carsharingservice.repositories.DriveRepository;
import com.example.carsharingservice.repositories.ModelZoneMappingRepository;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CarsharingService {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private ModelZoneMappingRepository modelZoneMappingRepository;

    public List<Car> getFreeCars() {
        return carRepository.findCarsByTaken(false);
    }

    public ResponseEntity<?> createDrive(String user_login, Long car_id) {
        Drive newDrive = new Drive();

        if (carRepository.takeCar(car_id) != 0)
            newDrive.setCar(carRepository.findById(car_id).get());
        else return ResponseEntity.badRequest().body("Машина уже занята!");

        newDrive.setLogin(user_login);
        newDrive.setStart(LocalDateTime.now());
        driveRepository.saveAndFlush(newDrive);

        return ResponseEntity.ok(newDrive);
    }

    public ResponseEntity<?> finishDrive(Long id) {
        Drive currentDrive = driveRepository.findById(id).get();
        currentDrive.setFinish(LocalDateTime.now());
        currentDrive.setDuration(Period.between(currentDrive.getStart().toLocalDate(),
                currentDrive.getFinish().toLocalDate()));

        //проверка на то, в зоне ли машина
        Car currentCar = currentDrive.getCar();
        int zoneId = modelZoneMappingRepository.findByModel(currentCar.getModel()).getZoneId();
        Zone zone = getZone(zoneId);

        if(checkZone(zone, S2LatLng.fromDegrees(currentCar.getLng(), currentCar.getLat()).toPoint())) {
            if (driveRepository.finishDrive(id, currentDrive.getFinish(), currentDrive.getDuration()) > 0) {
                carRepository.releaseCar(currentCar.getId());
                return ResponseEntity.ok(currentDrive);
            }
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка во время завершения поездки");
        }
        else {
            return ResponseEntity.badRequest().body("Нельзя завершить поездку за пределами зоны покрытия!");
        }
    }

    public List<Drive> getUserDrives(String login) {
        return driveRepository.findAllByLogin(login);
    }

    public ResponseEntity<?> getCurrentDrive(String login) {
        return ResponseEntity.ok(driveRepository.findByLoginAndFinishIsNull(login));
    }

    public ResponseEntity<?> moveCar(float newLng, float newLat, Long carId) {
        if(true) {
            carRepository.moveCar(newLng, newLat, carId);
            return ResponseEntity.ok(carRepository.findById(carId));
        }
        else {
            return ResponseEntity.badRequest().body("Нельзя выезжать за зону поездки!");
        }
    }

    public Zone getZone(int zoneId) {
        switch (zoneId) {
            case 1: {
                return new Zone(new double[][]{{37.372261377080605, 55.738131584335406},
                        {37.39347520874125, 55.83139155696324}, {37.47254494493106, 55.884428266871026},
                        {37.58536401922538, 55.90929795375135}, {37.708789948888096, 55.88983597131491},
                        {37.798466600907744, 55.84709350112843}, {37.83414440870146, 55.824892155045916},
                        {37.84282279438065, 55.77177414911617}, {37.83800146900265, 55.70989261126431},
                        {37.83703720392782, 55.66423287712652}, {37.79755762415414, 55.624128416183396},
                        {37.726230843053344, 55.58999381476289}, {37.62023132113944, 55.57207525675071},
                        {37.52017569765056, 55.59279285072438}, {37.46370866261216, 55.634754438177396},
                        {37.413185525998784, 55.69063339336094}, {37.38346603387288, 55.714636822063426},
                        {37.372261377080605, 55.738131584335406}});
            }
            case 2: {
                return new Zone(new double[][]{{37.486562426378214, 55.751015189579164},
                        {37.54772089382925, 55.664009377363215}, {37.640987556689396, 55.66918345315051},
                        {37.6425165183762, 55.70236749728133}, {37.7151421984739, 55.69978273917113},
                        {37.74342798966856, 55.754026770047744}, {37.647867884277446, 55.80046169396434},
                        {37.52784439190785, 55.79702396718295}, {37.486562426378214, 55.751015189579164}});
            }
            case 3: {
                return new Zone(new double[][]{{37.584059199590484, 55.7564127361332},
                        {37.58267160598447, 55.747041816894665}, {37.58648748839917, 55.738840415815986},
                        {37.60417930687106, 55.73200459810877}, {37.62464631255213, 55.72946527518485},
                        {37.64441952143085, 55.73454375586505}, {37.65621406707831, 55.74430820805992},
                        {37.65760166068429, 55.75582711963088}, {37.65586716867642, 55.764415280447196},
                        {37.63713465500183, 55.77163567907931}, {37.611811071700515, 55.77339179005986},
                        {37.59307855802592, 55.768708651622234}, {37.584059199590484, 55.7564127361332}});
            }
            default:
                return new Zone(new double[][]{{}});
        }
    }

    public List<Car> getCarsByZone(int zoneId) {
        List<ModelZoneMapping> mappings = modelZoneMappingRepository.findAllByZoneId(zoneId);
        List<Car> result = new ArrayList<>();

        for(ModelZoneMapping mapping: mappings) {
            result.addAll(carRepository.findAllByModel(mapping.getModel()));
        }
        return result;
    }

    public boolean checkZone(Zone zone, S2Point point) {
        List<S2Point> vertices = new ArrayList<>();
        for(int i = 0; i < zone.getCoordinates().length - 1; i++) {
            double lat = zone.getCoordinates()[i][0];
            double lng = zone.getCoordinates()[i][1];
            vertices.add(S2LatLng.fromDegrees(lat, lng).toPoint());
        }

        S2Loop loop = new S2Loop(vertices);
        return loop.contains(point);
    }




}
