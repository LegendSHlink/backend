package org.example.web_service_v2.domain.profiles.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile_portfolio_links")
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfilePortfolioLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 100)
    private String title;
    private boolean primaryLink;
    private Integer sortOrder;

}
