package com.librix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "study_resources")
public class StudyResource {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 180) private String title;
    @Column(nullable = false, length = 80) private String resourceType;
    @Column(nullable = false, length = 500) private String description;
    @Column(nullable = false, length = 500) private String url;
    protected StudyResource() { }
    public StudyResource(String title, String resourceType, String description, String url) { this.title = title; this.resourceType = resourceType; this.description = description; this.url = url; }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getResourceType() { return resourceType; }
    public String getDescription() { return description; }
    public String getUrl() { return url; }
}
