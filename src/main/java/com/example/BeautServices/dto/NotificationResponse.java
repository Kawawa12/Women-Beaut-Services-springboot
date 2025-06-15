package com.example.BeautServices.dto;

import com.example.BeautServices.entity.NotificationType;
import com.example.BeautServices.entity.RecipientType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private RecipientType recipients;
    private LocalDateTime sentAt;
    private List<String> recipientEmails;
    private int totalRecipients;
    private int readCount;
}
