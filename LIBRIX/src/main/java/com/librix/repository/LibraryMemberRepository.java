package com.librix.repository;

import com.librix.model.LibraryMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
    Optional<LibraryMember> findByEmailIgnoreCase(String email);
    Optional<LibraryMember> findByUser_Username(String username);
    List<LibraryMember> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String fullName, String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    long countByActiveTrue();
}

