package com.repair_desk_backend.dto;

import com.repair_desk_backend.entity.AuditLog;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuditHistoryResponse {

    private String username;
    private List<AuditLog> logs;
}