package com.repair_desk_backend.repository;

import com.repair_desk_backend.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data access for {@link Utilisateur}.
 *
 * <p>By extending {@link JpaRepository} we get findAll / findById / save / delete /
 * count … for free. We only declare the extra query we need for authentication.</p>
 */
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    /**
     * Look a user up by their login name. Spring Data generates the query from the
     * method name. Returns an {@link Optional} so the caller must handle "not found".
     */
    Optional<Utilisateur> findByUsername(String username);

    boolean existsByUsername(String username);
}
