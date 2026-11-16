package com.repair_desk_backend.controller;

import com.repair_desk_backend.dto.LoginRequest;
import com.repair_desk_backend.dto.LoginResponse;
import com.repair_desk_backend.entity.Utilisateur;
import com.repair_desk_backend.repository.UtilisateurRepository;
import com.repair_desk_backend.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication endpoints. This is the only controller that is publicly reachable
 * (see {@code SecurityConfig} — everything under {@code /api/auth/**} is permitted).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository utilisateurRepository;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UtilisateurRepository utilisateurRepository,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Verify the username + password and, if correct, return a fresh JWT plus the
     * user's basic info.
     *
     * <p>If the credentials are wrong, {@code authenticationManager.authenticate(...)}
     * throws {@code BadCredentialsException}, which {@code GlobalExceptionHandler}
     * turns into a clean 401 response — we don't handle it here.</p>
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // 1) Let Spring Security check the credentials (uses CustomUserDetailsService + BCrypt).
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // 2) Credentials are valid — load the entity so we can return its info.
        //    (We just authenticated, so this user is guaranteed to exist.)
        Utilisateur utilisateur = utilisateurRepository.findByUsername(request.getUsername())
                .orElseThrow(); // unreachable in practice

        // 3) Issue a token and build the response.
        String token = jwtUtil.generateToken(utilisateur.getUsername());

        LoginResponse response = new LoginResponse(
                token,
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getUsername(),
                utilisateur.getRole());

        return ResponseEntity.ok(response);
    }
}
