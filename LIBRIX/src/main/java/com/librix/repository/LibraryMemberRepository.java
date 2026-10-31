package com.librix.repository;

import com.librix.model.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
    Optional<LibraryMember> findByEmailIgnoreCase(String email);
    long countByActiveTrue();
}
