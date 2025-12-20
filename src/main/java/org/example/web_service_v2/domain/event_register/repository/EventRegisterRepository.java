package org.example.web_service_v2.domain.event_register.repository;

import org.example.web_service_v2.domain.event_register.entity.EventRegister;
import org.example.web_service_v2.domain.event_register.enums.RegisterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRegisterRepository extends JpaRepository<EventRegister, Long> {
    
    // 사용자가 특정 이벤트에 신청했는지 확인
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
    
    // 특정 이벤트의 신청자 목록
    List<EventRegister> findByEventId(Long eventId);
    
    // 특정 사용자의 신청 목록
    List<EventRegister> findByUserId(Long userId);
    
    // 사용자의 특정 이벤트 신청 정보
    Optional<EventRegister> findByEventIdAndUserId(Long eventId, Long userId);
    
    // 이벤트의 신청자 수 (상태별)
    @Query("SELECT COUNT(er) FROM EventRegister er " +
           "WHERE er.event.id = :eventId AND er.registerStatus = :status")
    long countByEventIdAndStatus(
        @Param("eventId") Long eventId,
        @Param("status") RegisterStatus status
    );
}
