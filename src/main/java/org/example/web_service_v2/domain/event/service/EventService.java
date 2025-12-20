package org.example.web_service_v2.domain.event.service;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.domain.event.converter.EventConverter;
import org.example.web_service_v2.domain.event.dto.EventRequestDTO;
import org.example.web_service_v2.domain.event.dto.EventResponseDTO;
import org.example.web_service_v2.domain.event.entity.Event;
import org.example.web_service_v2.domain.event.enums.EventStatus;
import org.example.web_service_v2.domain.event.repository.EventRepository;
import org.example.web_service_v2.domain.event_register.entity.EventRegister;
import org.example.web_service_v2.domain.event_register.enums.RegisterStatus;
import org.example.web_service_v2.domain.event_register.repository.EventRegisterRepository;
import org.example.web_service_v2.domain.users.entity.User;
import org.example.web_service_v2.domain.users.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final EventRegisterRepository eventRegisterRepository;
    private final UserRepository userRepository;

    // 이벤트 생성
    @Transactional
    public EventResponseDTO.EventCreatedDTO createEvent(
            EventRequestDTO.CreateEventDTO request
    ) {
        Event event = EventConverter.toEvent(request);
        Event savedEvent = eventRepository.save(event);

        return EventResponseDTO.EventCreatedDTO.builder()
                .eventId(savedEvent.getId())
                .message("이벤트가 등록되었습니다.")
                .build();
    }

    // 이벤트 목록 조회
    public Page<EventResponseDTO.EventListItemDTO> getEvents(
            Long userId,
            EventRequestDTO.EventFilterDTO filter
    ) {
        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                Sort.by(Sort.Direction.DESC, "eventDate")
        );

        Page<Event> events;

        // 필터링
        if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
            events = eventRepository.searchByKeyword(filter.getKeyword(), pageable);
        } else if (filter.getStatus() != null) {
            EventStatus status = EventStatus.valueOf(filter.getStatus());
            events = eventRepository.findByEventStatusOrderByEventDateDesc(status, pageable);
        } else {
            events = eventRepository.findAllByOrderByEventDateDesc(pageable);
        }

        // 각 이벤트에 대해 신청 여부와 신청자 수 조회
        return events.map(event -> {
            boolean isRegistered = eventRegisterRepository
                    .existsByEventIdAndUserId(event.getId(), userId);
            long registeredCount = eventRegisterRepository
                    .countByEventIdAndStatus(event.getId(), RegisterStatus.REGISTERED);

            return EventConverter.toEventListItemDTO(event, isRegistered, registeredCount);
        });
    }

    // 이벤트 상세 조회
    public EventResponseDTO.EventDetailDTO getEventDetail(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없습니다."));

        boolean isRegistered = eventRegisterRepository
                .existsByEventIdAndUserId(eventId, userId);
        long registeredCount = eventRegisterRepository
                .countByEventIdAndStatus(eventId, RegisterStatus.REGISTERED);

        String registerStatus = null;
        if (isRegistered) {
            EventRegister register = eventRegisterRepository
                    .findByEventIdAndUserId(eventId, userId)
                    .orElse(null);
            if (register != null) {
                registerStatus = register.getRegisterStatus().name();
            }
        }

        return EventConverter.toEventDetailDTO(
                event, isRegistered, registeredCount, registerStatus
        );
    }

    // 이벤트 신청
    @Transactional
    public EventResponseDTO.RegisterResultDTO registerEvent(Long eventId, Long userId) {
        // 이미 신청했는지 확인
        if (eventRegisterRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new RuntimeException("이미 신청한 이벤트입니다.");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        EventRegister register = EventConverter.toEventRegister(event, user);
        EventRegister savedRegister = eventRegisterRepository.save(register);

        return EventResponseDTO.RegisterResultDTO.builder()
                .registerId(savedRegister.getId())
                .message("이벤트 신청이 완료되었습니다.")
                .registerStatus(savedRegister.getRegisterStatus().name())
                .build();
    }

    // 이벤트 신청 취소
    @Transactional
    public void cancelRegister(Long eventId, Long userId) {
        EventRegister register = eventRegisterRepository
                .findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("신청 내역을 찾을 수 없습니다."));

        eventRegisterRepository.delete(register);
    }

    // 이벤트 삭제
    @Transactional
    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없습니다."));

        eventRepository.delete(event);
    }
}
