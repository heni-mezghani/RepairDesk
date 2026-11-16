import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ClientService } from '../../../core/services/client.service';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'app-client-detail',
  imports: [RouterLink],
  templateUrl: './client-detail.html',
  styleUrl: './client-detail.css',
})
export class ClientDetail implements OnInit {
  private clientService = inject(ClientService);
  private route = inject(ActivatedRoute);

  readonly client = signal<Client | null>(null);
  readonly fiches = signal<any[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.clientService.getById(id).subscribe({
      next: (data) => {
        this.client.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Client introuvable.');
        this.loading.set(false);
      }
    });

    this.clientService.getFiches(id).subscribe({
      next: (data) => this.fiches.set(data),
      error: () => this.errorMessage.set('Erreur lors du chargement des fiches.')
    });
  }
}