import { Component, inject , signal} from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { AuthService } from '../../core/services/auth.service';

/**
 * The shell around every authenticated page: a top navbar (app name, nav links, the
 * current user, and a logout button) plus a <router-outlet> where the feature pages
 * render.
 *
 * Feature routes are nested as children of this component (see app.routes.ts), so they
 * all share this navbar.
 */
@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
  private authService = inject(AuthService);

  /** Read in the template to greet the user and show admin-only links. */
  readonly currentUser = this.authService.currentUser;
  readonly isAdmin = this.authService.isAdmin;

  readonly menuOpen = signal(false);

  toggleMenu(): void {
    this.menuOpen.update(v => !v);
  }

  closeMenu(): void {
    this.menuOpen.set(false);
  }

  logout(): void {
    this.authService.logout();
  }
}
