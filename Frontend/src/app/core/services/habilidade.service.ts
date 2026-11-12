import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import { Habilidade } from '../../models/habilidade';

@Injectable({
  providedIn: 'root'
})
export class HabilidadeService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/habilidades`;

    criarHabilidade(habilidade: any): Observable<any> {
        const dadosParaEnviar = {
        nomeHabilidade: habilidade.nomeHabilidade,
        origemHabilidade: habilidade.origemHabilidade,
        descricaoHabilidade: habilidade.descricaoHabilidade,
        usosHabilidade: habilidade.usosHabilidade,
        recargaHabilidade: habilidade.recargaHabilidade,
        };
        return this.http.post(this.apiUrl, dadosParaEnviar);
    }

    atualizarHabilidade(id: number, habilidade: any): Observable<any> {
        return this.http.put<Habilidade>(`${this.apiUrl}/${id}`, habilidade);
    }
    deletarHabilidade(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}