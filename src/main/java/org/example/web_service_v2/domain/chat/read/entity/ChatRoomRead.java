package org.example.web_service_v2.domain.chat.read.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.chat.room.entity.ChatRoom;
import org.example.web_service_v2.domain.profiles.entity.Profile;

@Entity
@Table(
        name = "chat_room_reads",
        uniqueConstraints = @UniqueConstraint(name="uk_room_profile", columnNames = {"chat_room_id", "profile_id"})
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id", nullable = false)
    private Profile profile;

    @Column(name="last_read_message_id")
    private Long lastReadMessageId;

    public void updateLastReadMessageId(Long messageId) {
        this.lastReadMessageId = messageId;
    }
}
