import { Component, inject, OnInit, signal,computed } from '@angular/core';
import { MachineService } from '../../../core/services/machine.service';
import { Machine } from '../../../core/models/machine.model';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-machine-list',
  imports: [ReactiveFormsModule,RouterLink],
  templateUrl: './machine-list.html',
  styleUrl: './machine-list.css',
})
export class MachineList implements OnInit {
  private machineService = inject(MachineService);
  private fb = inject(FormBuilder);

  readonly Machines = signal<Machine[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);
  readonly formLoading = signal(false);
  readonly editingId = signal<number | null>(null);

  // --- filtres ---
  readonly categorieFilter = signal<string>('TOUTES');
  readonly statutFilter = signal<string>('TOUS');

  readonly filteredMachines = computed(() => {
    const categorie = this.categorieFilter();
    const statut = this.statutFilter();

    return this.Machines().filter(m => {
      const matchCategorie = categorie === 'TOUTES' || m.categoriePanne === categorie;
      const matchStatut = statut === 'TOUS' || m.statut === statut;
      return matchCategorie && matchStatut;
    });
  });


readonly editForm = this.fb.nonNullable.group({
    type:            ['', Validators.required],
    marque:          ['', Validators.required],
    modele:          ['', Validators.required],
    numeroSerie:     [''],
    problemeDeclare: ['', Validators.required],
    diagnostic:      ['', Validators.required],
    categoriePanne:  ['', Validators.required],
    statut:          ['', Validators.required],
    coutEstime:      [0, Validators.required],
    coutFinal:       [0, Validators.required],
});

  ngOnInit(): void {
    this.loadMachines();
  }

  private loadMachines(): void {
    this.loading.set(true);
    this.machineService.getAll().subscribe({
      next: (data) => {
        this.Machines.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set(
          err.error?.message || 'Erreur lors du chargement des Machines.'
        );
        this.loading.set(false);
      }
    });
  }

  onCategorieFilterChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.categorieFilter.set(value);
  }

  onStatutFilterChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.statutFilter.set(value);
  }


  editMachine(machine: Machine): void {
      this.editingId.set(machine.id);
      this.editForm.patchValue(machine);
  }

  cancelEdit(): void {
    this.editingId.set(null);
  }

  saveEdit(id: number): void {
      if (this.editForm.invalid) return;

      this.machineService.update(id, this.editForm.getRawValue()).subscribe({
          next: (updated) => {
              this.Machines.update(list =>
                  list.map(m => m.id === id ? updated : m)
              );
              this.editingId.set(null);
          },
          error: () => {
              this.errorMessage.set('Erreur lors de la modification.');
          }
      });
  }

}