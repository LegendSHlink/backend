package org.example.web_service_v2.domain.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class EventRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class CreateEventDTO {
        private String eventImage;
        private String eventTitle;
        private LocalDateTime eventDate;
        private String location;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateEventDTO {
        private String eventImage;
        private String eventTitle;
        private LocalDateTime eventDate;
        private String location;
        private String eventStatus; // ONGOING, ENDED, UPCOMING
    }

    @Getter
    @NoArgsConstructor
    public static class EventFilterDTO {
        private String status;      // ONGOING, ENDED, UPCOMING
        private String keyword;     // 검색 키워드
        private Integer page = 0;
        private Integer size = 10;
    }

    @Getter
    @NoArgsConstructor
    public static class RegisterEventDTO {
        private Long eventId;
    }
}
