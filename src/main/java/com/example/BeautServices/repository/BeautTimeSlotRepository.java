package com.example.BeautServices.repository;

import com.example.BeautServices.entity.BeautTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;

public interface BeautTimeSlotRepository extends JpaRepository<BeautTimeSlot, Long> {
    boolean existsByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);

}
