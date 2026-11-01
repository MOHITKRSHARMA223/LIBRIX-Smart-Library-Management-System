package com.librix.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "study_plan_items", indexes = @Index(name = "idx_study_plan_date", columnList = "scheduled_date"))
public class StudyPlanItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false) @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    @Column(nullable = false, length = 180)
    private String title;
    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;
    @Column(nullable = false)
    private int durationMinutes;
    @Column(nullable = false)
    private boolean completed;

    protected StudyPlanItem() { }
    public StudyPlanItem(AppUser user, String title, LocalDate scheduledDate, int durationMinutes) {
        this.user = user; this.title = title; this.scheduledDate = scheduledDate; this.durationMinutes = durationMinutes;
    }
    public Long getId() { return id; }
    public AppUser getUser() { return user; }
    public String getTitle() { return title; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public int getDurationMinutes() { return durationMinutes; }
    public boolean isCompleted() { return completed; }
    public void markCompleted() { completed = true; }
    public void markOpen() { completed = false; }
}
