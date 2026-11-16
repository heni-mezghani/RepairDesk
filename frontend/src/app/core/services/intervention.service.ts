import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Intervention } from '../models/machine.model';

@Injectable({ providedIn: 'root' })
export class InterventionService {
  private http = inject(HttpClient);

  getByMachine(machineId: number): Observable<Intervention[]> {
    return this.http.get<Intervention[]>(
      `${API_BASE_URL}/machines/${machineId}/interventions`
    );
  }

  create(machineId: number, intervention: any): Observable<Intervention> {
    return this.http.post<Intervention>(
      `${API_BASE_URL}/machines/${machineId}/interventions`,
      intervention
    );
  }
}