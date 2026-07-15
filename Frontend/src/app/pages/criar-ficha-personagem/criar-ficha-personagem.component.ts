import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { PersonagemService } from '../../core/services/personagem.service';
import { RpgService } from '../../core/services/rpg.service';
import { Personagem } from '../../models/personagem';
import { Habilidade } from '../../models/habilidade';
import { Proficiencia } from '../../models/proficiencia';
import { Magia } from '../../models/magia';
import { ComponentesMagia } from '../../models/componentesMagia';
import { Item } from '../../models/item';
import { TipoItem } from '../../models/enums/tipoItem.enum';
import { EscolaMagia } from '../../models/enums/escolaMagia.enum';
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

  // ============================================
  // PERSONAGEM - Usando o modelo correto
  // ============================================
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
    habilidadesPersonagem: [],
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
  // UI STATE
  // ============================================
  visivel: { [key: string]: boolean } = {
    addProficiencia: false,
    addMagia: false,
    addItem: false,
    addHabilidade: false
  };

  isLoading = false;
  mensagem = '';
  tipoMensagem: 'success' | 'error' | 'info' = 'info';

  // ============================================
  // FORMULÁRIOS AUXILIARES
  // ============================================
  novaHabilidade: Partial<Habilidade> = {
    nomeHabilidade: '',
    origemHabilidade: OrigemHabilidade.OUTROS,
    descricaoHabilidade: ''
  };

  novaProficiencia: Partial<Proficiencia> = {
    tipoProfPersonagem: TipoProficiencia.OUTRO,
    descricaoProficiencia: ''
  };

  itemSelecionado: Item | null = null;

  novoItem: Partial<Item> = {
    nomeItem: '',
    raridadeItem: '',
    quantidadeItem: 0,
    pesoItem: 0,
    tipoItem: TipoItem.ARMA,
    descricaoItem: ''
  };

  magiaSelecionada: any = null;

  novaMagia: Partial<Magia> = {
    nomeMagia: '',
    nivelMagia: '0',
    tempoConjuracao: '',
    alcance: '',
    componentes: 
    { verbal: false, 
      somatico: false, 
      material: false, 
      descricaoMaterial: '' 
    },
    ritual: false,
    concentracao: false,
    duracao: '',
    escola: undefined,
    descricao: ''
  };

  // ============================================
  // PERÍCIAS (mantido do seu código)
  // ============================================
  listaPericias = [
    'acrobacia', 'adestrarAnimais', 'arcanismo', 'atletismo',
    'enganacao', 'historia', 'intuicao', 'intimidacao',
    'investigacao', 'medicina', 'natureza', 'percepcao',
    'atuacao', 'persuacao', 'religiao', 'prestidigitacao',
    'furtividade', 'sobrevivencia'
  ];

  periciasPorAtributo = {
    forca: ['atletismo'],
    destreza: ['acrobacia', 'prestidigitacao', 'furtividade'],
    inteligencia: ['arcanismo', 'historia', 'investigacao', 'natureza', 'religiao'],
    sabedoria: ['adestrarAnimais', 'intuicao', 'medicina', 'percepcao', 'sobrevivencia'],
    carisma: ['enganacao', 'intimidacao', 'atuacao', 'persuacao']
  };

  pericias: { [key: string]: boolean } = {};
  valoresPericias: { [key: string]: number } = {};

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
  // CONSTRUCTOR
  // ============================================
  constructor() {
    const todasPericias = Object.values(this.periciasPorAtributo).flat();
    this.pericias = Object.fromEntries(todasPericias.map(p => [p, false]));
    this.valoresPericias = Object.fromEntries(todasPericias.map(p => [p, 0]));
    
    // Calcula valores iniciais
    this.calcularValoresAutomaticos();
  }

  ngOnInit(): void {}

  // ============================================
  // CÁLCULOS AUTOMÁTICOS
  // ============================================
  calcularValoresAutomaticos(): void {
    const destrezaBonus = this.rpgService.calcularBonusAtributo(this.personagem.valorDestreza);
    
    // CA = 10 + bônus de Destreza
    this.personagem.caPersonagem = 10 + destrezaBonus;
    
    // Iniciativa = bônus de Destreza
    this.personagem.iniciativaPersonagem = destrezaBonus;
    
    // Movimento padrão 9m
    this.personagem.movimentoPersonagem = 9;
    
    // PV = Nível * 6 + bônus de Constituição (simplificado)
    const constBonus = this.rpgService.calcularBonusAtributo(this.personagem.valorConstituicao);
    this.personagem.pontosVidaPersonagem = (this.personagem.nivelPersonagem || 1) * 6 + constBonus;
  }

  // ============================================
  // GETTERS - BÔNUS DE ATRIBUTOS
  // ============================================
  get bonusForca() {
    return this.rpgService.calcularBonusAtributo(this.personagem.valorForca);
  }

  get bonusDestreza() {
    return this.rpgService.calcularBonusAtributo(this.personagem.valorDestreza);
  }

  get bonusConstituicao() {
    return this.rpgService.calcularBonusAtributo(this.personagem.valorConstituicao);
  }

  get bonusInteligencia() {
    return this.rpgService.calcularBonusAtributo(this.personagem.valorInteligencia);
  }

  get bonusSabedoria() {
    return this.rpgService.calcularBonusAtributo(this.personagem.valorSabedoria);
  }

  get bonusCarisma() {
    return this.rpgService.calcularBonusAtributo(this.personagem.valorCarisma);
  }

  get bonusProficiencia() {
    return this.rpgService.calcularBonusProficiencia(this.personagem.nivelPersonagem || 1);
  }

  getBonusAtributo(atributo: string) {
    const atributos = {
      forca: this.personagem.valorForca,
      destreza: this.personagem.valorDestreza,
      constituicao: this.personagem.valorConstituicao,
      inteligencia: this.personagem.valorInteligencia,
      sabedoria: this.personagem.valorSabedoria,
      carisma: this.personagem.valorCarisma
    };
    return this.rpgService.getBonusAtributo(atributo, atributos);
  }

  getValorPericia(nome: string) {
    const atributos = {
      forca: this.personagem.valorForca,
      destreza: this.personagem.valorDestreza,
      constituicao: this.personagem.valorConstituicao,
      inteligencia: this.personagem.valorInteligencia,
      sabedoria: this.personagem.valorSabedoria,
      carisma: this.personagem.valorCarisma
    };
    return this.rpgService.getValorPericia(
      nome,
      this.pericias,
      this.personagem.nivelPersonagem || 1,
      atributos,
      this.periciasPorAtributo
    );
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
      const nivel = magia.nivelMagia || '0';
      if (!grupos[nivel]) {
        grupos[nivel] = [];
      }
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

  mostrarSecao(secao: string): void {
    this.visivel[secao] = true;
  }

  esconderSecao(secao: string): void {
    this.visivel[secao] = false;
  }

  voltar(): void {
    window.history.back();
  }

  // ============================================
  // MENSAGENS
  // ============================================
  mostrarMensagem(texto: string, tipo: 'success' | 'error' | 'info' = 'info'): void {
    this.mensagem = texto;
    this.tipoMensagem = tipo;
    setTimeout(() => {
      this.mensagem = '';
    }, 5000);
  }

  // ============================================
  // HABILIDADES
  // ============================================
  adicionarHabilidade(): void {
    if (this.novaHabilidade.nomeHabilidade?.trim() && this.novaHabilidade.origemHabilidade?.trim()) {
      const habilidade: Habilidade = {
        nomeHabilidade: this.novaHabilidade.nomeHabilidade || '',
        origemHabilidade: this.novaHabilidade.origemHabilidade || '',
        descricaoHabilidade: this.novaHabilidade.descricaoHabilidade || 'Sem descrição'
      };
      
      if (!this.personagem.habilidadesPersonagem) {
        this.personagem.habilidadesPersonagem = [];
      }
      
      this.personagem.habilidadesPersonagem.push(habilidade);
      this.novaHabilidade = {
        nomeHabilidade: '',
        origemHabilidade: undefined,
        descricaoHabilidade: ''
      };
      this.mostrarMensagem('✅ Habilidade adicionada!', 'success');
    } else {
      this.mostrarMensagem('⚠️ Preencha Nome e Origem da habilidade.', 'error');
    }
  }

  removerHabilidade(index: number): void {
    if (this.personagem.habilidadesPersonagem) {
      this.personagem.habilidadesPersonagem.splice(index, 1);
      this.mostrarMensagem('🗑️ Habilidade removida.', 'info');
    }
  }

  // ============================================
  // PROFICIÊNCIAS
  // ============================================
  adicionarProficiencia(): void {
  if (!this.novaProficiencia.tipoProfPersonagem) {
    this.mostrarMensagem('⚠️ Selecione o tipo de proficiência.', 'error');
    return;
  }

  // Verifica se já existe esse tipo
  const jaExiste = this.personagem.proficienciasPersonagem?.some(
    p => p.tipoProfPersonagem === this.novaProficiencia.tipoProfPersonagem
  );

  if (jaExiste) {
    this.mostrarMensagem('⚠️ Este tipo de proficiência já foi adicionado.', 'error');
    return;
  }

  const proficiencia: Proficiencia = {
    tipoProfPersonagem: this.novaProficiencia.tipoProfPersonagem as TipoProficiencia,
    descricaoProficiencia: this.novaProficiencia.descricaoProficiencia || ''
  };

  if (!this.personagem.proficienciasPersonagem) {
    this.personagem.proficienciasPersonagem = [];
  }

  this.personagem.proficienciasPersonagem.push(proficiencia);

  this.mostrarMensagem('✅ Tipo de proficiência adicionado!', 'success');
  }

  removerProficiencia(index: number): void {
    if (this.personagem.proficienciasPersonagem) {
      const removido = this.personagem.proficienciasPersonagem[index].tipoProfPersonagem;
      this.personagem.proficienciasPersonagem.splice(index, 1);
      this.mostrarMensagem(`🗑️ "${removido}" removido.`, 'info');
    }
  }

  // ============================================
  // ITENS
  // ============================================
  adicionarItem(): void {
    if (!this.novoItem.nomeItem?.trim()) {
      this.mostrarMensagem('⚠️ Informe o nome do item.', 'error');
      return;
    }
    
    const item: Item = {
      nomeItem: this.novoItem.nomeItem || '',
      raridadeItem: this.novoItem.raridadeItem || '',
      quantidadeItem: this.novoItem.quantidadeItem || 0,
      pesoItem: this.novoItem.pesoItem || 0,
      tipoItem: this.novoItem.tipoItem || TipoItem.ARMA,
      precisaSintonizacao: false,
      descricaoItem: this.novoItem.descricaoItem || ''
    };
    
    if (!this.personagem.inventarioPersonagem) {
      this.personagem.inventarioPersonagem = [];
    }
    
    this.personagem.inventarioPersonagem.push(item);
    this.novoItem = {
      nomeItem: '',
      raridadeItem: '',
      quantidadeItem: 0,
      pesoItem: 0,
      tipoItem: TipoItem.ARMA,
      descricaoItem: ''
    };
    this.mostrarMensagem('✅ Item adicionado ao inventário!', 'success');
  }
  
  selecionarItem(item: any): void {
    if (this.itemSelecionado === item) {
      this.itemSelecionado = null;
    } else {
      this.itemSelecionado = item;
    }
  }

  isItemSelecionado(item: any): boolean {
    return this.itemSelecionado === item;
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
  adicionarMagia(): void {
    if (!this.novaMagia.nomeMagia?.trim()) {
      this.mostrarMensagem('⚠️ Informe o nome da magia.', 'error');
      return;
    }

    const componentesMagia: ComponentesMagia = {
      verbal: this.novaMagia.componentes?.verbal || false,
      somatico: this.novaMagia.componentes?.somatico || false,
      material: this.novaMagia.componentes?.material || false,
      descricaoMaterial: this.novaMagia.componentes?.descricaoMaterial || ''
    };

    const magia: Magia = {
      nomeMagia: this.novaMagia.nomeMagia || '',
      nivelMagia: this.novaMagia.nivelMagia || '0',
      tempoConjuracao: this.novaMagia.tempoConjuracao || '',
      alcance: this.novaMagia.alcance || '',
      componentes: componentesMagia,
      ritual: this.novaMagia.ritual || false,
      concentracao: this.novaMagia.concentracao || false,
      duracao: this.novaMagia.duracao || '',
      escola: this.novaMagia.escola || EscolaMagia.ABJURACAO,
      descricao: this.novaMagia.descricao || ''
    };
    
    if (!this.personagem.magias) {
      this.personagem.magias = [];
    }
    
    this.personagem.magias.push(magia);
    this.novaMagia = {
      nomeMagia: '',
      nivelMagia: '0',
      tempoConjuracao: '',
      alcance: '',
      componentes: {
        verbal: false,
        somatico: false,
        material: false,
        descricaoMaterial: ''
      },
      ritual: false,
      concentracao: false,
      duracao: '',
      escola: EscolaMagia.ABJURACAO,
      descricao: ''
    };
    this.mostrarMensagem('✅ Magia adicionada!', 'success');
  }

  selecionarMagia(magia: any): void {

    console.log("Antes:", this.magiaSelecionada);

    if (this.magiaSelecionada === magia) {
      this.magiaSelecionada = null;
    } else {
      this.magiaSelecionada = magia;
    }

    console.log("Depois:", this.magiaSelecionada);

    console.log(JSON.stringify(magia, null, 2));
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

  // ============================================
  // SALVAR PERSONAGEM NO BACKEND
  // ============================================
  salvarPersonagem(): void {
    // Validações básicas
    if (!this.personagem.nomePersonagem?.trim()) {
      this.mostrarMensagem('⚠️ O nome do personagem é obrigatório!', 'error');
      return;
    }

    if (!this.personagem.racaPersonagem?.trim()) {
      this.mostrarMensagem('⚠️ A raça do personagem é obrigatória!', 'error');
      return;
    }

    if (!this.personagem.classePersonagem?.nomeClasse?.trim()) {
      this.mostrarMensagem('⚠️ A classe do personagem é obrigatória!', 'error');
      return;
    }

    if (this.personagem.nivelPersonagem < 1 || this.personagem.nivelPersonagem > 20) {
      this.mostrarMensagem('⚠️ O nível deve ser entre 1 e 20!', 'error');
      return;
    }

    // Recalcula valores antes de salvar
    this.calcularValoresAutomaticos();

    this.isLoading = true;
    this.mostrarMensagem('⏳ Salvando personagem...', 'info');

    // Adiciona imagem se houver
    if (this.imagemBase64) {
      this.personagem.aparenciaPersonagem = this.imagemBase64;
    }

    console.log('📤 Enviando personagem:', this.personagem);

    this.personagemService.criarPersonagem(this.personagem).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.mostrarMensagem(
          `✅ Personagem "${response.nomePersonagem}" salvo com sucesso! ID: ${response.idPersonagem}`,
          'success'
        );
        console.log('✅ Personagem criado:', response);
        
        // Opcional: redirecionar ou limpar formulário
        // this.limparFormulario();
      },
      error: (error) => {
        this.isLoading = false;
        console.error('❌ Erro ao salvar:', error);
        
        let mensagemErro = '❌ Erro ao salvar personagem. Tente novamente.';
        if (error.error?.message) {
          mensagemErro = `❌ ${error.error.message}`;
        } else if (error.message) {
          mensagemErro = `❌ ${error.message}`;
        }
        
        this.mostrarMensagem(mensagemErro, 'error');
      }
    });
  }

  // ============================================
  // LIMPAR FORMULÁRIO
  // ============================================
  limparFormulario(): void {
    if (confirm('Tem certeza que deseja limpar o formulário? Todos os dados não salvos serão perdidos.')) {
      this.personagem = {
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
        habilidadesPersonagem: [],
        proficienciasPersonagem: [],
        inventarioPersonagem: [],
        magias: []
      };
      
      this.imagemPreview = null;
      this.imagemBase64 = null;
      this.nomeArquivo = '';
      
      const todasPericias = Object.values(this.periciasPorAtributo).flat();
      this.pericias = Object.fromEntries(todasPericias.map(p => [p, false]));
      this.valoresPericias = Object.fromEntries(todasPericias.map(p => [p, 0]));
      
      this.resistencias = {
        forca: false,
        destreza: false,
        constituicao: false,
        inteligencia: false,
        sabedoria: false,
        carisma: false
      };
      
      this.calcularValoresAutomaticos();
      this.mostrarMensagem('🧹 Formulário limpo!', 'info');
    }
  }
  getCaPersonagem(): number {
    return this.bonusDestreza + 10; // CA base + bônus de Destreza
  }
}