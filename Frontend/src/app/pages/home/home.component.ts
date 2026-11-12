import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { PersonagemService } from '../../core/services/personagem.service';
import { PersonagemResponse } from '../../models/personagem-response';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {

  private personagemService = inject(PersonagemService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);   // ← ADICIONE

  personagens: PersonagemResponse[] = [];
  carregando = true;
  erro: string | null = null;
  logado = !!this.authService.getUsuario();
  mensagem = '';

  ngOnInit(): void {
    console.log('🟢 [Home] ngOnInit chamado');
    this.carregarPersonagens();
  }

  get nomeUsuario(): string {
    return this.authService.getUsuario()?.nome ?? 'Visitante';
  }

  carregarPersonagens(): void {
    console.log('🟢 [Home] Carregando personagens...');
    this.carregando = true;
    this.erro = null;

    this.personagemService.listarPersonagens().subscribe({
      next: (dados) => {
        console.log('✅ [Home] Dados recebidos:', dados);
        this.personagens = dados;
        this.carregando = false;
        this.cdr.detectChanges();   // 🔥 FORÇA O CHANGE DETECTION
        console.log('✅ [Home] detectChanges() chamado');
      },
      error: (err) => {
        console.error('❌ [Home] Erro:', err);
        this.erro = err.status === 401 || err.status === 403
          ? 'Você precisa estar logado para ver seus personagens.'
          : 'Não foi possível carregar os personagens.';
        this.carregando = false;
        this.cdr.detectChanges();   // 🔥 FORÇA NO ERRO TAMBÉM
      }
    });
  }
  encaminharCriacaoPersonagem(): void {
    if (!this.logado) {
      alert('Você precisa estar logado para criar um personagem.');
      this.router.navigate(['/login']);
      return;
    }
    this.router.navigate(['/criar-ficha']);
  }
  abrirDetalhesPersonagem(id: number): void {
    this.router.navigate(['/personagem', id]);
  }

  deletar(id: number, event?: Event): void {
    event?.stopPropagation();

    if (!confirm('Tem certeza que deseja deletar este personagem?')) return;

    this.personagemService.deletarPersonagem(id).subscribe({
      next: () => {
        this.personagens = this.personagens.filter(p => p.id !== id);
        this.mensagem = '🗑️ Personagem apagado com sucesso.';
        this.cdr.detectChanges();
        setTimeout(() => { this.mensagem = ''; this.cdr.detectChanges(); }, 4000);
      },
      error: (err) => {
        console.error('❌ Erro ao deletar:', err);
        this.mensagem = '❌ Não foi possível apagar o personagem.';
        this.cdr.detectChanges();
      }
    });
  }
}