package com.librix.repository;
import com.librix.model.ExchangeListing;
import com.librix.model.ExchangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ExchangeListingRepository extends JpaRepository<ExchangeListing, Long> {
    List<ExchangeListing> findByStatusOrderByCreatedAtDesc(ExchangeStatus status);
}
