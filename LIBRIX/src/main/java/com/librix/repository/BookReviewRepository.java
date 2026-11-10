package com.librix.repository;
import com.librix.model.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    List<BookReview> findTop100ByOrderByCreatedAtDesc();
    List<BookReview> findByBookIdOrderByCreatedAtDesc(Long bookId);
    Optional<BookReview> findByUser_UsernameIgnoreCaseAndBookId(String username, Long bookId);
}
