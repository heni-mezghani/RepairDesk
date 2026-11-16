import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Utilisateur } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  private http = inject(HttpClient);

  getAll(): Observable<Utilisateur[]> {
    return this.http.get<Utilisateur[]>(`${API_BASE_URL}/utilisateurs`);
  }

  create(user: any): Observable<Utilisateur> {
    return this.http.post<Utilisateur>(`${API_BASE_URL}/utilisateurs`, user);
  }

  update(id: number, user: any): Observable<Utilisateur> {
    return this.http.patch<Utilisateur>(`${API_BASE_URL}/utilisateurs/${id}`, user);
  }

  toggleActif(id: number): Observable<Utilisateur> {
    return this.http.patch<Utilisateur>(`${API_BASE_URL}/utilisateurs/${id}/toggle-actif`, {});
  }

  delete(id: number) {
    return this.http.delete<void>(`${API_BASE_URL}/utilisateurs/${id}`);
  }
}