import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../../core/services/auth.service';

/**
 * Login screen. A simple reactive form (username + password) that calls AuthService,
 * redirects to the dashboard on success, and shows an error message on failure.
 */
@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  /** The form. Both fields are required. */
  readonly form = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  /** UI state, exposed as signals so the template reacts automatically. */
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);

  submit(): void {
    // Don't call the backend with an empty form — show the field errors instead.
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.login(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        // The backend returns 401 with a clean message for bad credentials.
        this.errorMessage.set(
          err?.error?.message ?? 'Échec de la connexion. Veuillez réessayer.',
        );
        this.loading.set(false);
      },
    });
  }
}
