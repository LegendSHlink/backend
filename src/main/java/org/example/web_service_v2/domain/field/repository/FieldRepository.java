package org.example.web_service_v2.domain.field.repository;

import org.example.web_service_v2.domain.field.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
    Optional<Field> findByName(String name);
    boolean existsByName(String name);
}
