package org.example.web_service_v2.domain.job_post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.companies.entity.Company;
import org.example.web_service_v2.domain.field.entity.Field;
import org.example.web_service_v2.domain.job_post_detail.entity.JobPostDetail;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_post")
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profiles_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companies_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    private Integer size;

    @Column(name = "required_experience")
    private String requiredExperience;  // 요구 경력 (예: "신입", "경력 2년", "경력 3~5년")

    // === 연관관계 ===

    // JobPost 1:1 JobPostDetail
    @OneToOne(mappedBy = "jobPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private JobPostDetail jobPostDetail;


    // === 비즈니스 로직 ===

    public void updateJobPost(String description, LocalDateTime endDate, Integer size) {
        this.description = description;
        this.endDate = endDate;
        this.size = size;
    }

    public void updateJobPost(String description, LocalDateTime endDate, Integer size, String requiredExperience) {
        this.description = description;
        this.endDate = endDate;
        this.size = size;
        this.requiredExperience = requiredExperience;
    }
}
