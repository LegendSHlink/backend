package org.example.web_service_v2.auth;

import org.example.web_service_v2.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);

    User findUserByEmail(String email);
}
