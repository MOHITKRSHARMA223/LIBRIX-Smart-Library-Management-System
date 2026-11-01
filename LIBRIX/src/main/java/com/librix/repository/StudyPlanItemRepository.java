package com.librix.repository;

import com.librix.model.StudyPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface StudyPlanItemRepository extends JpaRepository<StudyPlanItem, Long> {
    @Query("select item from StudyPlanItem item where lower(item.user.username) = lower(:username) order by item.scheduledDate asc")
    List<StudyPlanItem> findForUser(@Param("username") String username);
}
