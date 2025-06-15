package com.example.BeautServices.services;

import com.example.BeautServices.dto.NotificationRequest;
import com.example.BeautServices.dto.NotificationResponse;
import com.example.BeautServices.dto.UserNotificationDTO;
import com.example.BeautServices.entity.Notification;

import java.util.List;

public interface NotificationService {
        NotificationResponse createNotification(NotificationRequest request);
        List<NotificationResponse> getAllNotifications();
        UserNotificationDTO markAsRead(Long id, String email);
        List<UserNotificationDTO> getMyNotifications(String email);
        void deleteNotification(Long id, String email);
        void deleteNotificationForUser(Long id, String email);
}