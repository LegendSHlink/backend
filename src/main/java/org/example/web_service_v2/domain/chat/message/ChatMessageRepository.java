package org.example.web_service_v2.domain.chat.message;

import org.example.web_service_v2.domain.chat.message.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("""
        select m
        from ChatMessage m
        where m.chatRoom.id = :roomId
        order by m.id desc
    """)
    List<ChatMessage> findLatest(Long roomId, Pageable pageable);

    @Query("""
        select m
        from ChatMessage m
        where m.chatRoom.id = :roomId
          and m.id < :beforeId
        order by m.id desc
    """)
    List<ChatMessage> findBefore(Long roomId, Long beforeId, Pageable pageable);

    @Query("""
        select m
        from ChatMessage m
        where m.chatRoom.id = :roomId
          and m.id > :afterId
        order by m.id asc
    """)
    List<ChatMessage> findAfter(Long roomId, Long afterId);

    boolean existsByIdAndChatRoomId(Long id, Long chatRoomId);
}
