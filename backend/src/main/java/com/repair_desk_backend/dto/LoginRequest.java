package com.repair_desk_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Body of POST /api/auth/login.
 *
 * <p>This is one of the only TWO DTOs allowed in the whole project (the other is
 * {@link LoginResponse}). Every other layer works directly with entities.</p>
 *
 * <p>The {@code @NotBlank} annotations are validated automatically because the
 * controller method marks this parameter with {@code @Valid}. A blank field produces a
 * clean 400 error (see {@code GlobalExceptionHandler}).</p>
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
