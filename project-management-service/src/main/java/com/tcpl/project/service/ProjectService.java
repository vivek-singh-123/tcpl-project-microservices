package com.tcpl.project.service;

// Naye imports add kiye gaye hain
import com.tcpl.project.dto.InvoiceDTO;
import com.tcpl.project.dto.ProjectDashboardResponse;
import com.tcpl.project.model.Project;
import com.tcpl.project.repository.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final RestTemplate restTemplate; // Invoice/Payment repo ki jagah RestTemplate

    // Constructor ko update kiya gaya hai
    public ProjectService(ProjectRepository projectRepo, RestTemplate restTemplate) {
        this.projectRepo = projectRepo;
        this.restTemplate = restTemplate;
    }

    // --- 1. List projects with optional filters (NO CHANGE) ---
    public List<Project> listProjects(String status, Long companyId, LocalDate start, LocalDate end) {
        if (status != null && companyId != null && start != null && end != null) {
            return projectRepo.findByStatusAndCompanyIdAndStartDateBetween(status, companyId, start, end);
        } else if (status != null && companyId != null) {
            return projectRepo.findByStatusAndCompanyId(status, companyId);
        } else if (companyId != null) {
            return projectRepo.findByCompanyId(companyId);
        } else if (status != null) {
            return projectRepo.findByStatus(status);
        } else {
            return projectRepo.findAll();
        }
    }

    // --- 2. Create project (NO CHANGE) ---
    public Project createProject(Project project) {
        // Best practice ke liye yahan @Transactional add kar sakte hain
        return projectRepo.save(project);
    }

    // --- 3. Get project details (NO CHANGE) ---
    public Project getProject(Long id) {
        return projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    // --- 4. Update project (NO CHANGE) ---
    public Project updateProject(Long id, Project updated) {
        // Best practice ke liye yahan @Transactional add kar sakte hain
        Project existing = getProject(id);
        existing.setName(updated.getName());
        existing.setCompanyId(updated.getCompanyId());
        existing.setStatus(updated.getStatus());
        existing.setBudget(updated.getBudget());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        return projectRepo.save(existing);
    }

    // --- 5. Get dashboard KPIs (YEH METHOD POORA UPDATE HUA HAI) ---
    public ProjectDashboardResponse getProjectDashboard(Long projectId) {
        Project project = getProject(projectId);

        // Use the SERVICE NAME registered in Eureka. Case-insensitive, but uppercase is standard.
        String billingServiceUrl = "http://BILLING-SERVICE/api/v1/invoices?projectId=" + projectId;

        List<InvoiceDTO> invoices = Collections.emptyList();
        try {
            // API call karke response ko InvoiceDTO ke array mein convert karein
            ResponseEntity<InvoiceDTO[]> response = restTemplate.getForEntity(billingServiceUrl, InvoiceDTO[].class);
            if (response.getBody() != null) {
                invoices = Arrays.asList(response.getBody());
            }
        } catch (Exception e) {
            // Agar billing-service down hai ya koi error aata hai, toh hum aage badh sakte hain
            System.err.println("Could not fetch invoices from billing-service: " + e.getMessage());
        }

        // STEP B: Ab DTOs se KPIs calculate karein (direct repository access ki jagah)
        double spent = invoices.stream()
                .mapToDouble(InvoiceDTO::getTotalPaid)
                .sum();

        int invoicesCount = invoices.size();

        int paymentsCount = invoices.stream()
                .filter(inv -> inv.getPayments() != null)
                .mapToInt(inv -> inv.getPayments().size())
                .sum();

        // Progress percentage capped at 100%
        // Note: Yahan budget ke liye BigDecimal use karna best practice hai
        double budget = (project.getBudget() != null) ? project.getBudget() : 0.0;
        int progressPercent = (budget > 0)
                ? (int) Math.min((spent / budget) * 100, 100)
                : 0;

        return new ProjectDashboardResponse(
                project.getId(),
                project.getName(),
                budget,
                spent,
                progressPercent,
                invoicesCount,
                paymentsCount
        );
    }
}