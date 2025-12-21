package org.example.web_service_v2.domain.event.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class EventResponseDTO {

    @Getter
    @Builder
    public static class EventListItemDTO {
        private Long eventId;
        private String eventImage;
        private String eventTitle;
        private LocalDateTime eventDate;
        private String location;
        private String eventStatus;      // ONGOING, ENDED, UPCOMING
        private Boolean isRegistered;     // 신청 여부
        private Long registeredCount;     // 신청자 수
    }

    @Getter
    @Builder
    public static class EventDetailDTO {
        private Long eventId;
        private String eventImage;
        private String eventTitle;
        private LocalDateTime eventDate;
        private String location;
        private String eventStatus;
        private Boolean isRegistered;
        private Long registeredCount;
        private String registerStatus;    // 내 신청 상태
    }

    @Getter
    @Builder
    public static class EventCreatedDTO {
        private Long eventId;
        private String message;
    }

    @Getter
    @Builder
    public static class RegisterResultDTO {
        private Long registerId;
        private String message;
        private String registerStatus;
    }
}
