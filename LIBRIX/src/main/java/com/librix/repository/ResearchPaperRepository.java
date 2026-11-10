package com.librix.repository;
import com.librix.model.ResearchPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ResearchPaperRepository extends JpaRepository<ResearchPaper, Long> {
    List<ResearchPaper> findTop100ByOrderByCreatedAtDesc();
}
