package org.example.web_service_v2.domain.chat_rooms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.chat_messages.entity.ChatMessage;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_rooms",
       uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id_1", "profile_id_2"}))
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id_1", nullable = false)
    private Profile profile1; // 참여자 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id_2", nullable = false)
    private Profile profile2; // 참여자 2

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // === 연관관계 ===

    // ChatRoom 1:N ChatMessage
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ChatMessage> messages = new ArrayList<>();
}
