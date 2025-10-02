package com.tcpl.procurement.service;

import com.tcpl.procurement.dto.PurchaseOrderResponse;
import com.tcpl.procurement.dto.PurchaseRequisitionRequest;
import com.tcpl.procurement.model.PurchaseOrder;
import com.tcpl.procurement.model.PurchaseRequisition;
import com.tcpl.procurement.repository.PurchaseOrderRepository;
import com.tcpl.procurement.repository.PurchaseRequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcurementService {

    private final PurchaseRequisitionRepository requisitionRepo;
    private final PurchaseOrderRepository orderRepo;

    // Helper method to convert PurchaseOrder Entity to DTO
    private PurchaseOrderResponse mapPoToResponse(PurchaseOrder entity) {
        PurchaseOrderResponse dto = new PurchaseOrderResponse();
        dto.setId(entity.getId());
        dto.setPoNo(entity.getPoNo());
        // Assuming amount is BigDecimal in model
        dto.setAmount(entity.getAmount() != null ? entity.getAmount() : BigDecimal.ZERO);
        dto.setStatus(entity.getStatus());
        if (entity.getDueDate() != null) {
            dto.setDueDate(entity.getDueDate().toLocalDate());
        }
        dto.setSupplierId(entity.getSupplierId());

        if (entity.getRequisition() != null) {
            PurchaseOrderResponse.RequisitionSummaryDTO reqDto = new PurchaseOrderResponse.RequisitionSummaryDTO();
            reqDto.setId(entity.getRequisition().getId());
            reqDto.setProjectId(entity.getRequisition().getProjectId());
            dto.setRequisition(reqDto);
        }
        return dto;
    }

    @Transactional
    public PurchaseRequisition createRequisition(PurchaseRequisitionRequest request) {
        PurchaseRequisition requisition = new PurchaseRequisition();
        requisition.setProjectId(request.getProjectId());
        // Assuming reqBy is a Long ID
        requisition.setReqBy(request.getReqBy());
        requisition.setTotalAmount(request.getTotalAmount());
        return requisitionRepo.save(requisition);
    }

    public List<PurchaseOrderResponse> getOrders(Long projectId) {
        return orderRepo.findByProjectId(projectId)
                .stream()
                .map(this::mapPoToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PurchaseOrderResponse approveOrder(Long id) {
        PurchaseOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Best practice: Enum use karein
        order.setStatus("APPROVED");
        PurchaseOrder savedOrder = orderRepo.save(order);
        return mapPoToResponse(savedOrder);
    }
}