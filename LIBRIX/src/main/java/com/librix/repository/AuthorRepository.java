package com.librix.repository;

import com.librix.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> { List<Author> findByNameContainingIgnoreCase(String name); }
