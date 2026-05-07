package com.fintech.backend.repository;

import com.fintech.backend.dto.NotificationProjection;
import com.fintech.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
//    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Projection queries
    List<NotificationProjection> findProjectedByUserIdOrderByCreatedAtDesc(Long userId);
}
