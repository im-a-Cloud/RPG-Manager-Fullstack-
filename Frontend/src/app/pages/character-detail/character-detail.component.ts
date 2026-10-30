import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { PersonagemService } from '../../core/services/personagem.service';
import { PersonagemResponse } from '../../models/personagem-response';

@Component({
  selector: 'app-character-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
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

  modoEdicao = false;
  salvando = false;
  mensagemSucesso: string | null = null;

  personagemEditavel: any = {};


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
ativarEdicao(): void {
  if (!this.personagem) return;

  this.personagemEditavel = {
    ...this.personagem,
    atributos: { ...this.personagem.atributos },
    classe: { ...this.personagem.classe },
    magia: { ...this.personagem.magia },
    pericias: [...(this.personagem.pericias || [])],
    habilidades: [...(this.personagem.habilidades || [])],
    proficiencia: [...(this.personagem.proficiencia || [])],
    magias: [...(this.personagem.magias || [])],
    inventario: [...(this.personagem.inventario || [])],
  };

  this.modoEdicao = true;
  this.mensagemSucesso = null;
  this.cdr.detectChanges();
}
  cancelarEdicao(): void {
    this.modoEdicao = false;
    this.personagemEditavel = {};
    this.mensagemSucesso = null;
    this.cdr.detectChanges();
  }

  salvarEdicao(): void {
    if (!this.personagem || !this.personagemEditavel) return;

    this.salvando = true;

    // Monta o DTO no formato do backend
    const dto = {
      nomePersonagem: this.personagemEditavel.nomePersonagem,
      nivelPersonagem: this.personagemEditavel.nivelPersonagem,
      classeId: this.personagem.classe.id,     // classe não é editável no form
      valorForca: this.personagemEditavel.atributos.forca,
      valorDestreza: this.personagemEditavel.atributos.destreza,
      valorConstituicao: this.personagemEditavel.atributos.constituicao,
      valorInteligencia: this.personagemEditavel.atributos.inteligencia,
      valorSabedoria: this.personagemEditavel.atributos.sabedoria,
      valorCarisma: this.personagemEditavel.atributos.carisma,

      ca: this.personagemEditavel.ca,
      iniciativa: this.personagemEditavel.iniciativa,
      pontosVida: this.personagemEditavel.pontosVidaPersonagem,
      movimento: this.personagemEditavel.movimento,

      historiaPersonagem: this.personagemEditavel.historiaPersonagem,
      aparenciaPersonagem: this.personagemEditavel.aparenciaPersonagem,
      racaPersonagem: this.personagemEditavel.racaPersonagem,
      ideaisPersonagem: this.personagemEditavel.ideaisPersonagem,
      defeitosPersonagem: this.personagemEditavel.defeitosPersonagem,
      anotacoesPersonagem: this.personagemEditavel.anotacoesPersonagem,
      personalidadePersonagem: this.personagemEditavel.personalidadePersonagem,
      escalaPersonagem: this.personagemEditavel.escalaPersonagem,
      alinhamentoPersonagem: this.personagemEditavel.alinhamentoPersonagem,
      idadePersonagem: this.personagemEditavel.idadePersonagem,
      generoPersonagem: this.personagemEditavel.generoPersonagem,
      antecedentePersonagem: this.personagemEditavel.antecedentePersonagem,
      pesoPersonagem: this.personagemEditavel.pesoPersonagem,
      alturaPersonagem: this.personagemEditavel.alturaPersonagem,
      fotoBase64: this.personagemEditavel.fotoBase64,

      proficiencias: this.personagemEditavel.proficiencia || [],
      pericias: this.personagemEditavel.pericias || [],
      habilidades: this.personagemEditavel.habilidades || [],
      magias: this.personagemEditavel.magias || [],
      inventario: this.personagemEditavel.inventario || [],
    };

    this.personagemService.atualizarPersonagem(this.personagem.id, dto)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (atualizado) => {
          console.log('✅ [Detail] Personagem atualizado:', atualizado);
          this.personagem = atualizado;
          this.modoEdicao = false;
          this.salvando = false;
          this.mensagemSucesso = 'Personagem atualizado com sucesso!';
          this.cdr.detectChanges();

          setTimeout(() => {
            this.mensagemSucesso = null;
            this.cdr.detectChanges();
          }, 3000);
        },
        error: (err) => {
          console.error('❌ [Detail] Erro ao salvar:', err);
          this.erro = 'Erro ao salvar personagem';
          this.salvando = false;
          this.cdr.detectChanges();
        }
      });
  }
  removerPericia(index: number): void {
    this.personagemEditavel.pericias.splice(index, 1);
    this.cdr.detectChanges();
  }
  onFotoSelecionada(event: any): void {
    const file = event.target.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = (e: any) => {
      this.personagemEditavel.fotoBase64 = e.target.result;
      this.cdr.detectChanges();
    };
    reader.readAsDataURL(file);
  }

  adicionarHabilidade(): void {
  this.personagemEditavel.habilidades.push({
    nomeHabilidade: 'Nova habilidade',
    descricaoHabilidade: '',
    origemHabilidade: 'OUTROS',
    usosHabilidade: '',
    recargaHabilidade: ''
  });
  this.cdr.detectChanges();
}

  removerHabilidade(index: number): void {
    this.personagemEditavel.habilidades.splice(index, 1);
    this.cdr.detectChanges();
  }
  adicionarProficiencia(): void {
  this.personagemEditavel.proficiencia.push({
    tipoProficiencia: 'OUTROS',
    listaProficiencias: ''
  });
  this.cdr.detectChanges();
}

removerProficiencia(index: number): void {
  this.personagemEditavel.proficiencia.splice(index, 1);
  this.cdr.detectChanges();
}

adicionarMagia(): void {
  this.personagemEditavel.magias.push({
    name: 'Nova magia',
    level: '0',
    casting_time: '',
    range: '',
    description: '',
    duration: '',
    school: '',
    ritual: false,
    concentration: false,
    type: '',
    classes: [],
    tags: [],
    components: {
      verbal: false,
      somatic: false,
      material: false,
      raw: ''
    }
  });
  this.cdr.detectChanges();
}

removerMagia(index: number): void {
  this.personagemEditavel.magias.splice(index, 1);
  this.cdr.detectChanges();
}

adicionarItem(): void {
  this.personagemEditavel.inventario.push({
    nomeItem: 'Novo item',
    descricaoItem: '',
    precoItem: 0,
    raridadeItem: 'COMUM',
    pesoItem: 0,
    isMagico: false,
    precisaSintonizacao: false,
    quantidade: 1,
    tipoItem: 'OUTROS'
  });
  this.cdr.detectChanges();
}

removerItem(index: number): void {
  this.personagemEditavel.inventario.splice(index, 1);
  this.cdr.detectChanges();
}

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}