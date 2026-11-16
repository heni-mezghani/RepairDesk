import { Client } from './client.model';
import { Machine } from './machine.model';

export interface FicheReception {
  id: number;
  numero: string;
  dateReception: string;
  client: Client;
  technicienRecepteur: { id: number; nom: string };
  machines: Machine[];
}