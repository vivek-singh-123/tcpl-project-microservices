package com.tcpl.notification.repository;

import com.tcpl.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReadStatus(boolean readStatus);
}
