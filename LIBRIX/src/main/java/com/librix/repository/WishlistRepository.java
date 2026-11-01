package com.librix.repository;

import com.librix.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    @Query("select item from WishlistItem item where lower(item.user.username) = lower(:username) order by item.createdAt desc")
    List<WishlistItem> findForUser(@Param("username") String username);
    @Query("select item from WishlistItem item where lower(item.user.username) = lower(:username) and item.book.id = :bookId")
    Optional<WishlistItem> findForUserAndBook(@Param("username") String username, @Param("bookId") Long bookId);
}
