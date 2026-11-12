import {OrigemHabilidade} from './enums/origemHabilidade.enum';

export interface Habilidade {
    idHabilidade?: number;
    nomeHabilidade: string;
    descricaoHabilidade?: string;
    origemHabilidade?: OrigemHabilidade;
    usosHabilidade?: number;
    recargaHabilidade?: string;
}