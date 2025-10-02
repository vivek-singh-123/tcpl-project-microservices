package com.tcpl.project.controller;

import com.tcpl.project.dto.ProjectDashboardResponse;
import com.tcpl.project.dto.ProjectResponse;
import com.tcpl.project.model.Project;
import com.tcpl.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // Helper: map entity -> DTO
    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getCompanyId(),
                project.getStatus(),
                project.getBudget(),
                project.getStartDate(),
                project.getEndDate()
        );
    }

    // --- 1. List projects ---
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> listProjects(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<Project> projects = projectService.listProjects(status, companyId, startDate, endDate);
        List<ProjectResponse> response = projects.stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // --- 2. Create project ---
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody Project project) {
        Project saved = projectService.createProject(project);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    // --- 3. Get project details ---
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id) {
        Project project = projectService.getProject(id);
        return ResponseEntity.ok(toResponse(project));
    }

    // --- 4. Update project ---
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @RequestBody Project project) {
        Project updated = projectService.updateProject(id, project);
        return ResponseEntity.ok(toResponse(updated));
    }

    // --- 5. Dashboard KPIs ---
    @GetMapping("/{id}/dashboard")
    public ResponseEntity<ProjectDashboardResponse> getProjectDashboard(@PathVariable Long id) {
        ProjectDashboardResponse dashboard = projectService.getProjectDashboard(id);
        return ResponseEntity.ok(dashboard);
    }
}
