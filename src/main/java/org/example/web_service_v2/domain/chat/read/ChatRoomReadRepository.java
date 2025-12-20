package org.example.web_service_v2.domain.chat.read;

import org.example.web_service_v2.domain.chat.read.entity.ChatRoomRead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomReadRepository extends JpaRepository<ChatRoomRead, Long> {
    Optional<ChatRoomRead> findByChatRoomIdAndProfileId(Long roomId, Long profileId);
}
