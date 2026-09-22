import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

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
})
export class CriarFichaPersonagemComponent implements OnInit {

  private rpgService = inject(RpgService);
  private personagemService = inject(PersonagemService);
  private classeService = inject(ClasseService);
  private habilidadeService = inject(HabilidadeService);
  private proficienciaService = inject(ProficienciaService);
  private magiaService = inject(MagiaService);
  private itemService = inject(ItemService);
  private periciaService = inject(PericiaService);   // ← NOVO

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

  // ============================================
  // UI
  // ============================================
  visivel: { [key: string]: boolean } = {
    addProficiencia: false,
    addMagia: false,
    addItem: false,
    addHabilidade: false
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

  novaMagia: Partial<Magia> = {
    name: '',
    level: '0',
    casting_time: '',
    range: '',
    components: {
      verbal: false,
      somatic: false,
      material: false,
      raw: ''
    },
    duration: '',
    school: '',
    ritual: false,
    concentration: false,
    description: '',
    classes: [],
    tags: [],
    type: ''
  };

  // ============================================
  // PERÍCIAS (fonte de verdade = backend)
  // ============================================

  private atributoPorSlug: Record<string, string> = {
  acrobacia: 'DESTREZA',
  adestrarAnimais: 'SABEDORIA',
  arcanismo: 'INTELIGENCIA',
  atletismo: 'FORCA',
  atuacao: 'CARISMA',
  enganacao: 'CARISMA',
  furtividade: 'DESTREZA',
  historia: 'INTELIGENCIA',
  intimidacao: 'CARISMA',
  intuicao: 'SABEDORIA',
  investigacao: 'INTELIGENCIA',
  medicina: 'SABEDORIA',
  natureza: 'INTELIGENCIA',
  percepcao: 'SABEDORIA',
  persuasao: 'CARISMA',
  prestidigitacao: 'DESTREZA',
  religiao: 'INTELIGENCIA',
  sobrevivencia: 'SABEDORIA'
};

  pericias: Pericia[] = [];                     // ← vem do GET /pericias
  slugsSelecionados = new Set<string>();        // ← slugs escolhidos

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
        console.log('🔍 CLASSES RECEBIDAS:', classes);
        classes.forEach(classe => {
          console.log(`   Classe: ${classe.nomeClasse}, ID: ${classe.id}`);
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
        console.log('🔍 PERÍCIAS RECEBIDAS DO BACKEND:', pericias);
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
    console.log('📌 Classe selecionada:', classe);
    if (classe) {
      this.personagem.classePersonagem = {
        ...classe,
        id: classe.id || 0
      };
      console.log('✅ Classe atribuída com ID:', this.personagem.classePersonagem.id);
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

  /**
   * Valor total da perícia para exibição.
   * O backend já devolve `valorTotalPericia` no response; isso aqui é só
   * para renderizar em tempo real conforme o usuário muda os atributos.
   */
    getValorPericia(slug: string): number {
      const atributo = this.atributoPorSlug[slug];
      if (!atributo) return 0;
      const modificador = this.getBonusAtributo(atributo);
      const prof = this.isSelecionada(slug) ? this.bonusProficiencia : 0;
      return modificador + prof;
    }

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
    if (file) {
      this.nomeArquivo = file.name;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagemPreview = e.target.result;
        this.imagemBase64 = e.target.result;
      };
      reader.readAsDataURL(file);
    }
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
    this.mensagem = texto;
    this.tipoMensagem = tipo;
    setTimeout(() => { this.mensagem = ''; }, 5000);
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
    this.visivel['addHabilidade'] = false;
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
          isMagicoItem: response.isMagicoItem || dadosItem.isMagicoItem,
          precisaSintonizacao: response.precisaSintonizacao || dadosItem.precisaSintonizacao,
          quantidadeItem: response.quantidadeItem || dadosItem.quantidadeItem,
          tipoItem: response.tipoItem || dadosItem.tipoItem
        };
        this.personagem.inventarioPersonagem.push(novoItem);
        this.cdr.detectChanges();
        this.mostrarMensagem(`✅ Item "${novoItem.nomeItem}" adicionado!`, 'success');

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

  adicionarMagia(): void {
    if (!this.personagem.classePersonagem?.conjurador) {
      this.mostrarMensagem('⚠️ A classe selecionada não é conjuradora!', 'error');
      return;
    }
    if (!this.novaMagia.name?.trim()) {
      this.mostrarMensagem('⚠️ O nome da magia é obrigatório!', 'error');
      return;
    }
    if (!this.novaMagia.level?.trim()) {
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
  }

  // ============================================
  // SALVAR PERSONAGEM
  // ============================================
  salvarPersonagem(): void {
    console.log('========================================');
    console.log('📤 DADOS DO PERSONAGEM ANTES DE SALVAR:');
    console.log('   Nome:', this.personagem.nomePersonagem);
    console.log('   Classe ID:', this.personagem.classePersonagem?.id);
    console.log('   Perícias selecionadas:', this.slugsSelecionados.size);
    console.log('========================================');

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

    this.calcularValoresAutomaticos();

    // 🔥 MONTA AS PERÍCIAS COM SLUG (o backend busca por slug)
    const pericias = Array.from(this.slugsSelecionados).map(slug => ({
      slug,
      isProficiente: true
    }));

    console.log('📤 PERÍCIAS PARA ENVIAR:', pericias);

    const dadosParaEnviar = {
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

      habilidades: this.personagem.habilidades || [],
      proficiencias: this.personagem.proficienciasPersonagem || [],
      inventario: this.personagem.inventarioPersonagem || [],
      magias: this.personagem.magias || [],
      pericias: pericias                     // ← AGORA COM SLUG
    };

    console.log('📤 DTO COMPLETO:');
    console.log(JSON.stringify(dadosParaEnviar, null, 2));

    this.personagemService.criarPersonagem(dadosParaEnviar).subscribe({
      next: (response) => {
        this.mostrarMensagem(
          `✅ Personagem "${response.nomePersonagem}" salvo com sucesso! ID: ${response.id}`,
          'success'
        );
      },
      error: (error) => {
        console.error('❌ Erro ao salvar:', error);
        this.mostrarMensagem('❌ Erro ao salvar personagem.', 'error');
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
}