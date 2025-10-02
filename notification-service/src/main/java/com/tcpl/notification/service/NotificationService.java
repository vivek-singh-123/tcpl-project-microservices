package com.tcpl.notification.service;

import com.tcpl.notification.dto.NotificationRequest;
import com.tcpl.notification.dto.NotificationResponse;
import com.tcpl.notification.model.Notification;
import com.tcpl.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    // Helper method to convert Entity to DTO
    private NotificationResponse mapToResponse(Notification entity) {
        NotificationResponse dto = new NotificationResponse();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setMessage(entity.getMessage());
        dto.setReadStatus(entity.isReadStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // Create notification (Updated to use DTOs)
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        // readStatus aur createdAt default values le lenge

        Notification savedEntity = repository.save(notification);
        return mapToResponse(savedEntity);
    }

    // Get all notifications (Updated to return DTOs)
    public List<NotificationResponse> getAllNotifications() {
        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get notification by ID (Updated to return DTO)
    public Optional<NotificationResponse> getNotificationById(Long id) {
        return repository.findById(id).map(this::mapToResponse);
    }

    // Update notification read status (Updated to return DTO)
    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        Notification updatedEntity = repository.save(notification);
        return mapToResponse(updatedEntity);
    }

    // Delete notification (No change needed in logic)
    public void deleteNotification(Long id) {
        repository.deleteById(id);
    }

    // Get unread notifications (Updated to return DTOs)
    public List<NotificationResponse> getUnreadNotifications() {
        return repository.findByReadStatus(false)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}