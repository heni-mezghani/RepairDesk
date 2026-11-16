import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MachineService } from '../../../core/services/machine.service';
import { InterventionService } from '../../../core/services/intervention.service';
import { PieceService } from '../../../core/services/piece.service';
import { Machine, Intervention } from '../../../core/models/machine.model';

@Component({
  selector: 'app-machine-detail',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './machine-detail.html',
  styleUrl: './machine-detail.css',
})
export class MachineDetail implements OnInit {
  private route = inject(ActivatedRoute);
  private machineService = inject(MachineService);
  private interventionService = inject(InterventionService);
  private pieceService = inject(PieceService);
  private fb = inject(FormBuilder);

  readonly machine = signal<Machine | null>(null);
  readonly interventions = signal<Intervention[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);
  readonly formError = signal<string | null>(null);
  readonly formLoading = signal(false);


  readonly addingPieceForId = signal<number | null>(null);
  readonly pieceFormLoading = signal(false);

  readonly form = this.fb.nonNullable.group({
    description: ['', Validators.required],
    duree:       [0, Validators.required],
  });

  readonly pieceForm = this.fb.nonNullable.group({
    designation:  ['', Validators.required],
    reference:    ['', Validators.required],
    prixUnitaire: [0, Validators.required],
    quantite:     [1, Validators.required],
  });

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.machineService.getById(id).subscribe({
      next: (data) => {
        this.machine.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Machine introuvable.');
        this.loading.set(false);
      }
    });

    this.loadInterventions(id);
  }

  private loadInterventions(machineId: number): void {
    this.interventionService.getByMachine(machineId).subscribe({
      next: (data) => this.interventions.set(data),
      error: () => this.errorMessage.set('Erreur lors du chargement des interventions.')
    });
  }

  addIntervention(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const machineId = Number(this.route.snapshot.paramMap.get('id'));

    const intervention = {
      ...this.form.getRawValue(),
      pieces: []
    };

    this.formLoading.set(true);
    this.formError.set(null);

    this.interventionService.create(machineId, intervention).subscribe({
      next: (newIntervention) => {
        this.interventions.update(list => [...list, { ...newIntervention, pieces: [] }]);
        this.form.reset({ duree: 0 });
        this.formLoading.set(false);
      },
      error: () => {
        this.formError.set('Erreur lors de l\'ajout de l\'intervention.');
        this.formLoading.set(false);
      }
    });
  }

  openPieceForm(interventionId: number): void {
    this.addingPieceForId.set(interventionId);
    this.pieceForm.reset({ quantite: 1, prixUnitaire: 0 });
  }

  cancelPieceForm(): void {
    this.addingPieceForId.set(null);
    this.pieceForm.reset();
  }

  addPiece(interventionId: number): void {
    if (this.pieceForm.invalid) {
      this.pieceForm.markAllAsTouched();
      return;
    }

    this.pieceFormLoading.set(true);

    this.pieceService.create(interventionId, this.pieceForm.getRawValue()).subscribe({
      next: (newPiece) => {
        this.interventions.update(list =>
          list.map(i => i.id === interventionId
            ? { ...i, pieces: [...(i.pieces || []), newPiece] }
            : i
          )
        );
        this.addingPieceForId.set(null);
        this.pieceForm.reset();
        this.pieceFormLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Erreur lors de l\'ajout de la pièce.');
        this.pieceFormLoading.set(false);
      }
    });
  }

  deletePiece(interventionId: number, pieceId: number): void {
    if (!confirm('Supprimer cette pièce ?')) return;

    this.pieceService.delete(interventionId, pieceId).subscribe({
      next: () => {
        this.interventions.update(list =>
          list.map(i => i.id === interventionId
            ? { ...i, pieces: i.pieces.filter(p => p.id !== pieceId) }
            : i
          )
        );
        console.log(this.interventions());
      },
      error: () => this.errorMessage.set('Erreur lors de la suppression.')
    });
  }
}