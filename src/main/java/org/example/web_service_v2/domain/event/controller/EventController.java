package org.example.web_service_v2.domain.event.controller;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.event.dto.EventRequestDTO;
import org.example.web_service_v2.domain.event.dto.EventResponseDTO;
import org.example.web_service_v2.domain.event.service.EventService;
import org.example.web_service_v2.global.apiPayload.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * 이벤트 생성
     * POST /api/events
     */
    @PostMapping
    public ApiResponse<EventResponseDTO.EventCreatedDTO> createEvent(
            @RequestBody EventRequestDTO.CreateEventDTO request
    ) {
        EventResponseDTO.EventCreatedDTO response = eventService.createEvent(request);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 이벤트 목록 조회 (필터링)
     * GET /api/events?status=ONGOING&keyword=테크&page=0&size=10
     */
    @GetMapping
    public ApiResponse<Page<EventResponseDTO.EventListItemDTO>> getEvents(
            @AuthenticationPrincipal Long userId,
            @ModelAttribute EventRequestDTO.EventFilterDTO filter
    ) {
        Page<EventResponseDTO.EventListItemDTO> response = 
                eventService.getEvents(userId, filter);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 이벤트 상세 조회
     * GET /api/events/{eventId}
     */
    @GetMapping("/{eventId}")
    public ApiResponse<EventResponseDTO.EventDetailDTO> getEventDetail(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long eventId
    ) {
        EventResponseDTO.EventDetailDTO response = 
                eventService.getEventDetail(eventId, userId);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 이벤트 신청
     * POST /api/events/{eventId}/register
     */
    @PostMapping("/{eventId}/register")
    public ApiResponse<EventResponseDTO.RegisterResultDTO> registerEvent(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long eventId
    ) {
        EventResponseDTO.RegisterResultDTO response = 
                eventService.registerEvent(eventId, userId);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 이벤트 신청 취소
     * DELETE /api/events/{eventId}/register
     */
    @DeleteMapping("/{eventId}/register")
    public ApiResponse<String> cancelRegister(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long eventId
    ) {
        eventService.cancelRegister(eventId, userId);
        return ApiResponse.onSuccess("이벤트 신청이 취소되었습니다.");
    }

    /**
     * 이벤트 삭제
     * DELETE /api/events/{eventId}
     */
    @DeleteMapping("/{eventId}")
    public ApiResponse<String> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ApiResponse.onSuccess("이벤트가 삭제되었습니다.");
    }
}
