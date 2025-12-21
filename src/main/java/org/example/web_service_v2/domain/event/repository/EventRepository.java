package org.example.web_service_v2.domain.event.repository;

import org.example.web_service_v2.domain.event.entity.Event;
import org.example.web_service_v2.domain.event.enums.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    // 전체 이벤트 조회 (최신순)
    Page<Event> findAllByOrderByEventDateDesc(Pageable pageable);
    
    // 상태별 이벤트 조회
    Page<Event> findByEventStatusOrderByEventDateDesc(
        EventStatus eventStatus, 
        Pageable pageable
    );
    
    // 진행 중인 이벤트만 조회
    @Query("SELECT e FROM Event e WHERE e.eventStatus = :status ORDER BY e.eventDate DESC")
    Page<Event> findOngoingEvents(
        @Param("status") EventStatus status,
        Pageable pageable
    );
    
    // 키워드 검색 (제목 또는 장소)
    @Query("SELECT e FROM Event e WHERE e.eventTitle LIKE %:keyword% " +
           "OR e.location LIKE %:keyword% ORDER BY e.eventDate DESC")
    Page<Event> searchByKeyword(
        @Param("keyword") String keyword,
        Pageable pageable
    );
    
    // 날짜 범위로 검색
    Page<Event> findByEventDateBetweenOrderByEventDateDesc(
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );
}
