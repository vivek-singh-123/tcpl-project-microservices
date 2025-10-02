package com.tcpl.procurement.repository;

import com.tcpl.procurement.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT po FROM PurchaseOrder po WHERE po.requisition.projectId = :projectId")
    List<PurchaseOrder> findByProjectId(@Param("projectId") Long projectId);
}
