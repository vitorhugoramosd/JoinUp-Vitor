package com.ticketsystem.notifications.infrastructure.adapter.persistence;

import com.ticketsystem.notifications.domain.model.Notification;
import com.ticketsystem.notifications.domain.port.out.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaNotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmail(String email);
    List<Notification> findByStatus(Notification.NotificationStatus status);
}

@Component
@RequiredArgsConstructor
class NotificationRepositoryAdapter implements NotificationRepository {

    private final JpaNotificationRepository jpaNotificationRepository;

    @Override
    public Notification save(Notification notification) {
        return jpaNotificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return jpaNotificationRepository.findById(id);
    }

    @Override
    public List<Notification> findByRecipientEmail(String email) {
        return jpaNotificationRepository.findByRecipientEmail(email);
    }

    @Override
    public List<Notification> findByStatus(Notification.NotificationStatus status) {
        return jpaNotificationRepository.findByStatus(status);
    }
}

