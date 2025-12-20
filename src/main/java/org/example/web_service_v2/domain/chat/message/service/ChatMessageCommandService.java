package org.example.web_service_v2.domain.chat.message.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.chat.message.ChatMessageRepository;
import org.example.web_service_v2.domain.chat.message.dto.ChatMessageResponse;
import org.example.web_service_v2.domain.chat.message.entity.ChatMessage;
import org.example.web_service_v2.domain.chat.room.ChatRoomRepository;
import org.example.web_service_v2.domain.chat.room.entity.ChatRoom;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageCommandService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public ChatMessageResponse send(Long roomId, Long senderProfileId, String message) {
        if (message == null || message.isBlank()) {
            throw new BusinessException(ErrorCode.CHAT_INVALID_MESSAGE);
        }

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!room.getProfile1().getId().equals(senderProfileId)
                && !room.getProfile2().getId().equals(senderProfileId)) {
            throw new BusinessException(ErrorCode.CHAT_NOT_PARTICIPANT);
        }

        Profile sender = profileRepository.findById(senderProfileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        ChatMessage saved = chatMessageRepository.save(
                ChatMessage.builder()
                        .chatRoom(room)
                        .sender(sender)
                        .message(message)
                        .build()
        );
        LocalDateTime now = LocalDateTime.now();

        room.updateLastMessage(message, now);
        return ChatMessageResponse.builder()
                .messageId(saved.getId())
                .senderProfileId(sender.getId())
                .message(saved.getMessage())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
