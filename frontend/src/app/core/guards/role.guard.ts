import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

/**
 * Functional route guard: allows the route only for ADMINISTRATEUR users.
 *
 * Pair it with `authGuard` so unauthenticated users go to /login while logged-in
 * non-admins are sent to the dashboard:
 *   {
 *     path: 'admin',
 *     component: AdminPage,
 *     canActivate: [authGuard, adminGuard]
 *   }
 */
export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAdmin()) {
    return true;
  }

  // Logged in but not an admin -> bounce to the dashboard.
  // (If they are not logged in at all, authGuard will have redirected to /login first.)
  return router.createUrlTree(['/dashboard']);
};
