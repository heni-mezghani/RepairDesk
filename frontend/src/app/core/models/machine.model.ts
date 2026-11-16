export interface Machine {
  id: number;
  type: string;
  marque: string;
  modele: string;
  numeroSerie: string;
  problemeDeclare: string;
  diagnostic: string;
  categoriePanne: string;
  statut: string;
  coutEstime: number;
  coutFinal: number;
}

export interface Intervention {
  id: number;
  date: string;
  description: string;
  duree: number;
  technicien: { id: number; nom: string };
  pieces: Piece[];
}

export interface Piece {
  id: number;
  designation: string;
  reference: string;
  prixUnitaire: number;
  quantite: number;
}