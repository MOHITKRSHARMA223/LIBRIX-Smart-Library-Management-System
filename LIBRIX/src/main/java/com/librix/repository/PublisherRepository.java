package com.librix.repository;

import com.librix.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> { List<Publisher> findByNameContainingIgnoreCase(String name); }
