import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Classe } from '../../models/classe';


@Injectable({
  providedIn: 'root'
})
export class ClasseService {
  
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/classes`;
  
  listarClasses(): Observable<Classe[]> {
    console.log('📋 Buscando classes do backend...');
    return this.http.get<Classe[]>(this.apiUrl);
  }

  buscarClassePorId(id: number): Observable<Classe> {
    console.log(`🔍 Buscando classe ID: ${id}`);
    return this.http.get<Classe>(`${this.apiUrl}/${id}`);
  }
  atualizarClasse(id: number, classe: Classe): Observable<Classe> {
    console.log(`📤 Atualizando classe ID: ${id}`);
    return this.http.put<Classe>(`${this.apiUrl}/${id}`, classe);
  }
}