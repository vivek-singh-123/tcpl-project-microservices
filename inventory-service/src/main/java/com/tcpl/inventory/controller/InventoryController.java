package com.tcpl.inventory.controller;

import com.tcpl.inventory.dto.InventoryMoveRequest;
import com.tcpl.inventory.dto.InventoryResponse;
import com.tcpl.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> listInventory(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long materialId
    ) {
        return ResponseEntity.ok(inventoryService.listInventory(projectId, materialId));
    }

    @PostMapping("/move")
    public ResponseEntity<InventoryResponse> moveInventory(@RequestBody InventoryMoveRequest req) {
        InventoryResponse moved = inventoryService.moveInventory(req);
        return ResponseEntity.ok(moved);
    }
}