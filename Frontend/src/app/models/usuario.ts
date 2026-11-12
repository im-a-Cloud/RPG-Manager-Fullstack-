import { Personagem } from "./personagem";

export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  senha: string;
  fotoPerfil?: string;
  listaPersonagensUsuario?: Personagem[];
}