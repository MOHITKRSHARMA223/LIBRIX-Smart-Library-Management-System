package com.librix.repository;

import com.librix.model.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
    List<LibraryBook> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    long countByAvailableCopiesGreaterThan(int copies);
    long countByCategoryIgnoreCase(String category);
    long countByAuthorIgnoreCase(String author);
}
