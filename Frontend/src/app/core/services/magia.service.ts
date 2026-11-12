import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import {Magia} from '../../models/magia';

@Injectable({
  providedIn: 'root'
})

export class MagiaService {
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/magias`;

    criarMagia(magia: any): Observable<any> {
        const dadosParaEnviar = {
          name: magia.name || magia.nomeMagia,                    // ← name
          level: magia.level || magia.nivelMagia || '0',          // ← level
          casting_time: magia.casting_time || magia.tempoConjuracao || '',  // ← casting_time
          range: magia.range || magia.alcance || '',              // ← range
          components: {
            verbal: magia.components?.verbal || magia.componentes?.verbal || false,
            somatic: magia.components?.somatico || magia.componentes?.somatico || false,
            material: magia.components?.material || magia.componentes?.material || false,
            material_description: magia.components?.descricaoMaterial || 
                                magia.componentes?.descricaoMaterial || ''
          },
          duration: magia.duration || magia.duracao || '',        // ← duration
          school: magia.school || magia.escola || '',             // ← school
          ritual: magia.ritual || false,                          // ← ritual
          concentration: magia.concentration || magia.concentracao || false, // ← concentration
          description: magia.description || magia.descricao || '', // ← description
          classes: magia.classes || [],                           // ← classes
          tags: magia.tags || [],                                 // ← tags
          type: magia.type || ''                                  // ← type
        };

        console.log('📤 Enviando magia:', dadosParaEnviar);
        return this.http.post(this.apiUrl, dadosParaEnviar);
      }
    atualizarMagia(id: number, magia: any): Observable<any> {
        return this.http.put<Magia>(`${this.apiUrl}/${id}`, magia);
    }
    deletarMagia(id: number): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}