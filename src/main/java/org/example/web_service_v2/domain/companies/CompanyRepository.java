package org.example.web_service_v2.domain.companies;

import org.example.web_service_v2.domain.companies.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
