package org.example.web_service_v2.domain.profiles.repositories;

import org.example.web_service_v2.domain.profiles.entity.Profile;
import org.example.web_service_v2.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser(User user);

    Profile findProfileByUser(User user);

    Profile findByUserId(Long userId);

    Long user(User user);

    Profile findProfileById(Long id);
}
