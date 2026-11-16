package com.repair_desk_backend.repository;

import com.repair_desk_backend.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * EXAMPLE TEMPLATE — intern: replicate this pattern for other entities.
 *
 * <p>The REPOSITORY layer of the worked example. Extending {@link JpaRepository} gives
 * you all the standard CRUD methods (findAll, findById, save, deleteById, count, …) with
 * zero code.</p>
 *
 * <p>Need a custom query? Just declare a method and Spring Data writes it from the name —
 * the {@code findByNomContainingIgnoreCase} below is a ready example of a search query.</p>
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
