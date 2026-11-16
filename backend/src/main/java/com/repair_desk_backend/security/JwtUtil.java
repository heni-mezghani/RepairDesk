package com.repair_desk_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Small helper that creates and reads JWT tokens (HS256).
 *
 * <p>The secret and expiration are read from {@code application.properties}:</p>
 * <pre>
 *   app.jwt.secret=...           (Base64-encoded signing key)
 *   app.jwt.expiration-ms=86400000
 * </pre>
 */
@Component
public class JwtUtil {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtUtil(@Value("${app.jwt.secret}") String secret,
                   @Value("${app.jwt.expiration-ms}") long expirationMs) {
        // The secret in application.properties is Base64-encoded, so decode it first.
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMs = expirationMs;
    }

    /**
     * Build a signed token whose "subject" is the username.
     * The token is valid for {@code app.jwt.expiration-ms} milliseconds.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)        // who the token belongs to
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)     // HS256, inferred from the key length
                .compact();
    }

    /** Read the username (subject) out of a token. Throws if the token is invalid/expired. */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * @return true if the token's signature is valid and it has not expired.
     *         Any parsing error (bad signature, malformed, expired) is treated as invalid.
     */
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token); // throws if signature is bad or token is expired
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Parse + verify the token and return its claims (payload). */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
