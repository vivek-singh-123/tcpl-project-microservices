package com.tcpl.procurement.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseOrderResponse {

    private Long id;
    private String poNo;
    private BigDecimal amount;
    private String status;
    private LocalDate dueDate;
    private Long supplierId;
    private RequisitionSummaryDTO requisition;

    // Ek chhota nested DTO sirf zaroori requisition details ke liye
    @Data
    public static class RequisitionSummaryDTO {
        private Long id;
        private Long projectId;
    }
}