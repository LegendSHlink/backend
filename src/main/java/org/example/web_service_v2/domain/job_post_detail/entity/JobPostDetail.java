package org.example.web_service_v2.domain.job_post_detail.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.job_post.entity.JobPost;
import org.example.web_service_v2.domain.job_post_detail.enums.JobStatus;

@Entity
@Table(name = "job_post_detail")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPostDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id", nullable = false, unique = true)
    private JobPost jobPost;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(columnDefinition = "TEXT")
    private String qualifications;

    @Column(columnDefinition = "TEXT")
    private String conditions;

    @Column(columnDefinition = "TEXT")
    private String information;

    @Column(name = "procedure_info", columnDefinition = "TEXT")
    private String procedureInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_status")
    private JobStatus jobStatus;

    // === 비즈니스 로직 ===

    public void updateJobPostDetail(String overview, String qualifications, 
                                    String conditions, String information, String procedureInfo) {
        this.overview = overview;
        this.qualifications = qualifications;
        this.conditions = conditions;
        this.information = information;
        this.procedureInfo = procedureInfo;
    }
}
