package org.example.web_service_v2.domain.chat.room;

import org.example.web_service_v2.domain.chat.room.dto.ChatRoomListItemResponse;
import org.example.web_service_v2.domain.chat.room.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByPairKey(String pairKey);

    @Query("""
        select new org.example.web_service_v2.domain.chat.room.dto.ChatRoomListItemResponse(
            cr.id,
            case when cr.profile1.id = :myProfileId then cr.profile2.id else cr.profile1.id end,
            case when cr.profile1.id = :myProfileId then cr.profile2.user.name else cr.profile1.user.name end,
            case when cr.profile1.id = :myProfileId then cr.profile2.userImage else cr.profile1.userImage end,
            cr.lastMessage,
            cr.lastMessageAt,
            (
              select count(m)
              from org.example.web_service_v2.domain.chat.message.entity.ChatMessage m
              where m.chatRoom = cr
                and m.sender.id <> :myProfileId
                and (
                    rr.lastReadMessageId is null
                    or m.id > rr.lastReadMessageId
                )
            )
        )
        from ChatRoom cr
        left join org.example.web_service_v2.domain.chat.read.entity.ChatRoomRead rr
               on rr.chatRoom = cr and rr.profile.id = :myProfileId
        where cr.profile1.id = :myProfileId
           or cr.profile2.id = :myProfileId
        order by cr.lastMessageAt desc nulls last, cr.updatedAt desc
    """)
    List<ChatRoomListItemResponse> findMyRoomsWithUnread(Long myProfileId);
}
