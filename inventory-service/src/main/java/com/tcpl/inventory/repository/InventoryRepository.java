package com.tcpl.inventory.repository;

import com.tcpl.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByProjectIdAndMaterialId(Long projectId, Long materialId);
    List<Inventory> findByProjectId(Long projectId);
    List<Inventory> findByMaterialId(Long materialId);

    Optional<Inventory> findByMaterialIdAndLocation(Long materialId, String location);
}