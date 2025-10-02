package com.tcpl.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectRequest {
    private String name;
    private String status;
    private String company;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double budget;
}
