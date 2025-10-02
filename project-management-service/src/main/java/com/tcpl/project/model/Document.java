package com.tcpl.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id")
    private Long projectId;

    private String fileName;
    private String fileType;

    @Lob
    private byte[] data;

    private String description;
}
