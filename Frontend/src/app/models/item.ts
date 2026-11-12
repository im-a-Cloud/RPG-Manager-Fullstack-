import { RaridadeItem } from './enums/raridadeItem.enum';
import { TipoItem } from './enums/tipoItem.enum';

export interface Item {
    idItem?: number;
    nomeItem: string;
    descricaoItem?: string;
    precoItem?: number;
    raridadeItem?: RaridadeItem;
    pesoItem?: number;
    isMagicoItem?: boolean;
    precisaSintonizacao: boolean;
    quantidadeItem?: number;
    tipoItem: TipoItem;
}