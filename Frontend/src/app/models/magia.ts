import { ComponentesMagia } from './componentesMagia';
import { EscolaMagia } from './enums/escolaMagia.enum';

export interface Magia {
  idMagia?: number;
  name: string;              // ← nomeMagia
  level: string;             // ← nivelMagia
  casting_time: string;      // ← tempoConjuracao
  range: string;             // ← alcance
  components: ComponentesMagia;
  duration: string;          // ← duracao
  school: string;       // ← escola
  ritual: boolean;           // ← ritual
  concentration: boolean;    // ← concentracao
  description: string;       // ← descricao
  classes?: string[];        // ← classes que podem usar
  tags?: string[];           // ← tags/categorias
  type?: string;             // ← tipo (ex: "Ataque", "Defesa", "Utilidade")
}