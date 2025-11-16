package com.ticketsystem.notifications.infrastructure.adapter.persistence;

import com.ticketsystem.notifications.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmail(String email);
    List<Notification> findByStatus(Notification.NotificationStatus status);
}

