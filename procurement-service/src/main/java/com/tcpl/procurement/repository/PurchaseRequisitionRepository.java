package com.tcpl.procurement.repository;

import com.tcpl.procurement.model.PurchaseRequisition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisition, Long> {
}
