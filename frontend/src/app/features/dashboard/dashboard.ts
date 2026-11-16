import { Component, inject, OnInit, signal } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { DashboardService } from '../../core/services/dashboard.service';
import { RouterLink } from '@angular/router';
import { Machine } from '../../core/models/machine.model';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  private authService = inject(AuthService);
  private dashboardService = inject(DashboardService);

  readonly currentUser = this.authService.currentUser;
  readonly isAdmin = this.authService.isAdmin;

  readonly machinesParStatut = signal<Record<string, number>>({});
  readonly pannesParCategorie = signal<Record<string, number>>({});
  readonly machinesTerminees = signal<Machine[]>([]);
  readonly fichesDuJour = signal<any[]>([]);
  readonly interventionsParTechnicien = signal<Record<string, number>>({});
  readonly loading = signal(true);

  ngOnInit(): void {
    this.dashboardService.getMachinesParStatut().subscribe({
      next: (data) => this.machinesParStatut.set(data)
    });

    this.dashboardService.getPannesParCategorie().subscribe({
      next: (data) => this.pannesParCategorie.set(data)
    });

    this.dashboardService.getMachinesTerminees().subscribe({
      next: (data) => this.machinesTerminees.set(data)
    });

    this.dashboardService.getFichesDuJour().subscribe({
      next: (data) => {
        this.fichesDuJour.set(data);
        this.loading.set(false);
      }
    });

    if (this.isAdmin()) {
      this.dashboardService.getInterventionsParTechnicien().subscribe({
        next: (data) => this.interventionsParTechnicien.set(data)
      });
    }
  }

  getStatutColor(statut: string): string {
    const colors: Record<string, string> = {
      'RECU':           '#3498db',
      'EN_COURS':       '#f39c12',
      'EN_ATTENTE':     '#e67e22',
      'TERMINE':        '#2ecc71',
      'RECUPERE':       '#95a5a6',
      'NON_REPARABLE':  '#e74c3c',
    };
    return colors[statut] || '#ccc';
  }

  objectKeys(obj: Record<string, number>): string[] {
    return Object.keys(obj);
  }
}