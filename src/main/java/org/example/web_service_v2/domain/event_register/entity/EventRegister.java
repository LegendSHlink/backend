package org.example.web_service_v2.domain.event_register.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.web_service_v2.domain.event.entity.Event;
import org.example.web_service_v2.domain.event_register.enums.RegisterStatus;
import org.example.web_service_v2.domain.users.entity.User;

@Entity
@Table(name = "event_register")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private RegisterStatus registerStatus;
}
