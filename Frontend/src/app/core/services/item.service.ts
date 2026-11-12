import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

import {Item} from '../../models/item';

@Injectable({
  providedIn: 'root'
})

export class ItemService{
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/itens`;

    criarItem(item: any): Observable<any> {
    const dadosItem = {
      nomeItem: item.nomeItem,
      descricaoItem: item.descricaoItem || '',
      precoItem: item.precoItem ?? 0,
      raridadeItem: item.raridadeItem || 'COMUM',
      pesoItem: item.pesoItem ?? 0,
      isMagico: item.isMagicoItem ?? false,        // ← MUDOU!
      precisaSintonizacao: item.precisaSintonizacao ?? false,
      quantidade: item.quantidadeItem ?? 1,         // ← MUDOU!
      tipoItem: item.tipoItem
    };

    console.log('📤 Enviando item:', dadosItem);
    return this.http.post(this.apiUrl, dadosItem);
  }

  atualizarItem(id: number, item: any): Observable<Item> {
    return this.http.put<Item>(`${this.apiUrl}/${id}`, item);
  }


  deletarItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
} 