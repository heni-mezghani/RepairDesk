import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FicheService } from '../../../core/services/fiche.service';
import { FicheReception } from '../../../core/models/fiche.model';

@Component({
  selector: 'app-fiche-detail',
  imports: [RouterLink],
  templateUrl: './fiche-detail.html',
  styleUrl: './fiche-detail.css',
})
export class FicheDetail implements OnInit {
  private ficheService = inject(FicheService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  readonly fiche = signal<FicheReception | null>(null);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.ficheService.getById(id).subscribe({
      next: (data) => {
        this.fiche.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Fiche introuvable.');
        this.loading.set(false);
      }
    });
  }

  deleteFiche(): void {
    const f = this.fiche();
    if (!f) return;

    if (!confirm('Voulez-vous vraiment supprimer cette fiche et toutes ses machines ?')) return;

    this.ficheService.delete(f.id).subscribe({
      next: () => this.router.navigate(['/fiches']),
      error: () => this.errorMessage.set('Erreur lors de la suppression.')
    });
  }

  printReceipt(): void {
    window.print();
  }
}