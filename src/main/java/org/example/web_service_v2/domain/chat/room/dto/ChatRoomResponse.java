package org.example.web_service_v2.domain.chat.room.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomResponse {

    private Long roomId;
    private Long profile1Id;
    private Long profile2Id;
    private String pairKey;

    private String lastMessage;
    private LocalDateTime lastMessageAt;
}
