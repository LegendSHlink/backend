package org.example.web_service_v2.domain.field.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FieldResponse {
    private Long id;
    private String name;
}
