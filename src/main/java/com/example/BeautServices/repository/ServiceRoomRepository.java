package com.example.BeautServices.repository;

import com.example.BeautServices.entity.ServiceRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ServiceRoomRepository extends JpaRepository<ServiceRoom, Long> {

    @Query("SELECT r FROM ServiceRoom r WHERE r.currentClientInService < r.capacity")
    List<ServiceRoom> findAvailableRooms();

    boolean existsByRoomNo(int roomNo);
}
