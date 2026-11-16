import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { AuditHistoryResponse } from '../models/audit-log.model';

@Injectable({ providedIn: 'root' })
export class AuditLogService {
  private http = inject(HttpClient);

  getByUtilisateur(id: string): Observable<AuditHistoryResponse> {
    return this.http.get<AuditHistoryResponse>(`${API_BASE_URL}/audit/utilisateur/${id}`);
  }
}