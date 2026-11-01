package com.librix.repository;

import com.librix.model.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    @Query("select item from UserNotification item where lower(item.user.username) = lower(:username) order by item.createdAt desc")
    List<UserNotification> findForUser(@Param("username") String username);
    @Query("select count(item) from UserNotification item where lower(item.user.username) = lower(:username) and item.read = false")
    long countUnreadForUser(@Param("username") String username);
}
