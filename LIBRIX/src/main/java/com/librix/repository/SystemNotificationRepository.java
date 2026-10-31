package com.librix.repository;

import com.librix.model.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SystemNotificationRepository extends JpaRepository<SystemNotification, Long> { List<SystemNotification> findTop100ByOrderByCreatedAtDesc(); long countByReadFalse(); }
