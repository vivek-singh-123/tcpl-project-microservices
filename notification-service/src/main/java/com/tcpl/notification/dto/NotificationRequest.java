package com.tcpl.notification.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String title;
    private String message;
    // Note: readStatus, createdAt, id jaisi cheezein client nahi bhejega,
    // woh server automatically handle karega.
}