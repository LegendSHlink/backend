package org.example.web_service_v2.domain.field;

import org.example.web_service_v2.domain.field.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<Field> findById(Long id);
}
