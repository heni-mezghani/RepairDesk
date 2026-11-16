import { Component, inject, OnInit, signal, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { FicheService } from '../../../core/services/fiche.service';
import { ClientService } from '../../../core/services/client.service';
import { Client } from '../../../core/models/client.model';
import { FormGroup } from '@angular/forms';


@Component({
  selector: 'app-fiche-create',
  imports: [ReactiveFormsModule],
  templateUrl: './fiche-create.html',
  styleUrl: './fiche-create.css',
})
export class FicheCreate implements OnInit {
  private fb = inject(FormBuilder);
  private ficheService = inject(FicheService);
  private clientService = inject(ClientService);


  readonly clients = signal<Client[]>([]);
  readonly loading = signal(false);
  readonly errorMessage = signal<string | null>(null);


  @Output() created = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  readonly form = this.fb.group({
    clientId: [null as number | null, Validators.required],
    machines: this.fb.array([])
  });

  ngOnInit(): void {
    this.clientService.getAll().subscribe({
      next: (data) => this.clients.set(data),
      error: (err) => {
        this.errorMessage.set(
          err.error?.message || 'Erreur lors du chargement des clients.'
        );
      }
    });

    this.addMachine();
  }

  get machines() {
    return this.form.get('machines') as FormArray;
  }

  getMachineGroup(index: number): FormGroup {
    return this.machines.at(index) as FormGroup;
  }

  addMachine(): void {
    this.machines.push(this.fb.nonNullable.group({
      type:            ['', Validators.required],
      marque:          ['', Validators.required],
      modele:          ['', Validators.required],
      numeroSerie:     [''],
      problemeDeclare: ['', Validators.required],
      diagnostic:      ['', Validators.required],
      categoriePanne:  ['MATERIEL', Validators.required],
      statut:          ['RECU', Validators.required],
      coutEstime:      [0, Validators.required],
      coutFinal:       [0, Validators.required],
    }));
  }

  removeMachine(index: number): void {
    this.machines.removeAt(index);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();

    const fiche = {
      machines: value.machines
    };

    this.loading.set(true);
    this.errorMessage.set(null);

    const clientId = Number(value.clientId);

    this.ficheService.create(fiche,clientId).subscribe({
      next: () => {
        this.loading.set(false);
        this.created.emit(); 
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la création de la fiche.');
        this.loading.set(false);
      }
    });
  }

  onBack(): void {
    this.cancelled.emit();
  }
}