package com.tcpl.project.repository;

import com.tcpl.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByStatus(String status);
    List<Project> findByCompanyId(Long companyId);
    List<Project> findByStatusAndCompanyId(String status, Long companyId);
    List<Project> findByStatusAndCompanyIdAndStartDateBetween(String status, Long companyId, LocalDate start, LocalDate end);
}
