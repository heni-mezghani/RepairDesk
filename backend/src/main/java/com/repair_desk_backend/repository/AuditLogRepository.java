package com.repair_desk_backend.repository;

import com.repair_desk_backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTechnicien_IdOrderByDateDesc(Long technicienId);
}