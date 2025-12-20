package org.example.web_service_v2.domain.chat.room.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "chat_rooms",
        uniqueConstraints = @UniqueConstraint(name = "uk_chat_room_pair_key", columnNames = "pair_key"),
        indexes = {
                @Index(name = "idx_room_profile1", columnList = "profile_id_1"),
                @Index(name = "idx_room_profile2", columnList = "profile_id_2"),
                @Index(name = "idx_room_last_message_at", columnList = "last_message_at")
        }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id_1", nullable = false)
    private Profile profile1;

    @ManyToOne
    @JoinColumn(name = "profile_id_2", nullable = false)
    private Profile profile2;

    @Column(name = "pair_key", nullable = false, unique = true)
    private String pairKey;

    @Column(name = "last_message", length = 500)
    private String lastMessage;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateLastMessage(String message, LocalDateTime at) {
        this.lastMessage = message;
        this.lastMessageAt = at;
    }
}
