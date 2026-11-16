import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UtilisateurService } from '../../../core/services/utilisateur.service';
import { Utilisateur } from '../../../core/models/user.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-admin',
  imports: [ReactiveFormsModule , RouterLink],
  templateUrl: './user-admin.html',
  styleUrl: './user-admin.css',
})
export class UserAdmin implements OnInit {
  private utilisateurService = inject(UtilisateurService);
  private fb = inject(FormBuilder);

  readonly users = signal<Utilisateur[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);
  readonly formError = signal<string | null>(null);
  readonly formLoading = signal(false);
  readonly editingId = signal<number | null>(null);

  // --- panel d'ajout ---
  readonly showAddPanel = signal(false);

  readonly addForm = this.fb.nonNullable.group({
    nom:        ['', Validators.required],
    username:   ['', Validators.required],
    motDePasse: ['', Validators.required],
    role:       ['TECHNICIEN', Validators.required],
  });

  readonly editForm = this.fb.nonNullable.group({
    nom:      ['', Validators.required],
    username: ['', Validators.required],
    role:     ['', Validators.required],
  });

  ngOnInit(): void {
    this.loadUsers();
  }

  private loadUsers(): void {
    this.loading.set(true);
    this.utilisateurService.getAll().subscribe({
      next: (data) => {
        this.users.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Erreur lors du chargement des utilisateurs.');
        this.loading.set(false);
      }
    });
  }

  openAddPanel(): void {
    this.showAddPanel.set(true);
  }

  closeAddPanel(): void {
    this.showAddPanel.set(false);
    this.formError.set(null);
    this.addForm.reset({ role: 'TECHNICIEN' });
  }

  addUser(): void {
    if (this.addForm.invalid) {
      this.addForm.markAllAsTouched();
      return;
    }

    this.formLoading.set(true);
    this.formError.set(null);

    this.utilisateurService.create(this.addForm.getRawValue()).subscribe({
      next: (newUser) => {
        this.users.update(list => [...list, newUser]);
        this.addForm.reset({ role: 'TECHNICIEN' });
        this.formLoading.set(false);
        this.showAddPanel.set(false);
      },
      error: (err) => {
        this.formError.set(
          err.error?.error ?? "Erreur lors de la création de l'utilisateur."
        );
        this.formLoading.set(false);
      }
    });
  }

  editUser(user: Utilisateur): void {
    this.editingId.set(user.id);
    this.editForm.patchValue(user);
  }

  cancelEdit(): void {
    this.editingId.set(null);
  }

  saveEdit(id: number): void {
    if (this.editForm.invalid) return;

    this.utilisateurService.update(id, this.editForm.getRawValue()).subscribe({
      next: (updated) => {
        this.users.update(list =>
          list.map(u => u.id === id ? updated : u)
        );
        this.editingId.set(null);
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la modification.');
      }
    });
  }

  toggleActif(id: number): void {
    this.utilisateurService.toggleActif(id).subscribe({
      next: (updated) => {
        this.users.update(list =>
          list.map(u => u.id === id ? updated : u)
        );
      },
      error: () => {
        this.errorMessage.set('Erreur lors du changement de statut.');
      }
    });
  }

}