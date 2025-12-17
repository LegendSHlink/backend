package org.example.web_service_v2.domain.companies.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.job_post.entity.JobPost;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cp_name", length = 255)
    private String cpName;

    @Column(length = 255)
    private String location;

    @Column(length = 255)
    private String website;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(name = "member_size")
    private Integer memberSize;

    @Column(name = "cp_size", length = 50)
    private String cpSize;

    // === 연관관계 ===

    // Company 1:N JobPost
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JobPost> jobPosts = new ArrayList<>();
}
