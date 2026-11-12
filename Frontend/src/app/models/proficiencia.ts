import { TipoProficiencia } from './enums/tipoProficiencia.enum';

export interface Proficiencia {
  idProficiencia?: number;
  listaProficiencias?: string;
  tipoProficiencia: TipoProficiencia;
}