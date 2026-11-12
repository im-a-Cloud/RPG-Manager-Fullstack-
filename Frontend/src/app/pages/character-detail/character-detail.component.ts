import { Component, OnInit, OnDestroy, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import { PersonagemService } from '../../core/services/personagem.service';
import { PericiaService } from '../../core/services/pericia.service';
import { PersonagemResponse } from '../../models/personagem-response';
import { Pericia } from '../../models/pericia';
import { Magia } from '../../models/magia';

type CampoAtributo =
  | 'forca' | 'destreza' | 'constituicao'
  | 'inteligencia' | 'sabedoria' | 'carisma';

type CampoBonus =
  | 'bonusForca' | 'bonusDestreza' | 'bonusConstituicao'
  | 'bonusInteligencia' | 'bonusSabedoria' | 'bonusCarisma';

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
  private periciaService = inject(PericiaService);
  private cdr = inject(ChangeDetectorRef);
  private destroy$ = new Subject<void>();

  magiaSelecionada: any = null;   
  modalAberto = false;
  magiaEditando: any = null;
  magiaOriginal: any = null;   // null = nova; referência = edição
  personagem: PersonagemResponse | null = null;
  carregando = true;
  erro: string | null = null;

  modoEdicao = false;
  salvando = false;
  mensagemSucesso: string | null = null;

  personagemEditavel: any = {};

  catalogoPericias: Pericia[] = [];
  periciasMarcadas = new Set<string>();

  itemSelecionado: any = null;

  private readonly mapaAbreviacao: Record<string, string> = {
    FORCA: 'FOR', DESTREZA: 'DES', CONSTITUICAO: 'CON',
    INTELIGENCIA: 'INT', SABEDORIA: 'SAB', CARISMA: 'CAR'
  };

  // ============================================
  // CICLO DE VIDA
  // ============================================
  ngOnInit(): void {
    console.log('🟢 [Detail] ngOnInit chamado');

    this.periciaService.listar()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (lista) => {
          this.catalogoPericias = lista;
          console.log('✅ [Detail] Catálogo de perícias:', lista.length);
          this.cdr.detectChanges();
        },
        error: (err) => console.error('❌ [Detail] Erro ao carregar catálogo:', err)
      });

    this.route.paramMap
      .pipe(takeUntil(this.destroy$))
      .subscribe(params => {
        const id = Number(params.get('id'));
        console.log('🟢 [Detail] ID recebido:', id);

        if (!id || isNaN(id)) {
          this.erro = 'ID inválido';
          this.carregando = false;
          this.cdr.detectChanges();
          return;
        }

        this.carregarPersonagem(id);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ============================================
  // CARREGAR
  // ============================================
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
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('❌ [Detail] Erro:', err);
          this.erro = err.status === 404
            ? 'Personagem não encontrado'
            : 'Erro ao carregar personagem';
          this.carregando = false;
          this.cdr.detectChanges();
        }
      });
  }

  // ============================================
  // EDIÇÃO
  // ============================================
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

  this.periciasMarcadas = new Set(
    (this.personagem.pericias ?? [])
      .filter(p => p.proficiente === true)
      .map(p => p.slug)
  );

  this.modoEdicao = true;
  this.mensagemSucesso = null;
  this.cdr.detectChanges();
}

  cancelarEdicao(): void {
    this.modoEdicao = false;
    this.personagemEditavel = {};
    this.periciasMarcadas = new Set<string>();
    this.mensagemSucesso = null;
    this.cdr.detectChanges();
  }

  salvarEdicao(): void {
    if (!this.personagem || !this.personagemEditavel) return;

    this.salvando = true;

    for (const magia of this.personagemEditavel.magias || []) {
      const magiasValidas = (this.personagemEditavel.magias ?? [])
        .filter((m: Magia) => this.podeAprenderEssaMagia(m));

      const removidas = (this.personagemEditavel.magias ?? []).length - magiasValidas.length;
      if (removidas > 0) {
        console.warn(`⚠️ ${removidas} magia(s) inválida(s) removida(s).`);
        this.personagemEditavel.magias = magiasValidas;
      }
    }

    const dto = {
      nomePersonagem: this.personagemEditavel.nomePersonagem,
      nivelPersonagem: this.personagemEditavel.nivelPersonagem,
      classeId: this.personagem.classe.id,
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
      pericias: this.montarPayloadPericias(),
      habilidades: this.personagemEditavel.habilidades || [],
      magias: this.personagemEditavel.magias || [],
      inventario: this.personagemEditavel.inventario || [],
    };
    console.log('📤 [PUT] dto completo:', JSON.stringify(dto, null, 2));
    this.personagemService.atualizarPersonagem(this.personagem.id, dto)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (atualizado) => {
          console.log('✅ [Detail] Personagem atualizado:', atualizado);
          this.personagem = atualizado;
          this.modoEdicao = false;
          this.salvando = false;
          this.periciasMarcadas = new Set<string>();
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

  // ============================================
  // FOTO
  // ============================================
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

  // ============================================
  // HABILIDADES
  // ============================================
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

  // ============================================
  // PROFICIÊNCIAS
  // ============================================
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

  // ============================================
  // MAGIAS
  // ============================================

  removerMagia(index: number): void {
    this.personagemEditavel.magias.splice(index, 1);
    this.cdr.detectChanges();
  }

  // ============================================
  // INVENTÁRIO
  // ============================================
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

  // ============================================
  // PERÍCIAS — helpers do checkbox
  // ============================================
  abreviarAtributo(attr: string): string {
    return this.mapaAbreviacao[attr] ?? attr;
  }

  isSelecionada(slug: string): boolean {
    return this.periciasMarcadas.has(slug);
  }

  togglePericia(slug: string): void {
    if (this.periciasMarcadas.has(slug)) {
      this.periciasMarcadas.delete(slug);
    } else {
      this.periciasMarcadas.add(slug);
    }
  }

  getValorPericia(slug: string): string {
    const pericia = this.catalogoPericias.find(p => p.slug === slug);
    if (!pericia) return '';

    const attr = this.atributoParaCampo(pericia.atributoChave);

    const attrsEdicao = this.personagemEditavel?.atributos as Record<string, number> | undefined;
    const attrsView   = this.personagem?.atributos           as Record<string, number> | undefined;
    const valorAttr = attrsEdicao?.[attr] ?? attrsView?.[attr] ?? 10;

    const mod = Math.floor((valorAttr - 10) / 2);
    const bonusProf = this.personagem?.magia?.bonusProficiencia ?? 0;
    const marcada = this.modoEdicao
      ? this.isSelecionada(slug)
      : this.temPericia(slug);

    const total = mod + (marcada ? bonusProf : 0);
    return total >= 0 ? `+${total}` : `${total}`;
  }

  temPericia(slug: string): boolean {
    return (this.personagem?.pericias ?? []).some((p: any) => p.slug === slug);
  }

  private atributoParaCampo(attr: string): CampoAtributo {
    switch (attr) {
      case 'FORCA': return 'forca';
      case 'DESTREZA': return 'destreza';
      case 'CONSTITUICAO': return 'constituicao';
      case 'INTELIGENCIA': return 'inteligencia';
      case 'SABEDORIA': return 'sabedoria';
      case 'CARISMA': return 'carisma';
      default: return 'forca';
    }
  }

  private montarPayloadPericias(): { slug: string; proficiente: boolean }[] {
    return this.catalogoPericias
      .filter(p => this.periciasMarcadas.has(p.slug))
      .map(p => ({ slug: p.slug, proficiente: true }));
  }

  // ============================================
  // SALVAGUARDA — corrige bug do template
  // ============================================
  getBonusSalvaguarda(atributo: string): string {
    if (!this.personagem?.atributos) return '+0';

    const a = this.personagem.atributos;

    const mapaBonus: Record<CampoBonus, number> = {
      bonusForca:        a.bonusForca,
      bonusDestreza:     a.bonusDestreza,
      bonusConstituicao: a.bonusConstituicao,
      bonusInteligencia: a.bonusInteligencia,
      bonusSabedoria:    a.bonusSabedoria,
      bonusCarisma:      a.bonusCarisma
    };

    const mapaCampo: Record<string, CampoBonus> = {
      FORCA:        'bonusForca',
      DESTREZA:     'bonusDestreza',
      CONSTITUICAO: 'bonusConstituicao',
      INTELIGENCIA: 'bonusInteligencia',
      SABEDORIA:    'bonusSabedoria',
      CARISMA:      'bonusCarisma'
    };

    const campo = mapaCampo[atributo];
    const b = campo ? mapaBonus[campo] : 0;
    return b >= 0 ? `+${b}` : `${b}`;
  }

// ============================================
// AGRUPAMENTO POR NÍVEL
// ============================================
getMagiasPorNivel(nivel: string): any[] {
  return (this.personagem?.magias ?? []).filter((m: any) => String(m.level) === nivel);
}

getMagiasEditaveisPorNivel(nivel: string): any[] {
  return (this.personagemEditavel?.magias ?? []).filter((m: any) => String(m.level) === nivel);
}

removerMagiaPorReferencia(magia: any): void {
  const idx = this.personagemEditavel.magias.indexOf(magia);
  if (idx >= 0) {
    this.personagemEditavel.magias.splice(idx, 1);
    this.cdr.detectChanges();
  }
}

  adicionarMagia(): void {

  if (!this.personagemEditavel?.classe?.isConjurador) {
    console.warn('⚠️ Personagem não é conjurador');
    return;
  }
    this.personagemEditavel.magias.push({
      name: 'Nova magia',
      level: '0',
      casting_time: '',
      range: '',
      description: '',
      duration: '',
      school: 'EVOCACAO',
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

  selecionarMagia(magia: any): void {
    this.magiaSelecionada = (this.magiaSelecionada === magia) ? null : magia;
  }

  isItemSelecionado(item: any): boolean {
    return this.itemSelecionado === item;
  }

  selecionarItem(item: any): void {
    this.itemSelecionado = (this.itemSelecionado === item) ? null : item;
  }
  isMagiaSelecionada(magia: any): boolean {
    return this.magiaSelecionada === magia;
  }
  abrirModal(magia?: any, event?: Event): void {
    event?.stopPropagation();

    if (magia) {
      this.magiaEditando = structuredClone(magia);
      this.magiaOriginal = magia;

      // se o nível da magia atual for inválido, ajusta para o máximo permitido
      if (!this.podeAprenderEssaMagia(this.magiaEditando)) {
        const max = this.getNivelMaximoMagia();
        this.magiaEditando.level = String(max);
      }
    } else {
      this.magiaEditando = {
        name: '', level: '0', casting_time: '', range: '',
        description: '', duration: '', school: 'EVOCACAO',
        ritual: false, concentration: false, type: '',
        classes: [], tags: [],
        components: { verbal: false, somatic: false, material: false, raw: '' }
      };
      this.magiaOriginal = null;
    }
    this.modalAberto = true;
  }

  fecharModal(): void {
    this.modalAberto = false;
    this.magiaEditando = null;
    this.magiaOriginal = null;
  }

salvarMagia(): void {
  if (!this.magiaEditando) return;

  if (!this.podeAprenderEssaMagia(this.magiaEditando)) {
    alert(`⚠️ Esta magia é de nível ${this.magiaEditando.level}, mas o personagem só pode aprender até nível ${this.getNivelMaximoMagia()}.`);
    return;
  }

  if (this.magiaOriginal) {
    const idx = this.personagemEditavel.magias.indexOf(this.magiaOriginal);
    if (idx >= 0) this.personagemEditavel.magias[idx] = structuredClone(this.magiaEditando);
  } else {
    this.personagemEditavel.magias.push(structuredClone(this.magiaEditando));
  }

  this.fecharModal();
  this.cdr.detectChanges();
}
  getPercepcaoPassiva(): number {
    const p = (this.personagem?.pericias as any[])?.find(x => x.slug === 'percepcao');
    return (p?.valorTotalPericia ?? 0) + 10;
  }

  getCdMagia(): number {
    const classe = this.personagemEditavel?.classe ?? this.personagem?.classe;
    if (!classe?.isConjurador || !classe.atributoConjuracao) return 0;

    return 8 + this.getBonusProficiencia() + this.getBonusAtributo(classe.atributoConjuracao);
  }

  getAtaqueMagico(): number {
    const classe = this.personagemEditavel?.classe ?? this.personagem?.classe;
    if (!classe?.isConjurador || !classe.atributoConjuracao) return 0;

    return this.getBonusProficiencia() + this.getBonusAtributo(classe.atributoConjuracao);
  }

  getBonusAtributo(atributo: string): number {
    const attrs = this.personagemEditavel?.atributos ?? this.personagem?.atributos;
    if (!attrs) return 0;

    switch (atributo) {
      case 'FORCA':        return attrs.bonusForca ?? Math.floor((attrs.forca - 10) / 2);
      case 'DESTREZA':     return attrs.bonusDestreza ?? Math.floor((attrs.destreza - 10) / 2);
      case 'CONSTITUICAO': return attrs.bonusConstituicao ?? Math.floor((attrs.constituicao - 10) / 2);
      case 'INTELIGENCIA': return attrs.bonusInteligencia ?? Math.floor((attrs.inteligencia - 10) / 2);
      case 'SABEDORIA':    return attrs.bonusSabedoria ?? Math.floor((attrs.sabedoria - 10) / 2);
      case 'CARISMA':      return attrs.bonusCarisma ?? Math.floor((attrs.carisma - 10) / 2);
      default: return 0;
    }
  }
  private getBonusProficiencia(): number {
    return this.personagemEditavel?.magia?.bonusProficiencia
        ?? this.personagem?.magia?.bonusProficiencia
        ?? 0;
  }

  private normalizarTipoConjuracao(tipo: any): 'COMPLETO' | 'MEIO' | 'TERCIARIO' | 'NENHUM' {
    if (tipo === null || tipo === undefined) return 'NENHUM';

    if (typeof tipo === 'number') {
      return ['NENHUM', 'COMPLETO', 'MEIO', 'TERCIARIO'][tipo] as any ?? 'NENHUM';
    }

    const s = String(tipo).toUpperCase();
    if (s === 'COMPLETO' || s === 'MEIO' || s === 'TERCIARIO' || s === 'NENHUM') {
      return s as any;
    }

    return 'NENHUM';
  }

  podeAprenderEssaMagia(magia: Magia): boolean {
    const tipo = this.normalizarTipoConjuracao(this.personagemEditavel?.classe?.tipo);
    if (tipo === 'NENHUM') return false;

    const nivelMagia = Number(magia.level ?? 0);
    const nivelPersonagem = this.personagemEditavel?.nivelPersonagem ?? 1;

    switch (tipo) {
      case 'COMPLETO':  return nivelMagia <= Math.ceil(nivelPersonagem / 2);
      case 'MEIO':      return nivelMagia <= Math.ceil(nivelPersonagem / 4);
      case 'TERCIARIO': return nivelMagia <= Math.ceil(nivelPersonagem / 6);
      default:          return false;
    }
  }

  private getNivelMaximoMagia(): number {
    const tipo = this.normalizarTipoConjuracao(this.personagemEditavel?.classe?.tipo);
    const nivelPersonagem = this.personagemEditavel?.nivelPersonagem ?? 1;

    switch (tipo) {
      case 'COMPLETO':  return Math.ceil(nivelPersonagem / 2);
      case 'MEIO':      return Math.ceil(nivelPersonagem / 4);
      case 'TERCIARIO': return Math.ceil(nivelPersonagem / 6);
      default:          return 0;
    }
  }
  podeAprenderNivel(nivel: string | number): boolean {
    return this.podeAprenderEssaMagia({ level: String(nivel) } as Magia);
  }
}