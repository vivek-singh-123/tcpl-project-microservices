package com.tcpl.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {
    private Long projectId;
    private String invoiceType;
    private Long milestoneId;
    private Date billingPeriodStart;
    private Date billingPeriodEnd;
    private Date dueDate;
    private boolean sendToClient;
    private List<ItemDto> items;

    private double amount;
    private double gstAmount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemDto {
        private String description;
        private double quantity;
        private double rate;
        private double amount;
        private String hsnCode;
        private double gstPercent;
    }
}
