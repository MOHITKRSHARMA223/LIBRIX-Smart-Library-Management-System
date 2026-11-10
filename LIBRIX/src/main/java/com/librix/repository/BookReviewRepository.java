package com.librix.repository;
import com.librix.model.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findTop100ByOrderByCreatedAtDesc();
    List<BookReview> findByBookIdOrderByCreatedAtDesc(Long bookId);
    @Query("select review from BookReview review where lower(review.user.username) = lower(:username) and review.book.id = :bookId")
    Optional<BookReview> findForUserAndBook(@Param("username") String username, @Param("bookId") Long bookId);
}
