import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { timeout, finalize } from 'rxjs/operators';

import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class LoginComponent {

  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  email = '';
  senha = '';
  erro = '';
  carregando = false;

  entrar(): void {
    if (this.carregando) return;

    if (!this.email?.trim() || !this.senha?.trim()) {
      this.erro = 'Preencha email e senha';
      this.cdr.detectChanges();     
      return;
    }

    this.carregando = true;
    this.erro = '';
    this.cdr.detectChanges();          

    this.authService.login({ email: this.email, senha: this.senha })
      .pipe(
        timeout(10000),
        finalize(() => {
          this.carregando = false;
          this.cdr.detectChanges();   
        })
      )
      .subscribe({
        next: () => {
          this.cdr.detectChanges();
          this.router.navigate(['/']);
        },
        error: (err: HttpErrorResponse | Error) => {
          console.error('❌ [Login] erro:', err);

          let msg = 'Erro ao entrar. Tente novamente.';

          if (err?.name === 'TimeoutError') {
            msg = 'Servidor não respondeu. Tente novamente.';
          } else {
            const httpErr = err as HttpErrorResponse;
            const msgBackend =
              httpErr?.error?.message ||
              (typeof httpErr?.error === 'string' ? httpErr.error : null);

            if (httpErr?.status === 401)      msg = msgBackend || 'Email ou senha inválidos';
            else if (httpErr?.status === 0)   msg = 'Não foi possível conectar ao servidor.';
            else if (httpErr?.status >= 500)  msg = msgBackend || 'Erro no servidor. Tente mais tarde.';
            else                              msg = msgBackend || msg;
          }

          this.erro = msg;
          this.cdr.detectChanges(); 
        }
      });
  }
}