import { Proficiencia } from './proficiencia';
import { DadosDeVida } from './enums/dadosDeVida.enum';
import { TipoConjuracao } from './enums/tipoConjuracao.enum';
import { Atributos } from './enums/atributos.enum';

export interface Classe {
  id?: number;
  nomeClasse: string;
  dadoDeVida: DadosDeVida;
  listaProficienciasClasse?: Proficiencia[];
  proficienciaSalvaguarda?: string[];

  conjurador: boolean;
  atributoConjuracao?: Atributos;
  tipoConjuracao?: TipoConjuracao;
  cdMagia?: number;
  ataqueMagico?: number;
}