package com.example.BeautServices.repository;

import com.example.BeautServices.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ✅ This is the correct and only needed query
    @Query("SELECT n FROM Notification n WHERE :email MEMBER OF n.recipientEmails")
    List<Notification> findByRecipientEmailsContains(@Param("email") String email);
}
