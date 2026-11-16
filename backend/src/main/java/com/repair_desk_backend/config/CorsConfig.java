package com.repair_desk_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) rules.
 *
 * <p>The Angular dev server runs on http://localhost:4200 while the API runs on
 * http://localhost:8089. Browsers block cross-origin calls unless the server opts in —
 * this configuration is that opt-in.</p>
 *
 * <p>The {@link CorsConfigurationSource} bean defined here is picked up by
 * {@code SecurityConfig}.</p>
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Which front-end origins may call this API.
        config.setAllowedOrigins(List.of("http://localhost:4200","http://192.168.0.214:4200"));
        // Which HTTP methods are allowed.
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // Allow any request header (e.g. Authorization, Content-Type).
        config.setAllowedHeaders(List.of("*"));
        // Allow the browser to send credentials if ever needed.
        config.setAllowCredentials(true);

        // Apply these rules to every path.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }
}
