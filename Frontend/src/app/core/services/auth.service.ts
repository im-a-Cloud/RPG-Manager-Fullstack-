import {Injectable, inject} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import { environment } from '../../../environments/environment';

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface RegisterRequest {
  email: string;
  senha: string;
  nome: string;
}

export interface TokenResponse {
  token: string;
  usuarioId: number;
  email: string;
  nome: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService{
    private http = inject(HttpClient);
    private apiUrl = `${environment.apiUrl}/auth`;
    private readonly TOKEN_KEY = 'rpg_token';
    private readonly USER_KEY = 'rpg_user';

    login(dados: LoginRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.apiUrl}/login`, dados).pipe(
      tap(res => this.salvarSessao(res))
    );
  }
   registrar(dados: RegisterRequest): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.apiUrl}/cadastrar`, dados).pipe(
      tap(res => this.salvarSessao(res))
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getUsuario(): { id: number; nome: string; email: string } | null {
    const raw = localStorage.getItem(this.USER_KEY);
    return raw ? JSON.parse(raw) : null;
  }

  estaLogado(): boolean {
    return !!this.getToken();
  }

  private salvarSessao(res: TokenResponse): void {
    localStorage.setItem(this.TOKEN_KEY, res.token);
    localStorage.setItem(this.USER_KEY, JSON.stringify({
      id: res.usuarioId,
      nome: res.nome,
      email: res.email
    }));
  }
}