package com.librix.repository;
import com.librix.model.GamificationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
public interface GamificationProfileRepository extends JpaRepository<GamificationProfile, Long> {
    @Query("select profile from GamificationProfile profile where lower(profile.user.username) = lower(:username)")
    Optional<GamificationProfile> findForUser(@Param("username") String username);
}
