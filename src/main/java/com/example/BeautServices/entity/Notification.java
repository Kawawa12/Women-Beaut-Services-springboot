package com.example.BeautServices.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    private RecipientType recipients;

    private LocalDateTime sentAt = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "notification_read_status", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "email")
    private List<String> readBy = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "notification_recipients", joinColumns = @JoinColumn(name = "notification_id"))
    @Column(name = "email")
    private List<String> recipientEmails = new ArrayList<>();
}