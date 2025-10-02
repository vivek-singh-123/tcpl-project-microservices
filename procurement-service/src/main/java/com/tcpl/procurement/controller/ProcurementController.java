package com.tcpl.procurement.controller;

import com.tcpl.procurement.dto.PurchaseOrderResponse;
import com.tcpl.procurement.dto.PurchaseRequisitionRequest;
import com.tcpl.procurement.model.PurchaseRequisition;
import com.tcpl.procurement.service.ProcurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcurementController {

    private final ProcurementService procurementService;

    @PostMapping("/purchase-requisitions")
    public ResponseEntity<PurchaseRequisition> createRequisition(@RequestBody PurchaseRequisitionRequest request) {
        PurchaseRequisition savedRequisition = procurementService.createRequisition(request);
        return new ResponseEntity<>(savedRequisition, HttpStatus.CREATED);
    }

    @GetMapping("/purchase-orders")
    public ResponseEntity<List<PurchaseOrderResponse>> getOrders(@RequestParam Long projectId) {
        List<PurchaseOrderResponse> orders = procurementService.getOrders(projectId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/purchase-orders/{id}/approve")
    public ResponseEntity<PurchaseOrderResponse> approveOrder(@PathVariable Long id) {
        PurchaseOrderResponse updatedOrder = procurementService.approveOrder(id);
        return ResponseEntity.ok(updatedOrder);
    }
}