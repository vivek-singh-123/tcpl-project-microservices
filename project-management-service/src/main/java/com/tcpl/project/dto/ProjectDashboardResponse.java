package com.tcpl.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectDashboardResponse {
    private Long projectId;
    private String name;
    private double totalBudget;
    private double spent;
    private double progressPercent;
    private int invoicesCount;
    private int paymentsCount;
}
