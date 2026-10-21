import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { PersonagemService } from '../../core/services/personagem.service';
import { PersonagemResponse } from '../../models/personagem-response';

@Component({
  selector: 'app-character-detail',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './character-detail.component.html',
  styleUrl: './character-detail.component.scss'
})
export class CharacterDetailComponent implements OnInit, OnDestroy {

  private route = inject(ActivatedRoute);
  private personagemService = inject(PersonagemService);
  private cdr = inject(ChangeDetectorRef);   // 🔥 ADICIONE
  private destroy$ = new Subject<void>();

  personagem: PersonagemResponse | null = null;
  carregando = true;
  erro: string | null = null;

  ngOnInit(): void {
    console.log('🟢 [Detail] ngOnInit chamado');

    this.route.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe(params => {
        const id = Number(params.get('id'));
        console.log('🟢 [Detail] ID recebido:', id);

        if (!id || isNaN(id)) {
          this.erro = 'ID inválido';
          this.carregando = false;
          this.cdr.detectChanges();   // 🔥
          return;
        }

        this.carregarPersonagem(id);
      });
  }

  private carregarPersonagem(id: number): void {
    this.carregando = true;
    this.erro = null;

    this.personagemService.buscarPersonagemPorId(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (p) => {
          console.log('✅ [Detail] Personagem carregado:', p);
          this.personagem = p;
          this.carregando = false;
          this.cdr.detectChanges();   // 🔥 FORÇA O CHANGE DETECTION
          console.log('✅ [Detail] detectChanges() chamado');
        },
        error: (err) => {
          console.error('❌ [Detail] Erro:', err);
          this.erro = err.status === 404
            ? 'Personagem não encontrado'
            : 'Erro ao carregar personagem';
          this.carregando = false;
          this.cdr.detectChanges();   // 🔥
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}