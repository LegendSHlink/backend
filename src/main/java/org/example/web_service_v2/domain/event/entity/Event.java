package org.example.web_service_v2.domain.event.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.event.enums.EventStatus;
import org.example.web_service_v2.domain.event_register.entity.EventRegister;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_image", length = 255)
    private String eventImage;

    @Column(name = "event_title", length = 255)
    private String eventTitle;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(length = 255)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private EventStatus eventStatus;

    // === 연관관계 ===

    // Event N:M User (via EventRegister)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EventRegister> eventRegisters = new ArrayList<>();
}
