import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Client } from '../models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientService {
  private http = inject(HttpClient);

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(`${API_BASE_URL}/clients`);
  }

  getById(id: number): Observable<Client> {
    return this.http.get<Client>(`${API_BASE_URL}/clients/${id}`);
  }

  create(client: Omit<Client, 'id'>): Observable<void> {
    return this.http.post<void>(`${API_BASE_URL}/clients`, client);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${API_BASE_URL}/clients/${id}`);
  }

  update(id: number, client: Omit<Client, 'id'>): Observable<Client> {
    return this.http.put<Client>(`${API_BASE_URL}/clients/${id}`, client);
  }

  getFiches(id: number): Observable<any[]> {
    return this.http.get<any[]>(`${API_BASE_URL}/clients/${id}/fiches`);
  }
}