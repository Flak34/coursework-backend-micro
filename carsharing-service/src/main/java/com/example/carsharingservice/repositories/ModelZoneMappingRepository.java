package com.example.carsharingservice.repositories;

import ch.qos.logback.core.model.Model;
import com.example.carsharingservice.entities.EModel;
import com.example.carsharingservice.entities.ModelZoneMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelZoneMappingRepository extends JpaRepository<ModelZoneMapping, Model> {
    List<ModelZoneMapping> findAllByZoneId(int zoneId);
    ModelZoneMapping findByModel(EModel model);
}
