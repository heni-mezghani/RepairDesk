package com.repair_desk_backend.repository;

import com.repair_desk_backend.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MachineRepository extends JpaRepository<Machine, Long> {
}
