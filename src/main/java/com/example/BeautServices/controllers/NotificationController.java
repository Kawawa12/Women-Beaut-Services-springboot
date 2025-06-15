package com.example.BeautServices.controllers;

import com.example.BeautServices.dto.NotificationRequest;
import com.example.BeautServices.dto.NotificationResponse;
import com.example.BeautServices.dto.UserNotificationDTO;
import com.example.BeautServices.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.createNotification(request));
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/my")
    public ResponseEntity<List<UserNotificationDTO>> getMyNotifications(Principal principal) {
        return ResponseEntity.ok(notificationService.getMyNotifications(principal.getName()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<UserNotificationDTO> markAsRead(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(notificationService.markAsRead(id, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id, Principal principal) {
        notificationService.deleteNotification(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
