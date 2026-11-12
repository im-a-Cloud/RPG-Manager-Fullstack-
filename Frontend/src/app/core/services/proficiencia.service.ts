import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Proficiencia } from '../../models/proficiencia';

@Injectable({
  providedIn: 'root'
})
export class ProficienciaService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/proficiencias`;

    criarProficiencia(proficiencia: any): Observable<any> {
        const dadosParaEnviar = {
        tipoProficiencia: proficiencia.tipoProficiencia,
        listaProficiencias: proficiencia.listaProficiencias
        };
        return this.http.post(this.apiUrl, dadosParaEnviar);
    }
    atualizarProficiencia(id: number, proficiencia: any): Observable<any> {
        return this.http.put<Proficiencia>(`${this.apiUrl}/${id}`, proficiencia);
    }
    deletarProficiencia(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}