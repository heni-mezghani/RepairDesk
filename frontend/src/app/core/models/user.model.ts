/**
 * The two roles a user can have. Must match the backend enum
 * `com.repair_desk_backend.enums.Role`.
 */
export type Role = 'TECHNICIEN' | 'ADMINISTRATEUR';

/**
 * The authenticated user as the front-end knows it. This mirrors the non-sensitive
 * fields the backend returns from POST /api/auth/login (see `LoginResponse`).
 */
export interface User {
  id: number;
  nom: string;
  username: string;
  role: Role;
}

/** Body we send to POST /api/auth/login. */
export interface LoginRequest {
  username: string;
  password: string;
}

/** Body we receive from POST /api/auth/login: the JWT plus the user's info. */
export interface LoginResponse {
  token: string;
  id: number;
  nom: string;
  username: string;
  role: Role;
}


export interface Utilisateur {
  id: number;
  nom: string;
  username: string;
  role: Role;
  actif: boolean;
}