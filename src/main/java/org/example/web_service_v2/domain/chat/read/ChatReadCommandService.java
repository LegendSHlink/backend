package org.example.web_service_v2.domain.chat.read;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.chat.message.ChatMessageRepository;
import org.example.web_service_v2.domain.chat.read.entity.ChatRoomRead;
import org.example.web_service_v2.domain.chat.room.ChatRoomRepository;
import org.example.web_service_v2.domain.chat.room.entity.ChatRoom;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ChatReadCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProfileRepository profileRepository;
    private final ChatRoomReadRepository chatRoomReadRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void markAsRead(Long roomId, Long myProfileId, Long lastReadMessageId) {
        if (lastReadMessageId == null) {
            throw new BusinessException(ErrorCode.CHAT_NOT_PARTICIPANT);
        }

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 참여자 검증
        boolean participant = room.getProfile1().getId().equals(myProfileId)
                || room.getProfile2().getId().equals(myProfileId);
        if (!participant) {
            throw new BusinessException(ErrorCode.CHAT_NOT_PARTICIPANT);
        }

        // lastReadMessageId가 이 방의 메시지가 맞는지 검증(조작 방지)
        boolean valid = chatMessageRepository.existsByIdAndChatRoomId(lastReadMessageId, roomId);
        if (!valid) {
            throw new BusinessException(ErrorCode.CHAT_MESSAGE_NOT_IN_ROOM);
        }

        Profile me = profileRepository.findById(myProfileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        ChatRoomRead read = chatRoomReadRepository.findByChatRoomIdAndProfileId(roomId, myProfileId)
                .orElseGet(() -> chatRoomReadRepository.save(
                        ChatRoomRead.builder()
                                .chatRoom(room)
                                .profile(me)
                                .lastReadMessageId(null)
                                .build()
                ));

        // 뒤로 되감기 방지: 이미 더 많이 읽었으면 유지
        Long current = read.getLastReadMessageId();
        if (current == null || lastReadMessageId > current) {
            read.updateLastReadMessageId(lastReadMessageId);
        }
    }
}