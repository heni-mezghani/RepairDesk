import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

import { API_BASE_URL } from '../config/api.config';
import { LoginRequest, LoginResponse, Role, User } from '../models/user.model';

/** localStorage keys — kept in one place to avoid typos. */
const TOKEN_KEY = 'repairdesk_token';
const USER_KEY = 'repairdesk_user';

/**
 * Single source of truth for authentication on the front-end.
 *
 * Responsibilities:
 *  - call the backend to log in,
 *  - persist the JWT + user in localStorage (so a page refresh keeps you logged in),
 *  - expose the current user as a signal that components/guards can read.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  /**
   * The currently logged-in user, or null. We seed it from localStorage so a refresh
   * does not log the user out. Components read this with `authService.currentUser()`.
   */
  readonly currentUser = signal<User | null>(this.readUserFromStorage());

  /** Convenience signals derived from currentUser. */
  readonly isLoggedIn = computed(() => this.currentUser() !== null);
  readonly isAdmin = computed(() => this.currentUser()?.role === 'ADMINISTRATEUR');

  /**
   * Authenticate against POST /api/auth/login. On success we store the token + user
   * and update the signal; the component just subscribes and redirects.
   */
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API_BASE_URL}/auth/login`, credentials).pipe(
      tap((res) => this.storeSession(res)),
    );
  }

  /** Clear the session and send the user back to the login screen. */
  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  /** The raw JWT, or null. Used by the HTTP interceptor to set the Authorization header. */
  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  /** True if the current user has the given role. */
  hasRole(role: Role): boolean {
    return this.currentUser()?.role === role;
  }

  // ----------------------------------------------------------------------------------
  // Private helpers
  // ----------------------------------------------------------------------------------

  /** Persist the login response and update the signal. */
  private storeSession(res: LoginResponse): void {
    const user: User = { id: res.id, nom: res.nom, username: res.username, role: res.role };
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this.currentUser.set(user);
  }

  /** Read the saved user from localStorage when the app boots (returns null if none/corrupt). */
  private readUserFromStorage(): User | null {
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) {
      return null;
    }
    try {
      return JSON.parse(raw) as User;
    } catch {
      // Corrupt value — clean it up.
      localStorage.removeItem(USER_KEY);
      return null;
    }
  }
}
