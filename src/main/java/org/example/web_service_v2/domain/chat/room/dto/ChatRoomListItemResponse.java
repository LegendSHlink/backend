package org.example.web_service_v2.domain.chat.room.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoomListItemResponse {
    private Long roomId;

    private Long otherProfileId;
    private String otherNickname;
    private String otherUserImage;

    private String lastMessage;
    private LocalDateTime lastMessageAt;

    private long unreadCount;
}
