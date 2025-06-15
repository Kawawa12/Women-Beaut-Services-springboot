package com.example.BeautServices.services;

import com.example.BeautServices.dto.NotificationRequest;
import com.example.BeautServices.dto.NotificationResponse;
import com.example.BeautServices.dto.UserNotificationDTO;
import com.example.BeautServices.entity.Client;
import com.example.BeautServices.entity.Notification;
import com.example.BeautServices.entity.RecipientType;
import com.example.BeautServices.entity.Role;
import com.example.BeautServices.repository.ClientRepository;
import com.example.BeautServices.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   ClientRepository clientRepository) {
        this.notificationRepository = notificationRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setRecipients(request.getRecipients());
        notification.setSentAt(LocalDateTime.now());

        List<Client> targetUsers = getTargetUsers(request.getRecipients());
        notification.setRecipientEmails(getUserEmails(targetUsers));

        Notification saved = notificationRepository.save(notification);
        return convertToResponse(saved);
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserNotificationDTO> getMyNotifications(String email) {
        return notificationRepository.findByRecipientEmailsContains(email).stream()
                .map(notification -> convertToUserDTO(notification, email))
                .collect(Collectors.toList());
    }

    @Override
    public UserNotificationDTO markAsRead(Long id, String email) {
        Notification notification = getNotificationById(id);
        validateUserIsRecipient(notification, email);

        if (!notification.getReadBy().contains(email)) {
            notification.getReadBy().add(email);
            notificationRepository.save(notification);
        }

        return convertToUserDTO(notification, email);
    }

    @Override
    public void deleteNotification(Long id, String email) {
        deleteNotificationForUser(id, email);
    }

    @Override
    public void deleteNotificationForUser(Long id, String email) {
        Notification notification = getNotificationById(id);
        validateUserIsRecipient(notification, email);

        notification.getRecipientEmails().remove(email);

        if (notification.getRecipientEmails().isEmpty()) {
            notificationRepository.delete(notification);
        } else {
            notificationRepository.save(notification);
        }
    }

    // Helper Methods
    private List<Client> getTargetUsers(RecipientType recipientType) {
        return switch (recipientType) {
            case RECEPTIONISTS -> clientRepository.findByRole(Role.RECEPTIONIST);
            case CLIENTS -> clientRepository.findByRole(Role.CUSTOMER);
            case ALL -> clientRepository.findAll();
        };
    }

    private List<String> getUserEmails(List<Client> users) {
        return users.stream()
                .map(Client::getEmail)
                .collect(Collectors.toList());
    }

    private Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    private void validateUserIsRecipient(Notification notification, String email) {
        boolean isRecipient = notification.getRecipientEmails().stream()
                .anyMatch(e -> e.trim().equalsIgnoreCase(email.trim()));

        if (!isRecipient) {
            throw new RuntimeException("User is not a recipient of this notification");
        }
    }


    private NotificationResponse convertToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .recipients(notification.getRecipients())
                .sentAt(notification.getSentAt())
                .recipientEmails(notification.getRecipientEmails())
                .totalRecipients(notification.getRecipientEmails().size())
                .readCount(notification.getReadBy().size())
                .build();
    }

    private UserNotificationDTO convertToUserDTO(Notification notification, String email) {
        return UserNotificationDTO.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .sentAt(notification.getSentAt())
                .read(notification.getReadBy().contains(email))
                .build();
    }
}
