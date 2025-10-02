package com.tcpl.project.service;

import com.tcpl.project.model.Document;
import com.tcpl.project.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document upload(Long projectId, MultipartFile file, String description) throws Exception {
        Document doc = new Document();
        doc.setProjectId(projectId);
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setData(file.getBytes());
        doc.setDescription(description);

        return documentRepository.save(doc);
    }

    public Document download(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }
}
