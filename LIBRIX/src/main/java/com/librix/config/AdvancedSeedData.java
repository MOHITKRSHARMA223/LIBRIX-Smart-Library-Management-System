package com.librix.config;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdvancedSeedData {
    @Bean CommandLineRunner seedAdvanced(CategoryRepository categories, AuthorRepository authors, PublisherRepository publishers, SystemNotificationRepository notifications, LibrarySettingRepository settings) {
        return args -> {
            if (categories.count() == 0) { categories.save(new Category("Computer Science", "Programming, systems, and software engineering")); categories.save(new Category("Literature", "Classic and contemporary fiction")); }
            if (authors.count() == 0) { authors.save(new Author("Robert C. Martin")); authors.save(new Author("James Clear")); }
            if (publishers.count() == 0) publishers.save(new Publisher("Prentice Hall"));
            if (notifications.count() == 0) notifications.save(new SystemNotification("Welcome to LIBRIX", "System notifications are now centralized for administrators.", "SYSTEM"));
            if (settings.count() == 0) { settings.save(new LibrarySetting("library.name", "LIBRIX Central Library")); settings.save(new LibrarySetting("policy.borrowing-days", "14")); settings.save(new LibrarySetting("policy.daily-fine", "2.00")); }
        };
    }
}
