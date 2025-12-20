package org.example.web_service_v2.domain.chat.room.service;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.chat.room.ChatRoomRepository;
import org.example.web_service_v2.domain.chat.room.dto.ChatRoomListItemResponse;
import org.example.web_service_v2.domain.profiles.repositories.ProfileRepository;
import org.example.web_service_v2.global.apiPayload.exception.BusinessException;
import org.example.web_service_v2.global.apiPayload.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryService {
    private final ChatRoomRepository chatRoomRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomListItemResponse> getMyRooms(Long myProfileId){
        profileRepository.findById(myProfileId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        return chatRoomRepository.findMyRoomsWithUnread(myProfileId);
    }
}
