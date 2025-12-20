package org.example.web_service_v2.domain.chat.message.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MessageUpdateResponse {

    private List<ChatMessageResponse> newMessages;
    private Long latestMessageId;

}
