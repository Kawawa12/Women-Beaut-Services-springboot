package com.example.BeautServices.dto;

import com.example.BeautServices.entity.NotificationType;
import com.example.BeautServices.entity.RecipientType;
import lombok.Data;

@Data
public class NotificationRequest {
    private String title;
    private String message;
    private NotificationType type;
    private RecipientType recipients;
}
