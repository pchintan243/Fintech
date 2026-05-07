package com.fintech.backend.config;

import com.fintech.backend.entity.BaseAuditEntity;
import com.fintech.backend.util.UserLoginUtil;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * JPA event audit listener
 */
@Component
@RequiredArgsConstructor
public class AuditListener  {

	/**
	 * Sets created date and created user details before saving
	 * @param object Entity object
	 */
	@PrePersist
	public void prePersist(Object object) {
		if(object instanceof BaseAuditEntity creatableEntity) {
			creatableEntity.setCreatedAt(LocalDateTime.now());
			creatableEntity.setCreatedByIdf(UserLoginUtil.getUserLoginIDP());
		}
	}
	/**
	 * Sets last modified date and last modified by before updating and also logs user / admin activity
	 * @param object Entity object
	 */
	@PreUpdate
	public void preUpdate(Object object) {
		if(object instanceof BaseAuditEntity creatableEntity) {
			creatableEntity.setLastModified(LocalDateTime.now());
			creatableEntity.setLastModifiedByIdf(UserLoginUtil.getUserLoginIDP());
		}
	}
}
