package com.librix.config;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdvancedSeedData {
    @Bean CommandLineRunner seedAdvanced(CategoryRepository categories, AuthorRepository authors, PublisherRepository publishers, SystemNotificationRepository notifications, LibrarySettingRepository settings, StudyResourceRepository resources) {
        return args -> {
            addCategory(categories, "Computer Science", "Programming, systems, and software engineering");
            addCategory(categories, "Literature", "Classic and contemporary fiction");
            addCategory(categories, "Personal Development", "Habits, learning, and personal growth");
            addCategory(categories, "History", "World history and civilization");
            addAuthor(authors, "Robert C. Martin");
            addAuthor(authors, "James Clear");
            addAuthor(authors, "Jane Austen");
            addAuthor(authors, "Paulo Coelho");
            addAuthor(authors, "Yuval Noah Harari");
            addPublisher(publishers, "Prentice Hall");
            addPublisher(publishers, "Penguin Classics");
            addPublisher(publishers, "HarperCollins");
            if (notifications.findTop100ByOrderByCreatedAtDesc().stream().noneMatch(n -> "Welcome to LIBRIX".equals(n.getTitle()))) {
                notifications.save(new SystemNotification("Welcome to LIBRIX", "System notifications are now centralized for administrators.", "SYSTEM"));
            }
            addSetting(settings, "library.name", "LIBRIX Central Library");
            addSetting(settings, "policy.borrowing-days", "14");
            addSetting(settings, "policy.daily-fine", "2.00");
            addResource(resources, "Academic research starter kit", "RESEARCH", "A practical guide to finding, evaluating, and citing high-quality sources.", "https://owl.purdue.edu/owl/research_and_citation/conducting_research/index.html");
            addResource(resources, "Deep work study session", "FOCUS", "Use a simple time-blocking rhythm to make your next study session count.", "https://learningcenter.unc.edu/tips-and-tools/studying-101-study-smarter-not-harder/");
        };
    }

    private static void addCategory(CategoryRepository repository, String name, String description) {
        if (repository.findAll().stream().noneMatch(item -> name.equalsIgnoreCase(item.getName()))) {
            repository.save(new Category(name, description));
        }
    }

    private static void addAuthor(AuthorRepository repository, String name) {
        if (repository.findAll().stream().noneMatch(item -> name.equalsIgnoreCase(item.getName()))) {
            repository.save(new Author(name));
        }
    }

    private static void addPublisher(PublisherRepository repository, String name) {
        if (repository.findAll().stream().noneMatch(item -> name.equalsIgnoreCase(item.getName()))) {
            repository.save(new Publisher(name));
        }
    }

    private static void addSetting(LibrarySettingRepository repository, String key, String value) {
        if (repository.findById(key).isEmpty()) repository.save(new LibrarySetting(key, value));
    }

    private static void addResource(StudyResourceRepository repository, String title, String type, String description, String url) {
        if (repository.findAll().stream().noneMatch(item -> title.equalsIgnoreCase(item.getTitle()))) {
            repository.save(new StudyResource(title, type, description, url));
        }
    }
}
