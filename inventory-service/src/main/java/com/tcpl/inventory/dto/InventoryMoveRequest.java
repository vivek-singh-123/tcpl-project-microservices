package com.tcpl.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InventoryMoveRequest {
    private Long projectId;
    private Long materialId;
    private BigDecimal qty;
    private String from;
    private String to;
    private String refType;
    private Long refId;
}