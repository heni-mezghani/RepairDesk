package com.repair_desk_backend.repository;

import com.repair_desk_backend.entity.FicheReception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FicheReceptionRepository extends JpaRepository<FicheReception, Long> {
    List<FicheReception> findByClient_Id(Long clientId);
    List<FicheReception> findByDateReception(LocalDate date);

    @Query("SELECT COALESCE(MAX(f.id), 0) FROM FicheReception f")
    Long findMaxId();
}