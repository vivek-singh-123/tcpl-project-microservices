package com.tcpl.billing.service;

import com.itextpdf.layout.properties.UnitValue;
import com.tcpl.billing.dto.InvoiceRequest;
import com.tcpl.billing.dto.PaymentRequest;
import com.tcpl.billing.model.Invoice;
import com.tcpl.billing.model.InvoiceItem;
import com.tcpl.billing.model.Payment;
import com.tcpl.billing.repository.InvoiceRepository;
import com.tcpl.billing.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

@Service
public class BillingService {

    private final InvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;

    public BillingService(InvoiceRepository invoiceRepo, PaymentRepository paymentRepo) {
        this.invoiceRepo = invoiceRepo;
        this.paymentRepo = paymentRepo;
    }

    // --- Create Invoice ---
    public Invoice createInvoice(InvoiceRequest req) {
        Invoice invoice = new Invoice();
        invoice.setProjectId(req.getProjectId());
        invoice.setInvoiceNo("INV-" + UUID.randomUUID().toString().substring(0, 6));
        invoice.setStatus("draft");

        // Convert java.util.Date to LocalDate
        if (req.getDueDate() != null) {
            LocalDate localDueDate = req.getDueDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            invoice.setDueDate(localDueDate);
        }

        double totalAmount = 0.0;
        double totalGst = 0.0;

        if (req.getItems() != null) {
            for (InvoiceRequest.ItemDto dto : req.getItems()) {
                InvoiceItem item = new InvoiceItem();
                item.setDescription(dto.getDescription());
                item.setQuantity(dto.getQuantity());
                item.setRate(dto.getRate());
                item.setAmount(dto.getAmount());
                item.setHsnCode(dto.getHsnCode());
                item.setGstPercent(dto.getGstPercent());

                invoice.addItem(item);

                totalAmount += dto.getAmount();
                totalGst += dto.getAmount() * dto.getGstPercent() / 100;
            }
        }

        invoice.setAmount(totalAmount);
        invoice.setGstAmount(totalGst);

        return invoiceRepo.save(invoice);
    }

    // --- Record Payment ---
    public Invoice recordPayment(Long invoiceId, PaymentRequest req) {
        Invoice invoice = invoiceRepo.findById(invoiceId).orElseThrow(() ->
                new RuntimeException("Invoice not found with id " + invoiceId)
        );

        double outstanding = invoice.getAmount() - invoice.getTotalPaid();
        double paymentAmount = Math.min(req.getAmount(), outstanding);

        Payment payment = new Payment();
        payment.setAmount(paymentAmount);
        payment.setMode(req.getMode());
        payment.setPaidOn(req.getPaidOn());
        payment.setRefNo(req.getRefNo());
        payment.setRemarks(req.getRemarks());
        payment.setInvoice(invoice);

        paymentRepo.save(payment);

        invoice.setTotalPaid(invoice.getTotalPaid() + paymentAmount);
        if (invoice.getTotalPaid() >= invoice.getAmount()) {
            invoice.setStatus("paid");
        }

        return invoiceRepo.save(invoice);
    }

    // --- Fetch invoice by ID ---
    public Invoice findInvoiceById(Long id) {
        return invoiceRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Invoice not found with id " + id)
        );
    }

    // --- Generate PDF bytes for invoice download ---
    public byte[] generateInvoicePdfBytes(Long invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            // Header
            doc.add(new Paragraph("Invoice").setBold().setFontSize(16));
            doc.add(new Paragraph("Invoice No: " + invoice.getInvoiceNo()));
            doc.add(new Paragraph("Project ID: " + invoice.getProjectId()));
            if (invoice.getDueDate() != null) {
                doc.add(new Paragraph("Due Date: " + invoice.getDueDate().format(DateTimeFormatter.ISO_DATE)));
            }
            doc.add(new Paragraph(" "));

            // Items table
            List<InvoiceItem> items = invoice.getItems();
            if (items != null && !items.isEmpty()) {
                // Correct Table creation with float literals
                Table table = new Table(UnitValue.createPercentArray(new float[]{40f, 15f, 15f, 15f, 15f}))
                        .useAllAvailableWidth();
                table.addHeaderCell("Description");
                table.addHeaderCell("Qty");
                table.addHeaderCell("Rate");
                table.addHeaderCell("Amount");
                table.addHeaderCell("GST %");

                for (InvoiceItem it : items) {
                    table.addCell(it.getDescription());
                    table.addCell(String.valueOf(it.getQuantity()));
                    table.addCell(String.valueOf(it.getRate()));
                    table.addCell(String.valueOf(it.getAmount()));
                    table.addCell(String.valueOf(it.getGstPercent()));
                }
                doc.add(table);
            }

            // Totals
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph(String.format("Subtotal: %.2f", invoice.getAmount())));
            doc.add(new Paragraph(String.format("GST: %.2f", invoice.getGstAmount())));
            doc.add(new Paragraph(String.format("Total Paid: %.2f", invoice.getTotalPaid())));
            doc.add(new Paragraph(String.format("Status: %s", invoice.getStatus())));

            doc.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    public List<Invoice> findInvoicesByProjectId(Long projectId) {
        return invoiceRepo.findByProjectId(projectId);
    }
}
