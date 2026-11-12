import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, shareReplay } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Pericia } from '../../models/pericia';


@Injectable({ providedIn: 'root' })
export class PericiaService {

  private readonly apiUrl = `${environment.apiUrl}/pericias`;
  private readonly cache$ = new BehaviorSubject<Pericia[]>([]);

  constructor(private http: HttpClient) {}

  listar(): Observable<Pericia[]> {
    if (this.cache$.value.length > 0) {
      return this.cache$.asObservable();
    }
    return this.http.get<Pericia[]>(this.apiUrl).pipe(
      tap(pericias => this.cache$.next(pericias)),
      shareReplay(1)
    );
  }

  limparCache(): void {
    this.cache$.next([]);
  }
}