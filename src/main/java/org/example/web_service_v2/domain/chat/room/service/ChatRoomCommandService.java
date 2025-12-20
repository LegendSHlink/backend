package org.example.web_service_v2.domain.chat.room.service;


import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.chat.room.ChatRoomRepository;
import org.example.web_service_v2.domain.chat.room.dto.ChatRoomResponse;
import org.example.web_service_v2.domain.chat.room.entity.ChatRoom;
import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public ChatRoomResponse createOrGet(Long myProfileId, Long otherProfileId) {
        if (myProfileId.equals(otherProfileId)) {
            throw new BusinessException(ErrorCode.PROFILE_NOT_FOUND);
        }

        Profile me = profileRepository.findById(myProfileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));
        Profile other = profileRepository.findById(otherProfileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        Profile p1 = me.getId() < other.getId() ? me : other;
        Profile p2 = me.getId() < other.getId() ? other : me;

        String pairKey = p1.getId() + ":" + p2.getId();

        ChatRoom room = chatRoomRepository.findByPairKey(pairKey)
                .orElseGet(() -> {
                    try {
                        return chatRoomRepository.save(
                                ChatRoom.builder()
                                        .profile1(p1)
                                        .profile2(p2)
                                        .pairKey(pairKey)
                                        .build()
                        );
                    } catch (DataIntegrityViolationException e) {
                        return chatRoomRepository.findByPairKey(pairKey)
                                .orElseThrow(() ->
                                        new BusinessException(ErrorCode.CHAT_ROOM_CONFLICT));
                    }
                });

        return ChatRoomResponse.builder()
                .roomId(room.getId())
                .profile1Id(room.getProfile1().getId())
                .profile2Id(room.getProfile2().getId())
                .pairKey(room.getPairKey())
                .lastMessage(room.getLastMessage())
                .lastMessageAt(room.getLastMessageAt())
                .build();
    }

}