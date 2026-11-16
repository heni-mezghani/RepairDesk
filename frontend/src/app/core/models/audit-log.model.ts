export interface AuditLog {
  id: number;
  action: string;
  entite: string;
  details: string;
  date: string;
}

export interface AuditHistoryResponse {
  username: string;
  logs: AuditLog[];
}