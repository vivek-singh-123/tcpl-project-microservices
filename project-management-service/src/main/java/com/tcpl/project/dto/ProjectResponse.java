package com.tcpl.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private Long companyId;
    private String status;
    private Double budget;
    private LocalDate startDate;
    private LocalDate endDate;
}
