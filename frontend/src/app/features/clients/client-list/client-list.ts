import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { ClientService } from '../../../core/services/client.service';
import { Client } from '../../../core/models/client.model';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ClientCreate } from '../../clients/cient-create/client-create';


@Component({
  selector: 'app-client-list',
  imports: [ReactiveFormsModule, RouterLink,ClientCreate],
  templateUrl: './client-list.html',
  styleUrl: './client-list.css',
})
export class ClientList implements OnInit {
  private clientService = inject(ClientService);
  private fb = inject(FormBuilder);

  readonly clients = signal<Client[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);
  readonly editingId = signal<number | null>(null);
  readonly searchTerm = signal('');

  readonly showCreatePanel = signal(false);

  readonly filteredClients = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    if (!term) return this.clients();

    return this.clients().filter(c =>
      c.nom.toLowerCase().includes(term) ||
      c.telephone.toLowerCase().includes(term) ||
      c.email?.toLowerCase().includes(term)
    );
  });

  readonly editForm = this.fb.nonNullable.group({
    nom:       ['', Validators.required],
    telephone: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
    email:     ['', [Validators.required, Validators.pattern(/^[a-zA-Z].*@.*[a-zA-Z]$/)]],
    adresse:   ['' , Validators.required],
    type:      ['', Validators.required],
  });

  ngOnInit(): void {
    this.loadClients();
  }

  private loadClients(): void {
    this.loading.set(true);
    this.clientService.getAll().subscribe({
      next: (data) => {
        this.clients.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set(
          err.error?.message || 'Erreur lors du chargement des clients.'
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

    onClientCreated(): void {
      this.showCreatePanel.set(false);
      this.loadClients(); 
    }

  deleteClient(id: number): void {
    if (!confirm('Voulez-vous vraiment supprimer ce client et ses fiches ?')) return;

    this.clientService.delete(id).subscribe({
      next: () => {
        this.clients.update(list => list.filter(c => c.id !== id));
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la suppression.');
      }
    });
  }

  editClient(client: Client): void {
    this.editingId.set(client.id);
    this.editForm.patchValue(client);
  }

  cancelEdit(): void {
    this.editingId.set(null);
  }

  saveEdit(id: number): void {
    
    if (this.editForm.invalid) {
      return;
    }

    this.clientService.update(id, this.editForm.getRawValue()).subscribe({
      next: (updated) => {
        this.clients.update(list =>
          list.map(c => c.id === id ? updated : c)
        );
        this.editingId.set(null);
      },
      error: () => {
        this.errorMessage.set('Erreur lors de la modification.');
      }
    });
  }
}