package com.repair_desk_backend.dto;

import com.repair_desk_backend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response returned by POST /api/auth/login: the signed JWT plus a few non-sensitive
 * fields about the user, so the front-end can show the name and adapt the UI to the role
 * without making a second call.
 *
 * <p>This is one of the only TWO DTOs allowed in the whole project (the other is
 * {@link LoginRequest}). We never expose the password hash here.</p>
 */
@Data
@AllArgsConstructor
public class LoginResponse {

    /** The JWT the front-end must send back on every future request. */
    private String token;

    private Long id;
    private String nom;
    private String username;
    private Role role;
}
