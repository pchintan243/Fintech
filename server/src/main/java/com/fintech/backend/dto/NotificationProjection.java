package com.fintech.backend.dto;

import java.time.LocalDateTime;

public interface NotificationProjection {
    Long getId();
    Long getUserId();
    String getType();
    String getTitle();
    String getMessage();
    Boolean getIsRead();
    LocalDateTime getCreatedAt();
}
