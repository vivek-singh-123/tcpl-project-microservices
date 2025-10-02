package com.tcpl.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadResponse {

    private Long id;
    private String fileName;
    private String fileType;
    private String description;
    private String downloadUrl;
}