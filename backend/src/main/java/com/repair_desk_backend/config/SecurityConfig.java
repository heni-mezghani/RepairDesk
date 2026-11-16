package com.repair_desk_backend.config;

import com.repair_desk_backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Central security configuration.
 *
 * <p>What this sets up:</p>
 * <ul>
 *   <li>Stateless sessions (no server-side session — every request is authenticated by its JWT).</li>
 *   <li>BCrypt password hashing.</li>
 *   <li>CORS enabled (the actual rules live in {@link CorsConfig}).</li>
 *   <li>{@code /api/auth/**} is public; everything else needs a valid token.</li>
 *   <li>Our {@link JwtAuthenticationFilter} runs before the username/password filter.</li>
 * </ul>
 *
 * <p>{@code @EnableMethodSecurity} lets the intern protect individual methods with
 * {@code @PreAuthorize("hasRole('ADMINISTRATEUR')")} when needed.</p>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS uses the rules defined in CorsConfig.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // CSRF protection is for browser form/cookie sessions. We are a stateless
                // token API, so we disable it.
                .csrf(AbstractHttpConfigurer::disable)
                // No HTTP session is created or used — authentication comes from the JWT.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Who can reach what:
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // login is open to everyone
                        .anyRequest().authenticated()                // everything else needs a token
                )
                // Check the JWT before Spring's usual username/password processing.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** BCrypt is the recommended one-way hash for passwords. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes Spring's {@link AuthenticationManager} so {@code AuthController} can use it
     * to verify a username + password at login time.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
