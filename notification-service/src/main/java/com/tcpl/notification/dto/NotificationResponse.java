package com.tcpl.notification.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private boolean readStatus;
    private LocalDateTime createdAt;
}