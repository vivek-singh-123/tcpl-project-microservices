package com.tcpl.billing.controller;

import com.tcpl.billing.dto.InvoiceRequest;
import com.tcpl.billing.dto.PaymentRequest;
import com.tcpl.billing.model.Invoice;
import com.tcpl.billing.service.BillingService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    //API endpoint
    @GetMapping
    public ResponseEntity<List<Invoice>> getInvoicesByProject(@RequestParam(required = false) Long projectId) {
        if (projectId == null) {
            // Handle the case where no project ID is given, maybe return all invoices or an error
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(billingService.findInvoicesByProjectId(projectId));
    }


    // --- 1. Create Invoice ---
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody InvoiceRequest req) {
        Invoice saved = billingService.createInvoice(req);
        return ResponseEntity.status(201).body(saved);
    }

    // --- 2. Download Invoice PDF ---
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
        // Generate PDF bytes from service
        byte[] pdfBytes = billingService.generateInvoicePdfBytes(id);

        // Get invoice to use invoice number for filename
        Invoice invoice = billingService.findInvoiceById(id);
        String filename = invoice.getInvoiceNo() + ".pdf";

        // Compute a simple ETag from PDF content
        String eTag = Base64.getEncoder().encodeToString(
                java.util.Arrays.copyOfRange(pdfBytes, 0, Math.min(pdfBytes.length, 16))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setETag("\"" + eTag + "\""); // Optional: quotes per HTTP spec

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // --- 3. Record Payment ---
    @PostMapping("/{id}/record-payment")
    public ResponseEntity<Invoice> recordPayment(@PathVariable Long id, @RequestBody PaymentRequest req) {
        Invoice updated = billingService.recordPayment(id, req);
        return ResponseEntity.ok(updated);
    }
}
