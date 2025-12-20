package org.example.web_service_v2.domain.chat.message.service;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.chat.message.ChatMessageRepository;
import org.example.web_service_v2.domain.chat.message.dto.ChatMessageResponse;
import org.example.web_service_v2.domain.chat.message.dto.MessageUpdateResponse;
import org.example.web_service_v2.domain.chat.message.entity.ChatMessage;
import org.example.web_service_v2.domain.chat.room.ChatRoomRepository;
import org.example.web_service_v2.domain.chat.room.entity.ChatRoom;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageQueryService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(Long roomId, Long beforeMessageId, int size) {
        int pageSize = clampSize(size);

        List<ChatMessage> messages = (beforeMessageId == null)
                ? chatMessageRepository.findLatest(roomId, PageRequest.of(0, pageSize))
                : chatMessageRepository.findBefore(roomId, beforeMessageId, PageRequest.of(0, pageSize));

        return messages.stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MessageUpdateResponse getUpdates(Long roomId, Long myProfileId, Long afterMessageId) {
        if (afterMessageId == null) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR);
        }

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 참여자 검증
        boolean participant = room.getProfile1().getId().equals(myProfileId)
                || room.getProfile2().getId().equals(myProfileId);
        if (!participant) {
            throw new BusinessException(ErrorCode.CHAT_NOT_PARTICIPANT);
        }

        List<ChatMessage> newOnes = chatMessageRepository.findAfter(roomId, afterMessageId);

        Long latest = newOnes.isEmpty()
                ? afterMessageId
                : newOnes.get(newOnes.size() - 1).getId();

        List<ChatMessageResponse> items = newOnes.stream()
                .map(this::toDto)
                .toList();

        return MessageUpdateResponse.builder()
                .newMessages(items)
                .latestMessageId(latest)
                .build();
    }

    private ChatMessageResponse toDto(ChatMessage m) {
        return ChatMessageResponse.builder()
                .messageId(m.getId())
                .senderProfileId(m.getSender().getId())
                .message(m.getMessage())
                .createdAt(m.getCreatedAt())
                .build();
    }

    private int clampSize(int size) {
        if (size <= 0) return 30;
        return Math.min(size, 50);
    }
}