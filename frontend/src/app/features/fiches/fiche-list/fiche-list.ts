import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FicheService } from '../../../core/services/fiche.service';
import { FicheCreate } from '../fiche-create/fiche-create';

@Component({
  selector: 'app-fiche-list',
  imports: [RouterLink, FicheCreate],
  templateUrl: './fiche-list.html',
  styleUrl: './fiche-list.css',
})
export class FicheList implements OnInit {
  private ficheService = inject(FicheService);

  readonly fiches = signal<any[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);
  readonly searchTerm = signal('');

  // --- panel de création ---
  readonly showCreatePanel = signal(false);

  readonly filteredFiches = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    if (!term) return this.fiches();

    return this.fiches().filter(f =>
      f.numero.toLowerCase().includes(term) ||
      f.client?.nom?.toLowerCase().includes(term) ||
      f.dateReception?.includes(term)
    );
  });

  ngOnInit(): void {
    this.loadFiches();
  }

  private loadFiches(): void {
    this.loading.set(true);
    this.ficheService.getAll().subscribe({
      next: (data) => {
        this.fiches.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set(
          err.error?.message || "Erreur lors du chargement des fiches."
        );
        this.loading.set(false);
      }
    });
  }

  onSearch(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.searchTerm.set(value);
  }

  openCreatePanel(): void {
    this.showCreatePanel.set(true);
  }

  closeCreatePanel(): void {
    this.showCreatePanel.set(false);
  }

  onFicheCreated(): void {
    this.showCreatePanel.set(false);
    this.loadFiches();
  }
}