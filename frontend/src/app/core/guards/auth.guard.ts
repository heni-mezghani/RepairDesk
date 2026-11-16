import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

/**
 * Functional route guard: blocks a route when nobody is logged in and redirects to the
 * login page.
 *
 * Use it in the routes like:
 *   { path: 'dashboard', component: Dashboard, canActivate: [authGuard] }
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  // Not logged in — go to login instead.
  return router.createUrlTree(['/login']);
};
