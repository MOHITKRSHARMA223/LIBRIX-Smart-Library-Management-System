package com.librix.repository;

import com.librix.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> { List<Category> findByNameContainingIgnoreCase(String name); }
