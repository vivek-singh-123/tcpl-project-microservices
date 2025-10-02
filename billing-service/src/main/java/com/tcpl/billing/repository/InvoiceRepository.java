package com.tcpl.billing.repository;

import com.tcpl.billing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Add this method to fetch invoices by project ID
    List<Invoice> findByProjectId(Long projectId);
}
