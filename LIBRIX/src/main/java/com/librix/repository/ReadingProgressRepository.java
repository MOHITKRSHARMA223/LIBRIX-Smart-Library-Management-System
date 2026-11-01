package com.librix.repository;

import com.librix.model.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    @Query("select item from ReadingProgress item where lower(item.user.username) = lower(:username) order by item.updatedAt desc")
    List<ReadingProgress> findForUser(@Param("username") String username);
    @Query("select item from ReadingProgress item where lower(item.user.username) = lower(:username) and item.book.id = :bookId")
    Optional<ReadingProgress> findForUserAndBook(@Param("username") String username, @Param("bookId") Long bookId);
}
