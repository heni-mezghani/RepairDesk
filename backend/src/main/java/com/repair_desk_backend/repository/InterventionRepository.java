package com.repair_desk_backend.repository;

import com.repair_desk_backend.entity.Intervention;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    List<Intervention> findByMachine_IdOrderByDateAsc(Long machineId);
}