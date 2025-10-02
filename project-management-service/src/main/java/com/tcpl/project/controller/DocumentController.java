package com.tcpl.project.controller;

import com.tcpl.project.model.Document;
import com.tcpl.project.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    // Upload Document
    @PostMapping("/projects/{id}/documents")
    public Document uploadDocument(@PathVariable Long id,
                                   @RequestParam("file") MultipartFile file,
                                   @RequestParam("description") String description) throws Exception {
        return documentService.upload(id, file, description);
    }

    // Download Document
    @GetMapping("/documents/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document doc = documentService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getFileName() + "\"")
                .body(doc.getData());
    }
}
