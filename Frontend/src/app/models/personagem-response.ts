import { Atributos } from './enums/atributos.enum';
import { Classes } from './enums/classes.enum';
import { DadosDeVida } from './enums/dadosDeVida.enum';
import { TipoConjuracao } from './enums/tipoConjuracao.enum';

export interface PersonagemResponse {
  id: number;
  nomePersonagem: string;
  nivelPersonagem: number;

  classe: {
    id: number;
    nome: Classes;
    dadoDeVida: DadosDeVida;
    isConjurador: boolean;
    atributoConjuracao: Atributos | null;
    tipo: TipoConjuracao;
    proficienciaSalvaguarda: Atributos[];
  };

  atributos: {
    forca: number;
    destreza: number;
    constituicao: number;
    inteligencia: number;
    sabedoria: number;
    carisma: number;
    bonusForca: number;
    bonusDestreza: number;
    bonusConstituicao: number;
    bonusInteligencia: number;
    bonusSabedoria: number;
    bonusCarisma: number;
  };

  magia: {
    bonusProficiencia: number;
    cd: number;
    ataque: number;
  };

  inventario: any[];
  pericias: any[];
  habilidades: any[];
  proficiencia: any[];
  magias: any[];

  // Descritivos
  historiaPersonagem: string;
  aparenciaPersonagem: string;
  ideaisPersonagem: string;
  defeitosPersonagem: string;
  anotacoesPersonagem: string;
  personalidadePersonagem: string;

  // Biográficos
  racaPersonagem: string;
  escalaPersonagem: string;
  alinhamentoPersonagem: string;
  idadePersonagem: number;
  generoPersonagem: string;
  antecedentePersonagem: string;

  // Numéricos
  pesoPersonagem: number;
  alturaPersonagem: number;

  // Combate
  ca: number;
  iniciativa: number;
  movimento: number;
  pontosVidaPersonagem: number;

  // Foto
  fotoBase64: string;
}