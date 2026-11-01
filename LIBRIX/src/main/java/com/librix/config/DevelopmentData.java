package com.librix.config;

import com.librix.model.*;
import com.librix.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;

@Configuration
public class DevelopmentData {
    @Bean CommandLineRunner seed(AppUserRepository users, LibraryBookRepository books, LibraryMemberRepository members,
                                 LoanRepository loans, BookReservationRepository reservations, PasswordEncoder encoder) {
        return args -> {
            user(users, encoder, "admin", Role.SUPER_ADMIN,
                    System.getenv().getOrDefault("LIBRIX_DEMO_ADMIN_PASSWORD", "admin123"));
            user(users, encoder, "librarian", Role.LIBRARIAN, "librarian123");
            AppUser student = user(users, encoder, "student", Role.STUDENT,
                    System.getenv().getOrDefault("LIBRIX_DEMO_STUDENT_PASSWORD", "student123"));
            AppUser aishaUser = user(users, encoder, "aisha", Role.STUDENT, "aisha123");
            AppUser rohanUser = user(users, encoder, "rohan", Role.STUDENT, "rohan123");

            LibraryMember studentMember = member(members, student, "LIBRIX Student", "student@example.com", "+91 90000 00001");
            LibraryMember aisha = member(members, aishaUser, "Aisha Rahman", "aisha@example.com", "+91 98765 43210");
            LibraryMember rohan = member(members, rohanUser, "Rohan Mehta", "rohan@example.com", "+91 98765 43211");
            member(members, null, "Maya Kapoor", "maya@example.com", "+91 98765 43212");

            LibraryBook cleanCode = book(books, "9780132350884", "Clean Code", "Robert C. Martin", "Computer Science", 8, "A-01");
            LibraryBook atomicHabits = book(books, "9780735211292", "Atomic Habits", "James Clear", "Personal Development", 5, "B-04");
            book(books, "9780141439518", "Pride and Prejudice", "Jane Austen", "Literature", 4, "C-02");
            LibraryBook pragmaticProgrammer = book(books, "9780135957059", "The Pragmatic Programmer", "David Thomas and Andrew Hunt", "Computer Science", 3, "A-03");
            LibraryBook designPatterns = book(books, "9780201633610", "Design Patterns", "Erich Gamma et al.", "Computer Science", 1, "A-04");
            book(books, "9780307474278", "The Alchemist", "Paulo Coelho", "Literature", 6, "C-05");
            book(books, "9780062316097", "Sapiens", "Yuval Noah Harari", "History", 3, "D-01");

            if (loans.count() == 0) {
                borrowForSeed(books, loans, cleanCode, studentMember, LocalDate.now().minusDays(4), LocalDate.now().plusDays(10));
                borrowForSeed(books, loans, atomicHabits, aisha, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6));
                borrowForSeed(books, loans, pragmaticProgrammer, rohan, LocalDate.now().minusDays(2), LocalDate.now().plusDays(12));
                borrowForSeed(books, loans, designPatterns, aisha, LocalDate.now().minusDays(35), LocalDate.now().minusDays(21));
                Loan returned = new Loan(cleanCode, aisha, LocalDate.now().minusDays(45), LocalDate.now().minusDays(31));
                returned.returnLoan(LocalDate.now().minusDays(32));
                loans.save(returned);
            }
            if (reservations.count() == 0 && !designPatterns.isAvailable()) {
                reservations.save(new BookReservation(designPatterns, studentMember));
            }
        };
    }

    private static AppUser user(AppUserRepository users, PasswordEncoder encoder, String username, Role role, String password) {
        return users.findByUsernameIgnoreCase(username)
                .orElseGet(() -> users.save(new AppUser(username, encoder.encode(password), role)));
    }

    private static LibraryMember member(LibraryMemberRepository members, AppUser user, String name, String email, String phone) {
        return members.findByEmailIgnoreCase(email).orElseGet(() -> {
            LibraryMember result = new LibraryMember(name, email, phone);
            result.setUser(user);
            return members.save(result);
        });
    }

    private static LibraryBook book(LibraryBookRepository books, String isbn, String title, String author,
                                   String category, int copies, String shelf) {
        LibraryBook result = books.findAll().stream()
                .filter(book -> isbn.equals(book.getIsbn()))
                .findFirst()
                .orElseGet(() -> books.save(new LibraryBook(isbn, title, author, category, copies)));
        if (result.getShelfLocation() == null) {
            result.setShelfLocation(shelf);
            books.save(result);
        }
        return result;
    }

    private static void borrowForSeed(LibraryBookRepository books, LoanRepository loans, LibraryBook book, LibraryMember member,
                                      LocalDate borrowedAt, LocalDate dueAt) {
        if (book.isAvailable()) {
            book.borrowCopy();
            books.save(book);
            loans.save(new Loan(book, member, borrowedAt, dueAt));
        }
    }
}
