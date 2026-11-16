package com.repair_desk_backend.repository;

import com.repair_desk_backend.entity.Piece;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PieceRepository extends JpaRepository<Piece, Long> {
}