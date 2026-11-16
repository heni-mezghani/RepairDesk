package com.repair_desk_backend.security;

import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.repository.UtilisateurRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Tells Spring Security how to load a user from OUR database.
 *
 * <p>Spring Security never touches our {@link Utilisateur} entity directly — it works
 * with its own {@link UserDetails} type. This class is the bridge: given a username it
 * loads the entity and converts it into a {@link UserDetails}.</p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        // Spring Security expects authorities prefixed with "ROLE_".
        // role ADMINISTRATEUR  ->  authority "ROLE_ADMINISTRATEUR"  ->  hasRole('ADMINISTRATEUR')
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()));

        return User.builder()
                .username(utilisateur.getUsername())
                .password(utilisateur.getMotDePasse()) // already BCrypt-hashed in the DB
                .authorities(authorities)
                .disabled(!utilisateur.getActif())      // a disabled user cannot authenticate
                .build();
    }
}
