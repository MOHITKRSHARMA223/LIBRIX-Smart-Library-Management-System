package com.librix.config;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DevelopmentData {
    @Bean CommandLineRunner seed(AppUserRepository users, LibraryBookRepository books, LibraryMemberRepository members, PasswordEncoder encoder) {
        return args -> {
            if (users.count() == 0) {
                users.save(new AppUser("admin", encoder.encode(System.getenv().getOrDefault("LIBRIX_DEMO_ADMIN_PASSWORD", "admin123")), Role.SUPER_ADMIN));
                users.save(new AppUser("student", encoder.encode(System.getenv().getOrDefault("LIBRIX_DEMO_STUDENT_PASSWORD", "student123")), Role.STUDENT));
            }
            if (books.count() == 0) {
                books.save(new LibraryBook("9780132350884", "Clean Code", "Robert C. Martin", "Computer Science", 8));
                books.save(new LibraryBook("9780735211292", "Atomic Habits", "James Clear", "Personal Development", 5));
                books.save(new LibraryBook("9780141439518", "Pride and Prejudice", "Jane Austen", "Literature", 4));
            }
            if (members.count() == 0) members.save(new LibraryMember("Aisha Rahman", "aisha@example.com", "+91 98765 43210"));
            users.findByUsernameIgnoreCase("student").ifPresent(student -> members.findByUser_Username("student").orElseGet(() -> {
                LibraryMember profile = new LibraryMember("LIBRIX Student", "student@example.com", "");
                profile.setUser(student);
                return members.save(profile);
            }));
        };
    }
}
