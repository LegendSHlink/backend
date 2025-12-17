package org.example.web_service_v2.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.web_service_v2.user.profile.entity.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Profile profile;

    public Profile ensureProfile(){
        if (this.profile == null){
            Profile p = Profile.create(this);
            this.profile = p;
        }

        return this.profile;
    }

    @Column(length = 64)
    private String refreshTokenHash;

    private LocalDateTime refreshTokenExpiresAt;

    public void updateRefreshToken(String tokenHash, LocalDateTime expiresAt){
        this.refreshTokenHash = tokenHash;
        this.refreshTokenExpiresAt = expiresAt;
    }

    public void clearRefreshToken(){
        this.refreshTokenHash = null;
        this.refreshTokenExpiresAt = null;
    }



}
