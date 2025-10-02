package com.tcpl.inventory.service;

import com.tcpl.inventory.dto.InventoryMoveRequest;
import com.tcpl.inventory.dto.InventoryResponse;
import com.tcpl.inventory.model.Inventory;
import com.tcpl.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepo;

    // Helper method to map Entity to DTO
    private InventoryResponse mapToResponse(Inventory entity) {
        InventoryResponse dto = new InventoryResponse();
        dto.setId(entity.getId());
        dto.setProjectId(entity.getProjectId());
        dto.setMaterialId(entity.getMaterialId());
        dto.setQuantity(entity.getQuantity());
        dto.setLocation(entity.getLocation());
        return dto;
    }

    public List<InventoryResponse> listInventory(Long projectId, Long materialId) {
        List<Inventory> inventories;
        if (projectId != null && materialId != null) {
            inventories = inventoryRepo.findByProjectIdAndMaterialId(projectId, materialId);
        } else if (projectId != null) {
            inventories = inventoryRepo.findByProjectId(projectId);
        } else if (materialId != null) {
            inventories = inventoryRepo.findByMaterialId(materialId);
        } else {
            inventories = inventoryRepo.findAll();
        }
        return inventories.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public InventoryResponse moveInventory(InventoryMoveRequest req) {
        Inventory fromInv = inventoryRepo.findByMaterialIdAndLocation(req.getMaterialId(), req.getFrom())
                .orElseThrow(() -> new RuntimeException("No inventory at source location for material: " + req.getMaterialId()));

        if (fromInv.getQuantity().compareTo(req.getQty()) < 0) {
            throw new RuntimeException("Insufficient quantity at source location: " + req.getFrom());
        }
        fromInv.setQuantity(fromInv.getQuantity().subtract(req.getQty()));

        Inventory toInv = inventoryRepo.findByMaterialIdAndLocation(req.getMaterialId(), req.getTo())
                .orElse(new Inventory(null, req.getProjectId(), req.getMaterialId(), BigDecimal.ZERO, req.getTo()));

        toInv.setQuantity(toInv.getQuantity().add(req.getQty()));

        inventoryRepo.save(fromInv);
        Inventory savedToInv = inventoryRepo.save(toInv);
        return mapToResponse(savedToInv);
    }
}