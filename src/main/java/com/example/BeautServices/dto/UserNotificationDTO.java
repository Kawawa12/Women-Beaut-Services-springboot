package com.example.BeautServices.dto;

import com.example.BeautServices.entity.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserNotificationDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private LocalDateTime sentAt;
    private boolean read;
}
