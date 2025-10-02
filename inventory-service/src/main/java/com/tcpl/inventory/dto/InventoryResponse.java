package com.tcpl.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InventoryResponse {
    private Long id;
    private Long projectId;
    private Long materialId;
    private BigDecimal quantity;
    private String location;
}