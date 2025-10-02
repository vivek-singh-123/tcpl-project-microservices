package com.tcpl.notification.controller;

import com.tcpl.notification.dto.NotificationRequest;
import com.tcpl.notification.dto.NotificationResponse;
import com.tcpl.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    // Create notification (Updated)
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        NotificationResponse saved = service.createNotification(request);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get all notifications (Updated)
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    // Get single notification (Updated)
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotification(@PathVariable Long id) {
        return service.getNotificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Mark notification as read (Updated)
    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        NotificationResponse updated = service.markAsRead(id);
        return ResponseEntity.ok(updated);
    }

    // Delete notification (No change)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        service.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    // Get unread notifications (Updated)
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications() {
        return ResponseEntity.ok(service.getUnreadNotifications());
    }
}