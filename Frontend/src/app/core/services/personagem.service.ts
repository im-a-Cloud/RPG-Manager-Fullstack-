import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { environment } from '../../../environments/environment';

import { Personagem } from '../../models/personagem';
import { PersonagemResponse } from '../../models/personagem-response';
import {Habilidade} from '../../models/habilidade';
import {Proficiencia} from '../../models/proficiencia';
import {Magia} from '../../models/magia';
import {Item} from '../../models/item';
import {Pericia} from '../../models/pericia';

@Injectable({
  providedIn: 'root'
})
export class PersonagemService {
  
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/personagens`;

  /**
   * Cria um novo personagem
   * @param personagem - Dados do personagem
   * @returns Observable com o personagem criado
   */
  criarPersonagem(personagem: any): Observable<any> {
      // 🔥 GARANTE QUE O CLASSE ID EXISTE
      const classeId = personagem.classeId || personagem.classePersonagem?.id || personagem.classePersonagem?.idClasse;    
      
      if (!classeId) {
          console.error('❌ Classe ID não encontrado!', personagem.classePersonagem);
          // Você pode lançar um erro ou retornar um observable com erro
      }

      console.log('📤 Service - Recebido:', personagem);
      console.log('📤 Service - pericias:', personagem.periciasPersonagem);
      console.log('📤 Service - pericias:', personagem.pericias);

      // 🔥 USAR O CAMPO CORRETO
      const pericias = personagem.pericias || [];

      const dadosParaEnviar = {
          nomePersonagem: personagem.nomePersonagem,
          nivelPersonagem: personagem.nivelPersonagem || 1,
          classeId: classeId,  // ← USA O ID CORRETO
          
          valorForca: personagem.valorForca || 10,
          valorDestreza: personagem.valorDestreza || 10,
          valorConstituicao: personagem.valorConstituicao || 10,
          valorInteligencia: personagem.valorInteligencia || 10,
          valorSabedoria: personagem.valorSabedoria || 10,
          valorCarisma: personagem.valorCarisma || 10,

          ca: personagem.caPersonagem || 10,
          iniciativa: personagem.iniciativaPersonagem || 0,
          pontosVida: personagem.pontosVidaPersonagem || 10,
          movimento: personagem.movimentoPersonagem || 9,

          historiaPersonagem: personagem.historiaPersonagem || '',
          aparenciaPersonagem: personagem.aparenciaPersonagem || '',
          racaPersonagem: personagem.racaPersonagem || '',
          antecedentePersonagem: personagem.antecedentePersonagem || '',
          ideaisPersonagem: personagem.ideaisPersonagem || '',
          defeitosPersonagem: personagem.defeitosPersonagem || '',
          anotacoesPersonagem: personagem.anotacoesPersonagem || '',
          personalidadePersonagem: personagem.personalidadePersonagem || '',
          escalaPersonagem: personagem.escalaPersonagem || '',
          alinhamentoPersonagem: personagem.alinhamentoPersonagem || '',
          pesoPersonagem: personagem.pesoPersonagem || 0,
          alturaPersonagem: personagem.alturaPersonagem || 0,
          idadePersonagem: personagem.idadePersonagem || 0,
          generoPersonagem: personagem.generoPersonagem || '',
          

          proficiencias: personagem.proficiencias || personagem.proficienciasPersonagem || [],
          pericias: personagem.pericias || [],
          habilidades: personagem.habilidades || [],
          magias: personagem.magias || [],
          inventario: personagem.inventario || [],

          fotoBase64: personagem.fotoBase64 || null
      };
      
      console.log('📤 Enviando DTO:', dadosParaEnviar);
      console.log('📤 Perícias enviadas:', dadosParaEnviar.pericias);

      return this.http.post(this.apiUrl, dadosParaEnviar);
  }
  /**
   * Lista todos os personagens
   * @returns Observable com lista de personagens
   */
listarPersonagens(): Observable<PersonagemResponse[]> {
  const url = this.apiUrl;
  console.log('🌐 [Service] GET', url);

  return this.http.get<PersonagemResponse[]>(url).pipe(
    tap({
      next: (dados) => {
        console.log('🌐 [Service] Resposta recebida:', dados);
        console.log('🌐 [Service] Quantidade:', dados?.length);
        console.log('🌐 [Service] É array?', Array.isArray(dados));
      },
      error: (err) => {
        console.error('🌐 [Service] ERRO:', err);
        console.error('🌐 [Service] Status:', err.status);
        console.error('🌐 [Service] Mensagem:', err.message);
        console.error('🌐 [Service] Body:', err.error);
      }
    })
  );
}

  /**
   * Busca um personagem por ID
   * @param id - ID do personagem
   * @returns Observable com o personagem
   */
  buscarPersonagemPorId(id: number): Observable<PersonagemResponse> {
    return this.http.get<PersonagemResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * Atualiza um personagem existente
   * @param id - ID do personagem
   * @param personagem - Dados atualizados
   * @returns Observable com o personagem atualizado
   */
  atualizarPersonagem(id: number, personagem: any): Observable<PersonagemResponse> {
    return this.http.put<PersonagemResponse>(`${this.apiUrl}/${id}`, personagem);
  }

  /**
   * Deleta um personagem
   * @param id - ID do personagem
   * @returns Observable void
   */
  deletarPersonagem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Busca personagens por classe
   * @param classeId - ID da classe
   * @returns Observable com lista de personagens
   */
  buscarPorClasse(classeId: number): Observable<Personagem[]> {
    return this.http.get<Personagem[]>(`${this.apiUrl}/classe/${classeId}`);
  }

  /**
   * Busca personagens por nível
   * @param nivelMin - Nível mínimo
   * @param nivelMax - Nível máximo
   * @returns Observable com lista de personagens
   */
  buscarPorNivel(nivelMin: number, nivelMax: number): Observable<Personagem[]> {
    return this.http.get<Personagem[]>(`${this.apiUrl}/nivel?min=${nivelMin}&max=${nivelMax}`);
  }
}