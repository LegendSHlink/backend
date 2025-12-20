package org.example.web_service_v2.domain.event.converter;

import org.example.web_service_v2.domain.event.dto.EventRequestDTO;
import org.example.web_service_v2.domain.event.dto.EventResponseDTO;
import org.example.web_service_v2.domain.event.entity.Event;
import org.example.web_service_v2.domain.event.enums.EventStatus;
import org.example.web_service_v2.domain.event_register.entity.EventRegister;
import org.example.web_service_v2.domain.event_register.enums.RegisterStatus;
import org.example.web_service_v2.domain.users.entity.User;

public class EventConverter {

    // Entity -> Response DTO
    public static EventResponseDTO.EventListItemDTO toEventListItemDTO(
            Event event,
            boolean isRegistered,
            long registeredCount
    ) {
        return EventResponseDTO.EventListItemDTO.builder()
                .eventId(event.getId())
                .eventImage(event.getEventImage())
                .eventTitle(event.getEventTitle())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .eventStatus(event.getEventStatus().name())
                .isRegistered(isRegistered)
                .registeredCount(registeredCount)
                .build();
    }

    public static EventResponseDTO.EventDetailDTO toEventDetailDTO(
            Event event,
            boolean isRegistered,
            long registeredCount,
            String registerStatus
    ) {
        return EventResponseDTO.EventDetailDTO.builder()
                .eventId(event.getId())
                .eventImage(event.getEventImage())
                .eventTitle(event.getEventTitle())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .eventStatus(event.getEventStatus().name())
                .isRegistered(isRegistered)
                .registeredCount(registeredCount)
                .registerStatus(registerStatus)
                .build();
    }

    // Request DTO -> Entity
    public static Event toEvent(EventRequestDTO.CreateEventDTO request) {
        return Event.builder()
                .eventImage(request.getEventImage())
                .eventTitle(request.getEventTitle())
                .eventDate(request.getEventDate())
                .location(request.getLocation())
                .eventStatus(EventStatus.UPCOMING)
                .build();
    }

    public static EventRegister toEventRegister(Event event, User user) {
        return EventRegister.builder()
                .event(event)
                .user(user)
                .registerStatus(RegisterStatus.REGISTERED)
                .build();
    }
}
