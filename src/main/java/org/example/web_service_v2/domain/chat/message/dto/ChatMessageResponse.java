package org.example.web_service_v2.domain.chat.message.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessageResponse {
    private Long messageId;
    private Long senderProfileId;
    private String message;
    private LocalDateTime createdAt;
}
