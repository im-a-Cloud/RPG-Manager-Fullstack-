import { Atributos } from './enums/atributos.enum';

export interface Pericia {
  id: number;
  slug: string;
  nomeExibicao: string;
  atributoChave: Atributos;
}

export interface PericiaPersonagemRequest {
  slug: string;
  isProficiente: boolean;
}

export interface PericiaPersonagemResponse {
  nomePericia: string;
  atributoChave?: Atributos;
  isProficiente: boolean;
  modificador: number;
  bonusProficiencia: number;
  valorTotalPericia: number;
}