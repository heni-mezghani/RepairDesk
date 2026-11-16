import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { FicheReception } from '../models/fiche.model';

@Injectable({ providedIn: 'root' })
export class FicheService {
  private http = inject(HttpClient);

  getAll(): Observable<any[]> {
    return this.http.get<any[]>(`${API_BASE_URL}/fiches`);
  }

  getById(id: number): Observable<FicheReception> {
    return this.http.get<FicheReception>(`${API_BASE_URL}/fiches/${id}`);
  }

  create(fiche: any,clientId : number): Observable<void> {
    return this.http.post<void>(`${API_BASE_URL}/fiches/${clientId}`, fiche);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/fiches/${id}`);
  }
}