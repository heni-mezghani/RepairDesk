import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../config/api.config';
import { Piece } from '../models/machine.model';

@Injectable({ providedIn: 'root' })
export class PieceService {
  private http = inject(HttpClient);

  create(interventionId: number, piece: Omit<Piece, 'id'>): Observable<Piece> {
    return this.http.post<Piece>(
      `${API_BASE_URL}/interventions/${interventionId}/pieces`,
      piece
    );
  }

  delete(interventionId: number, pieceId: number): Observable<void> {
    return this.http.delete<void>(
      `${API_BASE_URL}/interventions/${interventionId}/pieces/${pieceId}`
    );
  }
}