import {Component, EventEmitter , Output, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClientService } from '../../../core/services/client.service';

@Component({
  selector: 'app-client-create',
  imports: [ReactiveFormsModule],
  templateUrl: './client-create.html',
  styleUrl: './client-create.css',
})
export class ClientCreate {
  private fb = inject(FormBuilder);
  private clientService = inject(ClientService);


  @Output() created = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();


  readonly formError = signal<string | null>(null);
  readonly formLoading = signal(false);

  readonly form = this.fb.nonNullable.group({
    nom:       ['', Validators.required],
    telephone: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
    email:     ['',[Validators.required, Validators.pattern(/^[a-zA-Z].*@.*[a-zA-Z]$/)]],
    adresse:   ['', Validators.required],
    type:      ['PARTICULIER', Validators.required],
  });

   submit(): void {

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.formLoading.set(true);
    this.formError.set(null);

    this.clientService.create(this.form.getRawValue()).subscribe({
      next: () => {
        this.formLoading.set(false);
        this.created.emit(); 
      },
      error: () => {
        this.formError.set('Erreur lors de la création du client.');
        this.formLoading.set(false);
      }
    });
  }

  onBack(): void {
    this.cancelled.emit();
  }
}