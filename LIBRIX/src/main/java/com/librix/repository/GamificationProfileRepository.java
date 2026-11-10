package com.librix.repository;
import com.librix.model.GamificationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface GamificationProfileRepository extends JpaRepository<GamificationProfile, Long> {
    Optional<GamificationProfile> findByUser_UsernameIgnoreCase(String username);
}
