package org.example.web_service_v2.domain.profiles.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.chat_messages.entity.ChatMessage;
import org.example.web_service_v2.domain.chat_rooms.entity.ChatRoom;
import org.example.web_service_v2.domain.field.entity.Field;
import org.example.web_service_v2.domain.follow.entity.Follow;
import org.example.web_service_v2.domain.job_post.entity.JobPost;
import org.example.web_service_v2.domain.users.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // field: 분야 / 직군
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;

    // === 비즈니스 로직 (정민님 코드에서 가져옴) ===
    // 유저 1:1
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // 한줄 소개
    @Column(length = 150)
    private String headline;

    // 링크
    @OneToMany(
            mappedBy = "profile",
            cascade = CascadeType.ALL
    )
    @Builder.Default
    private List<ProfilePortfolioLink> portfolioLinks = new ArrayList<>();

    // 유저 이미지
    @Column(length = 255)
    private String userImage;

    // === 연관관계 ===

    // Profile 1:N JobPost
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JobPost> jobPosts = new ArrayList<>();

    // Profile 1:N Follow (내가 팔로우한 사람들)
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Follow> followings = new ArrayList<>();

    // Profile 1:N Follow (나를 팔로우한 사람들)
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Follow> followers = new ArrayList<>();

    // Profile 1:N ChatRoom (참여자1로 참여한 채팅방)
    @OneToMany(mappedBy = "profile1", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatRoom> chatRoomsAsProfile1 = new ArrayList<>();

    // Profile 1:N ChatRoom (참여자2로 참여한 채팅방)
    @OneToMany(mappedBy = "profile2", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatRoom> chatRoomsAsProfile2 = new ArrayList<>();

    // Profile 1:N ChatMessage
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatMessage> sentMessages = new ArrayList<>();

    public void updateField(Field field) {
        this.field = field;
    }

    public void attachUser(User user){
        this.user = user;
        if (user != null && user.getProfile() != this){
            user.attachProfile(this);
        }
    }
}
