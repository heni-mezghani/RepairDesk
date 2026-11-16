package com.repair_desk_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Runs once for every HTTP request. If the request carries a valid
 * "Authorization: Bearer &lt;token&gt;" header, this filter authenticates the user for
 * the duration of that request (by putting them into the Spring SecurityContext).
 *
 * <p>If there is no token, or it is invalid, the filter simply does nothing and lets
 * the request continue — Spring Security will then reject it later if the endpoint is
 * protected.</p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        // Authenticate only if: we have a token, it is valid, and nobody is authenticated yet.
        if (token != null
                && jwtUtil.isTokenValid(token)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtUtil.extractUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!userDetails.isEnabled()) {

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write("""
                    {
                      "status":403,
                      "message":"Ce compte a été désactivé. Contactez l'administrateur."
                    }
                """);
                return;
            }

            // Build an "authenticated" token (no password needed — the JWT already proved identity).
            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Always continue the chain.
        filterChain.doFilter(request, response);
    }

    /** Pull the raw token out of the "Authorization: Bearer ..." header, or return null. */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        if (header != null && header.startsWith(PREFIX)) {
            return header.substring(PREFIX.length());
        }
        return null;
    }
}
