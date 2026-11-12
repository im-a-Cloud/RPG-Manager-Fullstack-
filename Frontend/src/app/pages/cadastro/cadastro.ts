import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-cadastro',
  imports: [CommonModule, RouterModule, FormsModule],
  standalone: true,
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.scss',
})
export class CadastroComponent implements OnInit, OnDestroy {

  id: string | null = null;
  mensagem = '';
  tipoMensagem: 'success' | 'error' | 'info' = 'info';

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);
  private timeoutMensagem?: ReturnType<typeof setTimeout>;

  novoUsuario = { nome: '', email: '', senha: '' };
  confirmarSenha: string | null = null;

  ngOnInit(): void {
    console.log('ID do personagem:', this.route.snapshot.paramMap.get('id'));
  }

  ngOnDestroy(): void {
    if (this.timeoutMensagem) clearTimeout(this.timeoutMensagem);
  }

  mostrarMensagem(texto: string, tipo: 'success' | 'error' | 'info' = 'info'): void {
    if (this.timeoutMensagem) clearTimeout(this.timeoutMensagem);

    this.mensagem = texto;
    this.tipoMensagem = tipo;
    this.cdr.detectChanges();

    this.timeoutMensagem = setTimeout(() => {
      this.mensagem = '';
      this.cdr.detectChanges();
    }, 5000);
  }

  cadastrarUsuario(): void {
    if (!this.novoUsuario.nome?.trim()) {
      this.mostrarMensagem('⚠️ O nome é obrigatório!', 'error');
      return;
    }
    if (!this.novoUsuario.email?.trim()) {
      this.mostrarMensagem('⚠️ O email é obrigatório!', 'error');
      return;
    }
    if (!this.novoUsuario.email?.includes('@')) {
      this.mostrarMensagem('⚠️ O email deve ser válido!', 'error');
      return;
    }
    if (!this.novoUsuario.senha?.trim()) {
      this.mostrarMensagem('⚠️ A senha é obrigatória!', 'error');
      return;
    }
    if (this.novoUsuario.senha !== this.confirmarSenha) {
      this.mostrarMensagem('⚠️ As senhas não coincidem!', 'error');
      return;
    }

    this.mostrarMensagem('⏳ Cadastrando usuário...', 'info');

    const dadosCadastro = {
      nome: this.novoUsuario.nome,
      email: this.novoUsuario.email,
      senha: this.novoUsuario.senha
    };

    this.authService.registrar(dadosCadastro).subscribe({
      next: () => {
        this.mostrarMensagem('✅ Usuário cadastrado com sucesso!', 'success');
        this.router.navigate(['/login']);
      },
      error: (err: HttpErrorResponse) => {
        console.error('❌ [Cadastro] erro:', err);
        console.error('   body do backend:', err.error);

        const msgBackend =
          err.error?.message ||
          (typeof err.error === 'string' ? err.error : null);

        let texto = '❌ Erro ao cadastrar usuário!';

        if (err.status === 409) {
          texto = `⚠️ ${msgBackend || 'Já existe um usuário com este email.'}`;
        } else if (err.status === 400) {
          texto = `⚠️ ${msgBackend || 'Dados inválidos.'}`;
        } else if (err.status === 0) {
          texto = '❌ Não foi possível conectar ao servidor.';
        } else {
          texto = `❌ ${msgBackend || 'Erro ao cadastrar usuário!'}`;
        }

        this.mostrarMensagem(texto, 'error');
      }
    });
  }
}