package com.librix.repository;

import com.librix.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> { List<ActivityLog> findTop100ByOrderByCreatedAtDesc(); }
