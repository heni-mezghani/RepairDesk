import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Machine } from '../models/machine.model';

export interface DashboardStats {
  machinesParStatut: Record<string, number>;
  pannesParCategorie: Record<string, number>;
  machinesTerminees: any[];
  fichesDuJour: any[];
  interventionsParTechnicien?: Record<string, number>;
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);

  getMachinesParStatut(): Observable<Record<string, number>> {
    return this.http.get<Record<string, number>>(`${API_BASE_URL}/dashboard/machines-par-statut`);
  }

  getPannesParCategorie(): Observable<Record<string, number>> {
    return this.http.get<Record<string, number>>(`${API_BASE_URL}/dashboard/pannes-par-categorie`);
  }

  getMachinesTerminees(): Observable<Machine[]> {
    return this.http.get<Machine[]>(`${API_BASE_URL}/dashboard/machines-terminees`);
  }

  getFichesDuJour(): Observable<any[]> {
    return this.http.get<any[]>(`${API_BASE_URL}/dashboard/fiches-du-jour`);
  }

  getInterventionsParTechnicien(): Observable<Record<string, number>> {
    return this.http.get<Record<string, number>>(`${API_BASE_URL}/dashboard/interventions-par-technicien`);
  }
}