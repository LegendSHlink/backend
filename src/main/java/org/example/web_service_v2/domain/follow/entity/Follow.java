package org.example.web_service_v2.domain.follow.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.follow.enums.FollowStatus;
import org.example.web_service_v2.domain.profiles.entity.Profile;

@Entity
@Table(name = "follow",
       uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private Profile follower; // 팔로우 하는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private Profile following; // 팔로우 받는 사람

    @Enumerated(EnumType.STRING)
    @Column(name = "follow_status", nullable = false)
    private FollowStatus followStatus;

    // === 비즈니스 로직 ===

    public void updateStatus(FollowStatus status) {
        this.followStatus = status;
    }
}
