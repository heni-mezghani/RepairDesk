
import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuditLogService } from '../../../core/services/audit-log.service';
import { AuditLog } from '../../../core/models/audit-log.model';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-user-audit-log',
  imports: [RouterLink, FormsModule, DatePipe],
  templateUrl: './user-audit-log.html',
  styleUrl: './user-audit-log.css',
})
export class UserAuditLog implements OnInit {

  private route = inject(ActivatedRoute);
  private auditLogService = inject(AuditLogService);

  readonly logs = signal<AuditLog[]>([]);
  readonly username = signal<string>('');

  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);

  // --- filtres de date ---
  readonly dateFrom = signal<string>('');
  readonly dateTo = signal<string>('');

  readonly filteredLogs = computed(() => {
    const from = this.dateFrom();
    const to = this.dateTo();

    return this.logs().filter(log => {
      const logDate = log.date.substring(0, 10);

      if (from && logDate < from) return false;
      if (to && logDate > to) return false;

      return true;
    });
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      this.errorMessage.set('Utilisateur introuvable.');
      this.loading.set(false);
      return;
    }

    this.loadLogs(id);
  }

  private loadLogs(id: string): void {
    this.loading.set(true);

    this.auditLogService.getByUtilisateur(id).subscribe({
      next: (data) => {

        this.username.set(data.username);

        this.logs.set(data.logs);

        this.loading.set(false);
      },

      error: () => {
        this.errorMessage.set(
          "Erreur lors du chargement de l'historique."
        );

        this.loading.set(false);
      }
    });
  }

  onDateFromChange(event: Event): void {
    this.dateFrom.set(
      (event.target as HTMLInputElement).value
    );
  }

  onDateToChange(event: Event): void {
    this.dateTo.set(
      (event.target as HTMLInputElement).value
    );
  }

  clearFilters(): void {
    this.dateFrom.set('');
    this.dateTo.set('');
  }
}
