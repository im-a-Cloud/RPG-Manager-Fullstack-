import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {ChangeDetectionStrategy, } from '@angular/core';

import { PersonagemService } from '../../core/services/personagem.service';
import { RpgService } from '../../core/services/rpg.service';
import { ClasseService } from '../../core/services/classe.service';
import { HabilidadeService } from '../../core/services/habilidade.service';
import { ProficienciaService } from '../../core/services/proficiencia.service';
import { MagiaService } from '../../core/services/magia.service';
import { ItemService } from '../../core/services/item.service';
import { PericiaService } from '../../core/services/pericia.service';

import { Personagem } from '../../models/personagem';
import { Habilidade } from '../../models/habilidade';
import { Proficiencia } from '../../models/proficiencia';
import { Magia } from '../../models/magia';
import { Item } from '../../models/item';
import { Classe } from '../../models/classe';
import { Pericia } from '../../models/pericia';
import { finalize } from 'rxjs/operators';

import { TipoItem } from '../../models/enums/tipoItem.enum';
import { RaridadeItem } from '../../models/enums/raridadeItem.enum';
import { TipoProficiencia } from '../../models/enums/tipoProficiencia.enum';
import { OrigemHabilidade } from '../../models/enums/origemHabilidade.enum';

@Component({
  selector: 'app-criar-ficha-personagem',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './criar-ficha-personagem.html',
  styleUrl: './criar-ficha-personagem.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CriarFichaPersonagemComponent implements OnInit {

  private rpgService = inject(RpgService);
  private personagemService = inject(PersonagemService);
  private classeService = inject(ClasseService);
  private habilidadeService = inject(HabilidadeService);
  private proficienciaService = inject(ProficienciaService);
  private magiaService = inject(MagiaService);
  private itemService = inject(ItemService);
  private periciaService = inject(PericiaService);

  modalAberto = false;
  salvando = false;
  classesDisponiveis: Classe[] = [];

  personagem: Personagem = {
    nomePersonagem: '',
    nivelPersonagem: 1,
    valorForca: 10,
    valorDestreza: 10,
    valorConstituicao: 10,
    valorInteligencia: 10,
    valorSabedoria: 10,
    valorCarisma: 10,
    caPersonagem: 10,
    iniciativaPersonagem: 0,
    movimentoPersonagem: 9,
    pontosVidaPersonagem: 10,

    historiaPersonagem: '',
    aparenciaPersonagem: '',
    racaPersonagem: '',
    escalaPersonagem: '',
    antecedentePersonagem: '',
    alinhamentoPersonagem: '',
    ideaisPersonagem: '',
    defeitosPersonagem: '',
    anotacoesPersonagem: '',
    personalidadePersonagem: '',
    alturaPersonagem: 0,
    generoPersonagem: '',
    idadePersonagem: 0,
    pesoPersonagem: 0.0,
    habilidades: [],
    periciasPersonagem: [],
    proficienciasPersonagem: [],
    inventarioPersonagem: [],
    magias: []
  };

  // ============================================
  // IMAGEM
  // ============================================
  imagemPreview: string | null = null;
  nomeArquivo = '';
  imagemBase64: string | null = null;

  alertaAberto = false;
  alertaTexto = '';
  alertaTipo: 'success' | 'error' | 'info' = 'info';

  // ============================================
  // UI
  // ============================================
  visivel: { [key: string]: boolean } = {
    proficiencia: false,
    item: false,
    habilidade: false,
    magia: false
  };
  mensagem = '';
  tipoMensagem: 'success' | 'error' | 'info' = 'info';

  // ============================================
  // FORMULÁRIOS AUXILIARES
  // ============================================
  novaHabilidade: Partial<Habilidade> = {
    nomeHabilidade: '',
    origemHabilidade: OrigemHabilidade.OUTROS,
    descricaoHabilidade: '',
    usosHabilidade: 0,
    recargaHabilidade: ''
  };

  novaProficiencia: Partial<Proficiencia> = {
    tipoProficiencia: TipoProficiencia.OUTROS,
    listaProficiencias: ''
  };

  itemSelecionado: Item | null = null;

  novoItem: Partial<Item> = {
    nomeItem: '',
    tipoItem: TipoItem.ARMA,
    descricaoItem: '',
    precoItem: 0,
    raridadeItem: RaridadeItem.COMUM,
    pesoItem: 0,
    isMagicoItem: false,
    precisaSintonizacao: false,
    quantidadeItem: 1
  };

  magiaSelecionada: any = null;
  magiaOriginal: any = null; 

  novaMagia: Magia = this.magiaVazia();

  // ============================================
  // PERÍCIAS (fonte de verdade = backend)
  // ============================================
  pericias: Pericia[] = [];
  slugsSelecionados = new Set<string>();

  // ============================================
  // RESISTÊNCIAS
  // ============================================
  resistencias: { [key: string]: boolean } = {
    forca: false,
    destreza: false,
    constituicao: false,
    inteligencia: false,
    sabedoria: false,
    carisma: false
  };

  // ============================================
  // MAPA DE ABREVIAÇÃO DE ATRIBUTO
  // ============================================
  private readonly mapaAbreviacao: Record<string, string> = {
    FORCA: 'FOR',
    DESTREZA: 'DES',
    CONSTITUICAO: 'CON',
    INTELIGENCIA: 'INT',
    SABEDORIA: 'SAB',
    CARISMA: 'CAR'
  };

  // ============================================
  // CONSTRUTOR
  // ============================================
  constructor(private cdr: ChangeDetectorRef) {
    this.calcularValoresAutomaticos();
  }

  // ============================================
  // CICLO DE VIDA
  // ============================================
  ngOnInit(): void {
    this.carregarClasses();
    this.carregarPericias();
  }

  // ============================================
  // CARREGAR DADOS DO BACKEND
  // ============================================
  carregarClasses(): void {
    this.classeService.listarClasses().subscribe({
      next: (classes: any[]) => {
        classes.forEach(classe => {
        });
        this.classesDisponiveis = classes;
      },
      error: (erro) => console.error('❌ Erro ao carregar classes:', erro)
    });
  }

  carregarPericias(): void {
    this.periciaService.listar().subscribe({
      next: (pericias) => {
        this.pericias = pericias;
      },
      error: (err) => console.error('❌ Erro ao carregar perícias', err)
    });
  }

  // ============================================
  // PERÍCIAS - SELEÇÃO
  // ============================================
  togglePericia(slug: string): void {
    if (this.slugsSelecionados.has(slug)) {
      this.slugsSelecionados.delete(slug);
    } else {
      this.slugsSelecionados.add(slug);
    }
  }

  isSelecionada(slug: string): boolean {
    return this.slugsSelecionados.has(slug);
  }

  // ============================================
  // UTILIDADES
  // ============================================
  normalizarTexto(str: string): string {
    return str
      ?.toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim() || '';
  }

  abreviarAtributo(attr: string): string {
    return this.mapaAbreviacao[attr] ?? attr;
  }

  // ============================================
  // CÁLCULOS AUTOMÁTICOS
  // ============================================
  calcularValoresAutomaticos(): void {
    const destrezaBonus = this.rpgService.calcularBonusAtributo(this.personagem.valorDestreza);
    this.personagem.caPersonagem = 10 + destrezaBonus;
    this.personagem.iniciativaPersonagem = destrezaBonus;
    this.personagem.movimentoPersonagem = 9;

    const constBonus = this.rpgService.calcularBonusAtributo(this.personagem.valorConstituicao);
    this.personagem.pontosVidaPersonagem = (this.personagem.nivelPersonagem || 1) * 6 + constBonus;
  }

  // ============================================
  // COMPARAR/SELECIONAR CLASSE
  // ============================================
  compararClasses(classe1: Classe | null, classe2: Classe | null): boolean {
    if (!classe1 || !classe2) return false;
    return classe1.id === classe2.id;
  }

  onClasseSelecionada(classe: Classe | null): void {
    if (classe) {
      this.personagem.classePersonagem = {
        ...classe,
        id: classe.id || 0
      };
      this.calcularValoresAutomaticos();
    }
  }

  // ============================================
  // GETTERS - ATRIBUTOS
  // ============================================
  get bonusForca() { return this.rpgService.calcularBonusAtributo(this.personagem.valorForca); }
  get bonusDestreza() { return this.rpgService.calcularBonusAtributo(this.personagem.valorDestreza); }
  get bonusConstituicao() { return this.rpgService.calcularBonusAtributo(this.personagem.valorConstituicao); }
  get bonusInteligencia() { return this.rpgService.calcularBonusAtributo(this.personagem.valorInteligencia); }
  get bonusSabedoria() { return this.rpgService.calcularBonusAtributo(this.personagem.valorSabedoria); }
  get bonusCarisma() { return this.rpgService.calcularBonusAtributo(this.personagem.valorCarisma); }
  get bonusProficiencia() { return this.rpgService.calcularBonusProficiencia(this.personagem.nivelPersonagem || 1); }

  getBonusAtributo(atributo: string) {
    const atributoNormalizado = this.normalizarTexto(atributo);
    const atributos = {
      forca: this.personagem.valorForca,
      destreza: this.personagem.valorDestreza,
      constituicao: this.personagem.valorConstituicao,
      inteligencia: this.personagem.valorInteligencia,
      sabedoria: this.personagem.valorSabedoria,
      carisma: this.personagem.valorCarisma
    };
    return this.rpgService.getBonusAtributo(atributoNormalizado, atributos);
  }

  // ============================================
  // PERÍCIAS — VALOR TOTAL (tempo real)
  // ============================================
  getValorPericia(slug: string): number {
    const pericia = this.pericias.find(p => p.slug === slug);
    if (!pericia) return 0;

    const modificador = this.getBonusAtributo(pericia.atributoChave);
    const prof = this.isSelecionada(slug) ? this.bonusProficiencia : 0;
    return modificador + prof;
  }

  getPercepcaoPassiva(): number {
    return 10 + this.getValorPericia('percepcao');
  }

  // ============================================
  // TESTES DE RESISTÊNCIA
  // ============================================
  getTesteResistencia(atributo: string) {
    const atributos = {
      forca: this.personagem.valorForca,
      destreza: this.personagem.valorDestreza,
      constituicao: this.personagem.valorConstituicao,
      inteligencia: this.personagem.valorInteligencia,
      sabedoria: this.personagem.valorSabedoria,
      carisma: this.personagem.valorCarisma
    };
    return this.rpgService.getTesteResistencia(
      atributo,
      this.resistencias,
      this.personagem.nivelPersonagem || 1,
      atributos
    );
  }

  // ============================================
  // GETTERS - INVENTÁRIO
  // ============================================
  get pesoTotal(): number {
    return (this.personagem.inventarioPersonagem || []).reduce((total, item) => {
      const peso = Number(item.pesoItem) || 0;
      const quantidade = Number(item.quantidadeItem) || 1;
      return total + (peso * quantidade);
    }, 0);
  }

  get quantidadeTotal(): number {
    return (this.personagem.inventarioPersonagem || []).reduce((total, item) => {
      return total + (Number(item.quantidadeItem) || 1);
    }, 0);
  }

  // ============================================
  // GETTERS - MAGIAS
  // ============================================
  get magiasPorNivel() {
    const grupos: { [nivel: string]: Magia[] } = {};
    for (const magia of this.personagem.magias || []) {
      const nivel = magia.level || '0';
      if (!grupos[nivel]) grupos[nivel] = [];
      grupos[nivel].push(magia);
    }
    return grupos;
  }

  getNivelFormatado(nivel: string): string {
    return nivel === '0' ? 'Truques' : `${nivel}º Nível`;
  }

  getMagiasPorNivel(nivel: string): Magia[] {
    return this.magiasPorNivel[nivel] || [];
  }

  // ============================================
  // IMAGEM
  // ============================================
  onSelectedFile(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    this.nomeArquivo = file.name;

    const reader = new FileReader();
    reader.onload = (e: any) => {
      const img = new Image();
      img.onload = () => {
        const MAX = 400;
        let w = img.width;
        let h = img.height;

        if (w > h && w > MAX) {
          h = Math.round(h * MAX / w);
          w = MAX;
        } else if (h > MAX) {
          w = Math.round(w * MAX / h);
          h = MAX;
        }

        const canvas = document.createElement('canvas');
        canvas.width = w;
        canvas.height = h;
        const ctx = canvas.getContext('2d');
        if (!ctx) return;

        ctx.drawImage(img, 0, 0, w, h);

        const dataUrl = canvas.toDataURL('image/jpeg', 0.8);

        this.imagemPreview = dataUrl;
        this.imagemBase64 = dataUrl;
      };
      img.src = e.target.result;
    };
    reader.readAsDataURL(file);
  }

  removerImagem(): void {
    this.imagemPreview = null;
    this.imagemBase64 = null;
    this.nomeArquivo = '';
    const fileInput = document.getElementById('imagemPersonagem') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  // ============================================
  // UI HELPERS
  // ============================================
  toggleSecao(secao: string): void {
    this.visivel[secao] = !this.visivel[secao];
  }

  mostrarSecao(secao: string): void { this.visivel[secao] = true; }
  esconderSecao(secao: string): void { this.visivel[secao] = false; }
  voltar(): void { window.history.back(); }

  // ============================================
  // MENSAGENS
  // ============================================
  mostrarMensagem(texto: string, tipo: 'success' | 'error' | 'info' = 'info'): void {
    console.log('🔍 [mostrarMensagem] CHAMADO', { texto, tipo });
    this.alertaTexto = texto;
    this.alertaTipo = tipo;
    this.alertaAberto = true;
    console.log('🔍 [mostrarMensagem] alertaAberto =', this.alertaAberto);
    this.cdr.detectChanges();
  }

  fecharAlerta(): void {
  this.alertaAberto = false;
  this.alertaTexto = '';
  this.cdr.detectChanges();
  }
  // ============================================
  // HABILIDADES
  // ============================================
  adicionarHabilidade(): void {
    if (!this.novaHabilidade.nomeHabilidade?.trim()) {
      this.mostrarMensagem('⚠️ O nome da habilidade é obrigatório!', 'error');
      return;
    }
    if (!this.novaHabilidade.origemHabilidade) {
      this.mostrarMensagem('⚠️ A origem da habilidade é obrigatória!', 'error');
      return;
    }
    if (!this.novaHabilidade.descricaoHabilidade?.trim()) {
      this.mostrarMensagem('⚠️ A descrição da habilidade é obrigatória!', 'error');
      return;
    }
    if (this.novaHabilidade.usosHabilidade === undefined || this.novaHabilidade.usosHabilidade < 0) {
      this.mostrarMensagem('⚠️ O número de usos deve ser 0 ou mais!', 'error');
      return;
    }
    if (!this.novaHabilidade.recargaHabilidade?.trim()) {
      this.mostrarMensagem('⚠️ A recarga da habilidade é obrigatória!', 'error');
      return;
    }

    const dadosHabilidade = {
      nomeHabilidade: this.novaHabilidade.nomeHabilidade,
      origemHabilidade: this.novaHabilidade.origemHabilidade,
      descricaoHabilidade: this.novaHabilidade.descricaoHabilidade,
      usosHabilidade: this.novaHabilidade.usosHabilidade,
      recargaHabilidade: this.novaHabilidade.recargaHabilidade
    };

    if (!this.personagem.habilidades) this.personagem.habilidades = [];
    this.personagem.habilidades.push(dadosHabilidade);

    this.mostrarMensagem(`✅ Habilidade "${dadosHabilidade.nomeHabilidade}" adicionada!`, 'success');

    this.novaHabilidade = {
      nomeHabilidade: '',
      origemHabilidade: OrigemHabilidade.OUTROS,
      descricaoHabilidade: '',
      usosHabilidade: 0,
      recargaHabilidade: ''
    };
    this.visivel['habilidade'] = false;
  }

  removerHabilidade(index: number): void {
    if (this.personagem.habilidades) {
      this.personagem.habilidades.splice(index, 1);
      this.mostrarMensagem('🗑️ Habilidade removida.', 'info');
    }
  }

  // ============================================
  // PROFICIÊNCIAS
  // ============================================
  salvarProficiencia(): void {
    if (!this.novaProficiencia.tipoProficiencia?.trim()) {
      this.mostrarMensagem('⚠️ O tipo de proficiência é obrigatório!', 'error');
      return;
    }
    if (!this.novaProficiencia.listaProficiencias?.trim()) {
      this.mostrarMensagem('⚠️ A descrição da proficiência é obrigatória!', 'error');
      return;
    }

    const dadosProficiencia = {
      tipoProficiencia: this.novaProficiencia.tipoProficiencia,
      listaProficiencias: this.novaProficiencia.listaProficiencias
    };

    this.proficienciaService.criarProficiencia(dadosProficiencia).subscribe({
      next: (response) => {
        if (!this.personagem.proficienciasPersonagem) {
          this.personagem.proficienciasPersonagem = [];
        }
        const novaProficiencia = {
          tipoProficiencia: response.tipoProficiencia || dadosProficiencia.tipoProficiencia,
          listaProficiencias: response.descricaoProficiencia || dadosProficiencia.listaProficiencias,
        };
        this.personagem.proficienciasPersonagem.push(novaProficiencia);
        this.cdr.detectChanges();
        this.mostrarMensagem(`✅ Proficiência salva!`, 'success');
      },
      error: (error) => {
        console.error('❌ Erro ao salvar:', error);
        this.mostrarMensagem('❌ Erro ao salvar proficiência.', 'error');
      }
    });
  }

  adicionarProficiencia(): void {
    if (!this.novaProficiencia.listaProficiencias?.trim()) {
      this.mostrarMensagem('⚠️ A descrição da proficiência é obrigatória!', 'error');
      return;
    }
    const dadosProficiencia = {
      tipoProficiencia: this.novaProficiencia.tipoProficiencia || TipoProficiencia.OUTROS,
      listaProficiencias: this.novaProficiencia.listaProficiencias
    };
    if (!this.personagem.proficienciasPersonagem) this.personagem.proficienciasPersonagem = [];
    this.personagem.proficienciasPersonagem.push(dadosProficiencia);

    this.mostrarMensagem(`✅ Proficiência "${dadosProficiencia.listaProficiencias}" adicionada!`, 'success');

    this.novaProficiencia = {
      tipoProficiencia: TipoProficiencia.OUTROS,
      listaProficiencias: ''
    };
  }

  removerProficiencia(index: number): void {
    if (this.personagem.proficienciasPersonagem) {
      const removido = this.personagem.proficienciasPersonagem[index].tipoProficiencia;
      this.personagem.proficienciasPersonagem.splice(index, 1);
      this.mostrarMensagem(`🗑️ "${removido}" removido.`, 'info');
    }
  }

  // ============================================
  // ITENS
  // ============================================
  selecionarItem(item: any): void {
    this.itemSelecionado = (this.itemSelecionado === item) ? null : item;
  }

  isItemSelecionado(item: any): boolean {
    return this.itemSelecionado === item;
  }

  salvarItem(): void {
    if (!this.novoItem.nomeItem?.trim()) {
      this.mostrarMensagem('⚠️ O nome do item é obrigatório!', 'error');
      return;
    }
    if (!this.novoItem.tipoItem) {
      this.mostrarMensagem('⚠️ O tipo do item é obrigatório!', 'error');
      return;
    }
    if (!this.novoItem.quantidadeItem || this.novoItem.quantidadeItem < 1) {
      this.mostrarMensagem('⚠️ A quantidade deve ser pelo menos 1!', 'error');
      return;
    }
    if (this.novoItem.precoItem === undefined || this.novoItem.precoItem === null || this.novoItem.precoItem < 0) {
      this.mostrarMensagem('⚠️ O preço não pode ser negativo!', 'error');
      return;
    }
    if (this.novoItem.pesoItem === undefined || this.novoItem.pesoItem === null || this.novoItem.pesoItem < 0) {
      this.mostrarMensagem('⚠️ O peso não pode ser negativo!', 'error');
      return;
    }

    const dadosItem = {
      nomeItem: this.novoItem.nomeItem,
      descricaoItem: this.novoItem.descricaoItem || "",
      precoItem: this.novoItem.precoItem ?? 1,
      raridadeItem: this.novoItem?.raridadeItem || undefined,
      pesoItem: this.novoItem.pesoItem ?? 0.1,
      isMagicoItem: this.novoItem.isMagicoItem === true,
      precisaSintonizacao: this.novoItem?.precisaSintonizacao === true,
      quantidadeItem: this.novoItem.quantidadeItem ?? 1,
      tipoItem: this.novoItem.tipoItem
    };

    this.itemService.criarItem(dadosItem).subscribe({
      next: (response) => {
        if (!this.personagem.inventarioPersonagem) {
          this.personagem.inventarioPersonagem = [];
        }
        const novoItem = {
          idItem: response.idItem || response.id,
          nomeItem: response.nomeItem || dadosItem.nomeItem,
          descricaoItem: response.descricaoItem || dadosItem.descricaoItem,
          precoItem: response.precoItem || dadosItem.precoItem,
          raridadeItem: response.raridadeItem || dadosItem.raridadeItem,
          pesoItem: response.pesoItem || dadosItem.pesoItem,
          isMagicoItem: response.isMagicoItem ?? response.isMagico ?? dadosItem.isMagicoItem,
          precisaSintonizacao: response.precisaSintonizacao || dadosItem.precisaSintonizacao,
          quantidadeItem: response.quantidadeItem ?? response.quantidade ?? dadosItem.quantidadeItem,
          tipoItem: response.tipoItem || dadosItem.tipoItem
        };
        this.personagem.inventarioPersonagem.push(novoItem);
        this.cdr.detectChanges();
        this.mostrarMensagem(`Item "${novoItem.nomeItem}" adicionado!`, 'success');

        setTimeout(() => {
          this.novoItem = {
            nomeItem: '',
            tipoItem: TipoItem.ARMA,
            descricaoItem: '',
            precoItem: 0,
            raridadeItem: RaridadeItem.COMUM,
            pesoItem: 0,
            isMagicoItem: false,
            precisaSintonizacao: false,
            quantidadeItem: 1
          };
          this.visivel['item'] = false;
        }, 0);
      },
      error: (error) => {
        console.error('❌ Erro ao salvar item:', error);
        this.mostrarMensagem('❌ Erro ao salvar item. Tente novamente.', 'error');
      }
    });
  }

  removerItem(index: number): void {
    if (this.personagem.inventarioPersonagem) {
      this.personagem.inventarioPersonagem.splice(index, 1);
      this.mostrarMensagem('🗑️ Item removido do inventário.', 'info');
    }
  }

  // ============================================
  // MAGIAS
  // ============================================
  selecionarMagia(magia: any): void {
    this.magiaSelecionada = (this.magiaSelecionada === magia) ? null : magia;
  }

  isMagiaSelecionada(magia: any): boolean {
    return this.magiaSelecionada === magia;
  }

  removerMagia(index: number): void {
    if (this.personagem.magias) {
      this.personagem.magias.splice(index, 1);
      this.mostrarMensagem('🗑️ Magia removida.', 'info');
    }
  }
  abrirModal(magia?: any, event?: Event): void {
    console.log('🔍 [abrirModal] CHAMADO');
    event?.stopPropagation();

    if (magia) {
      this.novaMagia = structuredClone(magia);
      this.magiaOriginal = magia;

      // se o nível da magia atual for inválido, ajusta para o máximo permitido
      if (!this.podeAprenderEssaMagia(this.novaMagia)) {
        const max = this.getNivelMaximoMagia();
        this.novaMagia.level = String(max);
      }
    } else {
      this.novaMagia = {
        name: '', level: '0', casting_time: '', range: '',
        description: '', duration: '', school: 'EVOCACAO',
        ritual: false, concentration: false, type: '',
        classes: [], tags: [],
        components: { verbal: false, somatic: false, material: false, raw: '' }
      };
      this.magiaOriginal = null;
    }
    this.modalAberto = true;
    console.log('🔍 [abrirModal] modalAberto =', this.modalAberto);
    this.cdr.detectChanges();
    console.log('🔍 [abrirModal] detectChanges chamado');
    this.cdr.detectChanges();   // ← obrigatório no OnPush

  }
  
  adicionarMagia(): void {
    if (!this.personagem.classePersonagem?.conjurador) {
      this.mostrarMensagem('⚠️ A classe selecionada não é conjuradora!', 'error');
      return;
    }
    if (!this.novaMagia.name?.trim()) {
      this.mostrarMensagem('⚠️ O nome da magia é obrigatório!', 'error');
      return;
    }
    if (!this.novaMagia.level?.toString().trim()) {
      this.mostrarMensagem('⚠️ O nível da magia é obrigatório!', 'error');
      return;
    }
    if (!this.novaMagia.casting_time?.trim()) {
      this.mostrarMensagem('⚠️ O tempo de conjuração é obrigatório!', 'error');
      return;
    }
    if (!this.novaMagia.range?.trim()) {
      this.mostrarMensagem('⚠️ O alcance é obrigatório!', 'error');
      return;
    }
    if (!this.novaMagia.duration?.trim()) {
      this.mostrarMensagem('⚠️ A duração é obrigatória!', 'error');
      return;
    }
    if (!this.novaMagia.school?.trim()) {
      this.mostrarMensagem('⚠️ A escola da magia é obrigatória!', 'error');
      return;
    }
    if (!this.novaMagia.description?.trim()) {
      this.mostrarMensagem('⚠️ A descrição da magia é obrigatória!', 'error');
      return;
    }
    if (!this.novaMagia.components?.verbal &&
        !this.novaMagia.components?.somatic &&
        !this.novaMagia.components?.material) {
      this.mostrarMensagem('⚠️ Selecione pelo menos um componente (V, S ou M)!', 'error');
      return;
    }

    const dadosMagia = {
      name: this.novaMagia.name,
      level: this.novaMagia.level,
      casting_time: this.novaMagia.casting_time,
      range: this.novaMagia.range,
      components: {
        verbal: this.novaMagia.components?.verbal || false,
        somatic: this.novaMagia.components?.somatic || false,
        material: this.novaMagia.components?.material || false,
        raw: this.novaMagia.components?.raw || ''
      },
      duration: this.novaMagia.duration,
      school: this.novaMagia.school,
      ritual: this.novaMagia.ritual || false,
      concentration: this.novaMagia.concentration || false,
      description: this.novaMagia.description,
      classes: this.novaMagia.classes || [],
      tags: this.novaMagia.tags || [],
      type: this.novaMagia.type || ''
    };

    if (!this.personagem.magias) this.personagem.magias = [];
    this.personagem.magias.push(dadosMagia);

    this.mostrarMensagem(`✅ Magia "${dadosMagia.name}" adicionada!`, 'success');

    this.novaMagia = {
      name: '',
      level: '0',
      casting_time: '',
      range: '',
      components: { verbal: false, somatic: false, material: false, raw: '' },
      duration: '',
      school: '',
      ritual: false,
      concentration: false,
      description: '',
      classes: [],
      tags: [],
      type: ''
    };
    this.visivel['magia'] = false;
  }

  private magiaVazia(): Magia {
    return {
      name: '', level: '0', casting_time: '', range: '',
      description: '', duration: '', school: 'EVOCACAO',
      ritual: false, concentration: false, type: '',
      classes: [], tags: [],
      components: { verbal: false, somatic: false, material: false, raw: '' }
    };
  }

  fecharModal(): void {
    this.modalAberto = false;
    this.novaMagia = this.magiaVazia();   // ← Magia, não null
    this.magiaOriginal = null;
    this.cdr.detectChanges();
  }
  podeAprenderEssaMagia(magia: Partial<Magia>): boolean {
    const tipo = this.normalizarTipoConjuracao(this.personagem?.classePersonagem?.tipoConjuracao);
    if (tipo === 'NENHUM') return false;

    const nivelMagia = Number(magia.level ?? 0);
    const nivelPersonagem = this.personagem?.nivelPersonagem ?? 1;

    switch (tipo) {
      case 'COMPLETO':  return nivelMagia <= Math.ceil(nivelPersonagem / 2);
      case 'MEIO':      return nivelMagia <= Math.ceil(nivelPersonagem / 4);
      case 'TERCIARIO': return nivelMagia <= Math.ceil(nivelPersonagem / 6);
      default:          return false;
    }
  }

private getNivelMaximoMagia(): number {
  const tipo = this.normalizarTipoConjuracao(this.personagem?.classePersonagem?.tipoConjuracao);
  const nivelPersonagem = this.personagem?.nivelPersonagem ?? 1;

  switch (tipo) {
    case 'COMPLETO':  return Math.ceil(nivelPersonagem / 2);
    case 'MEIO':      return Math.ceil(nivelPersonagem / 4);
    case 'TERCIARIO': return Math.ceil(nivelPersonagem / 6);
    default:          return 0;
  }
}
podeAprenderNivel(nivel: string | number): boolean {
  return this.podeAprenderEssaMagia({ level: String(nivel) } as Partial<Magia>);
}

  // ============================================
  // SALVAR PERSONAGEM
  // ============================================
  salvarPersonagem(): void {
    // 0) guarda de reentrada — evita clique duplo criar 2 personagens
    if (this.salvando) return;

    // 1) limpa erros anteriores
    this.alertaAberto = false;
    this.alertaTexto = '';

    // 2) validações (cada uma abre o modal com a mensagem)
    if (!this.personagem.nomePersonagem?.trim()) {
      this.mostrarMensagem('⚠️ O nome do personagem é obrigatório!', 'error');
      return;
    }

    const classeId = this.personagem.classePersonagem?.id;
    if (!classeId) {
      this.mostrarMensagem('⚠️ Selecione uma classe para o personagem!', 'error');
      return;
    }

    if (this.personagem.nivelPersonagem < 1 || this.personagem.nivelPersonagem > 20) {
      this.mostrarMensagem('⚠️ O nível deve ser entre 1 e 20!', 'error');
      return;
    }

    // validação dos atributos (1..30)
    const atributos = [
      { nome: 'Força', valor: this.personagem.valorForca },
      { nome: 'Destreza', valor: this.personagem.valorDestreza },
      { nome: 'Constituição', valor: this.personagem.valorConstituicao },
      { nome: 'Inteligência', valor: this.personagem.valorInteligencia },
      { nome: 'Sabedoria', valor: this.personagem.valorSabedoria },
      { nome: 'Carisma', valor: this.personagem.valorCarisma }
    ];

    for (const attr of atributos) {
      if (attr.valor === undefined || attr.valor === null || attr.valor < 1 || attr.valor > 30) {
        this.mostrarMensagem(`⚠️ ${attr.nome} deve ser entre 1 e 30!`, 'error');
        return;
      }
    }

    // 3) remove magias inválidas (sem abrir modal, só corrige)
    const magiasValidas = (this.personagem.magias ?? [])
      .filter((m: Partial<Magia>) => this.podeAprenderEssaMagia(m));

    const removidas = (this.personagem.magias ?? []).length - magiasValidas.length;
    if (removidas > 0) {
      this.personagem.magias = magiasValidas;
      console.warn(`⚠️ ${removidas} magia(s) inválida(s) removida(s).`);
    }

    // 4) se o personagem não é conjurador, remove as magias
    if (!this.personagem.classePersonagem?.conjurador && (this.personagem.magias || []).length > 0) {
      this.personagem.magias = [];
      console.warn('⚠️ Personagem não conjurador — magias removidas.');
    }

    // 5) monta o DTO
    this.calcularValoresAutomaticos();

    const pericias = Array.from(this.slugsSelecionados).map(slug => ({
      slug,
      isProficiente: true
    }));

    const dadosParaEnviar = {
      fotoBase64: this.imagemBase64 || null,
      nomePersonagem: this.personagem.nomePersonagem,
      nivelPersonagem: this.personagem.nivelPersonagem || 1,
      classeId: classeId,
      valorForca: this.personagem.valorForca || 10,
      valorDestreza: this.personagem.valorDestreza || 10,
      valorConstituicao: this.personagem.valorConstituicao || 10,
      valorInteligencia: this.personagem.valorInteligencia || 10,
      valorSabedoria: this.personagem.valorSabedoria || 10,
      valorCarisma: this.personagem.valorCarisma || 10,
      racaPersonagem: this.personagem.racaPersonagem || '',
      ca: this.personagem.caPersonagem || 10,
      iniciativa: this.personagem.iniciativaPersonagem || 0,
      movimento: this.personagem.movimentoPersonagem || 9,
      pontosVida: this.personagem.pontosVidaPersonagem || 10,
      historiaPersonagem: this.personagem.historiaPersonagem || '',
      aparenciaPersonagem: this.personagem.aparenciaPersonagem || '',
      escalaPersonagem: this.personagem.escalaPersonagem || '',
      idadePersonagem: this.personagem.idadePersonagem || 0,
      antecedentePersonagem: this.personagem.antecedentePersonagem || '',
      alinhamentoPersonagem: this.personagem.alinhamentoPersonagem || '',
      ideaisPersonagem: this.personagem.ideaisPersonagem || '',
      defeitosPersonagem: this.personagem.defeitosPersonagem || '',
      anotacoesPersonagem: this.personagem.anotacoesPersonagem || '',
      personalidadePersonagem: this.personagem.personalidadePersonagem || '',
      alturaPersonagem: this.personagem.alturaPersonagem || 0,
      generoPersonagem: this.personagem.generoPersonagem || '',
      pesoPersonagem: this.personagem.pesoPersonagem || 0.0,
      habilidades: this.personagem.habilidades || [],
      proficiencias: this.personagem.proficienciasPersonagem || [],
      inventario: this.personagem.inventarioPersonagem || [],
      magias: this.personagem.magias || [],
      pericias: pericias
    };

    // 6) decide criar vs atualizar com base no id já existente
    const id = this.personagem.idPersonagem;
    const editando = id !== undefined && id !== null;

    console.log(`🔍 [salvarPersonagem] modo=${editando ? 'ATUALIZAR' : 'CRIAR'} id=${id}`);

    this.salvando = true;
    this.cdr.markForCheck();

    const operacao$ = editando
      ? this.personagemService.atualizarPersonagem(id!, dadosParaEnviar)
      : this.personagemService.criarPersonagem(dadosParaEnviar);

    operacao$
      .pipe(
        finalize(() => {
          this.salvando = false;
          this.cdr.markForCheck();
        })
      )
      .subscribe({
        next: (response) => {
          // ✅ ESSENCIAL: grava o id retornado para que próximos saves atualizem
          if (!editando) {
            const novoId = response?.idPersonagem ?? response?.id;
            if (novoId !== undefined && novoId !== null) {
              this.personagem.idPersonagem = novoId;
            } else {
              console.warn('⚠️ Backend não retornou id — próximos saves vão criar de novo!', response);
            }
          }

          this.mostrarMensagem(
            editando
              ? `✅ Personagem atualizado com sucesso!`
              : `✅ Personagem "${response.nomePersonagem}" salvo com sucesso! ID: ${this.personagem.idPersonagem}`,
            'success'
          );
        },
        error: (error) => {
          console.error(`❌ Erro ao ${editando ? 'atualizar' : 'salvar'}:`, error);
          const msgBackend = error?.error?.message || `Erro ao ${editando ? 'atualizar' : 'salvar'} personagem.`;
          this.mostrarMensagem(`❌ ${msgBackend}`, 'error');
        }
      });
  }
  // ============================================
  // CÁLCULOS DE CONJURAÇÃO
  // ============================================
  getCdMagia(): number {
    if (this.personagem.classePersonagem?.conjurador) {
      const atributoConjuracao = this.personagem.classePersonagem.atributoConjuracao;
      if (atributoConjuracao) {
        return this.getBonusAtributo(atributoConjuracao) + this.bonusProficiencia + 8;
      }
    }
    return 0;
  }

  getAtaqueMagico(): number {
    if (this.personagem.classePersonagem?.conjurador) {
      const atributoConjuracao = this.personagem.classePersonagem?.atributoConjuracao;
      if (atributoConjuracao) {
        return this.getBonusAtributo(atributoConjuracao) + this.bonusProficiencia;
      }
    }
    return 0;
  }

  getCaPersonagem(): number {
    return this.bonusDestreza + 10;
  }
    private normalizarTipoConjuracao(tipo: any): 'COMPLETO' | 'MEIO' | 'TERCIARIO' | 'NENHUM' {
    if (tipo === null || tipo === undefined) return 'NENHUM';

    // se vier como número (ordinal do enum)
    if (typeof tipo === 'number') {
      return ['NENHUM', 'COMPLETO', 'MEIO', 'TERCIARIO'][tipo] as any ?? 'NENHUM';
    }

    // se vier como string
    const s = String(tipo).toUpperCase();
    if (s === 'COMPLETO' || s === 'MEIO' || s === 'TERCIARIO' || s === 'NENHUM') {
      return s as any;
    }

    return 'NENHUM';
  }
}