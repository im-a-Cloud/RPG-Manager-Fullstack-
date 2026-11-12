import {Magia} from "./magia";
import {Item} from "./item";
import {Pericia} from "./pericia";
import {Habilidade} from "./habilidade";
import {Proficiencia} from "./proficiencia";
import {Classe} from "./classe";
import { Atributos } from "./enums/atributos.enum";

export interface Personagem {
  idPersonagem?: number;

  nomePersonagem: string;
  nivelPersonagem: number;

  valorForca: number;
  valorDestreza: number;
  valorConstituicao: number;
  valorInteligencia: number;
  valorSabedoria: number;
  valorCarisma: number;

  racaPersonagem?: string;
  escalaPersonagem?: string;
  antecedentePersonagem?: string;

  caPersonagem: number;
  iniciativaPersonagem: number;
  movimentoPersonagem: number;
  pontosVidaPersonagem: number;

  classePersonagem?: Classe | null;
  magias?: Magia[];
  inventarioPersonagem?: Item[];
  periciasPersonagem?: Pericia[];
  habilidades?: Habilidade[];
  proficienciasPersonagem?: Proficiencia[];

  fotoBase64?: string;

  historiaPersonagem?: string;
  aparenciaPersonagem?: string;
  ideaisPersonagem?: string;
  defeitosPersonagem?: string;
  anotacoesPersonagem?: string;
  personalidadePersonagem?: string;
  idadePersonagem?: number;
  alturaPersonagem?: number;
  pesoPersonagem?: number;
  generoPersonagem?: string;
  alinhamentoPersonagem?: string;
}