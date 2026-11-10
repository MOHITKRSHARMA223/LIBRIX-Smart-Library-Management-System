package com.librix.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table(name = "research_papers")
public class ResearchPaper {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER) private AppUser submittedBy;
    @NotBlank @Column(nullable = false, length = 240) private String title;
    @NotBlank @Column(nullable = false, length = 160) private String authors;
    @NotBlank @Column(nullable = false, length = 80) private String subject;
    @NotBlank @Column(nullable = false, length = 2000) private String abstractText;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    protected ResearchPaper() { }
    public ResearchPaper(AppUser submittedBy, String title, String authors, String subject, String abstractText) { this.submittedBy = submittedBy; this.title = title; this.authors = authors; this.subject = subject; this.abstractText = abstractText; }
    public Long getId() { return id; } public AppUser getSubmittedBy() { return submittedBy; } public String getTitle() { return title; }
    public String getAuthors() { return authors; } public String getSubject() { return subject; } public String getAbstractText() { return abstractText; } public Instant getCreatedAt() { return createdAt; }
}
