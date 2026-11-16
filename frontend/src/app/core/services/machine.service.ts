import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Machine } from '../models/machine.model';

@Injectable({ providedIn: 'root' })
export class MachineService {
  private http = inject(HttpClient);

  getAll(): Observable<Machine[]> {
    return this.http.get<Machine[]>(`${API_BASE_URL}/Machines`);
  }

  update(id: number, machine: any): Observable<Machine> {
    return this.http.put<Machine>(`${API_BASE_URL}/Machines/${id}`, machine);
  }

  getById(id: number): Observable<Machine> {
    return this.http.get<Machine>(`${API_BASE_URL}/Machines/${id}`);
  }

}