package com.repair_desk_backend.config;

import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.enums.Role;
import com.repair_desk_backend.repository.UtilisateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Seeds a default administrator the first time the app starts, so you always have
 * someone to log in with.
 *
 * <p>It only creates the admin if NO user named "admin" exists yet — so it is safe to
 * restart the app any number of times.</p>
 *
 * <p>Default credentials: <b>admin / admin123</b> &nbsp;(change the password in production!)</p>
 */
@Configuration
@Slf4j
public class DataSeeder {

    @Bean
    public CommandLineRunner seedDefaultAdmin(UtilisateurRepository utilisateurRepository,
                                              PasswordEncoder passwordEncoder) {
        return args -> {
            if (utilisateurRepository.findByUsername("admin123").isPresent()) {
                log.info("Default admin already exists — skipping seed.");
                return;
            }

            Utilisateur admin = new Utilisateur();
            admin.setNom("khalil");
            admin.setUsername("admin123");
            admin.setMotDePasse(passwordEncoder.encode("root")); // store the BCrypt hash, not the plain text
            admin.setRole(Role.ADMINISTRATEUR);
            admin.setActif(true);

            Utilisateur technician = new Utilisateur();
            technician.setNom("Heni");
            technician.setUsername("heni123");
            technician.setMotDePasse(passwordEncoder.encode("user")); // store the BCrypt hash, not the plain text
            technician.setRole(Role.TECHNICIEN);
            technician.setActif(true);

            utilisateurRepository.save(admin);
            utilisateurRepository.save(technician);
            log.info("Seeded default admin user (username: 'admin', password: 'admin123').");
        };
    }
}
