package com.example.carsharingservice.repositories;


import com.example.carsharingservice.entities.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public interface DriveRepository extends JpaRepository<Drive, Long> {
    @Modifying
    @Query("update Drive drive set drive.finish = ?2, drive.duration = ?3 where drive.id = ?1")
    int finishDrive(Long id, LocalDateTime finish, Period duration);
    List<Drive> findAllByLogin(String user_login);
    Drive findByLoginAndFinishIsNull(String user_login);
}
