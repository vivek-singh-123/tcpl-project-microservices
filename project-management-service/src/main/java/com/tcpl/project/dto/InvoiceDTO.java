package com.tcpl.project.dto;

import lombok.Data;
import java.util.List;

@Data
public class InvoiceDTO {
    // Humein in do fields ki zaroorat hai dashboard ke liye
    private double totalPaid;
    private List<PaymentDTO> payments;
}