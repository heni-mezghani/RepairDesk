import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { AuthService } from '../services/auth.service';

/**
 * Functional HTTP interceptor (the modern Angular style).
 *
 * Attaches "Authorization: Bearer <token>" to every outgoing request when we have a
 * token, so individual services never have to think about it.
 *
 * Registered in app.config.ts via `provideHttpClient(withInterceptors([authInterceptor]))`.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();

  // No token (e.g. the login call itself) — send the request unchanged.
  if (!token) {
    return next(req);
  }

  // Clone the request and add the header (HttpRequest is immutable).
  const authReq = req.clone({
    setHeaders: { Authorization: `Bearer ${token}` },
  });
  return next(authReq);
};
