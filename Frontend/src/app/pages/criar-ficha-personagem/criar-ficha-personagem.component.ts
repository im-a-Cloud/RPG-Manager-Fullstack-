import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';

import { PersonagemService } from '../../core/services/personagem.service';
import { RpgService } from '../../core/services/rpg.service';
import { ClasseService } from '../../core/services/classe.service';
import { HabilidadeService} from '../../core/services/habilidade.service';
import { ProficienciaService} from '../../core/services/proficiencia.service'; 
import { MagiaService } from '../../core/services/magia.service'; 
import { ItemService } from '../../core/services/item.service'; 

import { Personagem } from '../../models/personagem';
import { Habilidade } from '../../models/habilidade';
import { Proficiencia } from '../../models/proficiencia';
import { Magia } from '../../models/magia';
import { Item } from '../../models/item';

import { TipoItem } from '../../models/enums/tipoItem.enum';
import { RaridadeItem } from '../../models/enums/raridadeItem.enum';
import { TipoProficiencia } from '../../models/enums/tipoProficiencia.enum';
import { OrigemHabilidade } from '../../models/enums/origemHabilidade.enum';

import { Classe } from '../../models/classe';

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
    proficienciasPersonagem: [],
    inventarioPersonagem: [],
    magias: []
  };

  // ============================================
  // Foto do personagem
  // ============================================
  imagemPreview: string | null = null;
  nomeArquivo = '';
  imagemBase64: string | null = null;

  // ============================================
  // deixar partes da UI visiveis/ocultas
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
    name: '',                       // ← name (antigo nomeMagia)
    level: '0',                     // ← level (antigo nivelMagia)
    casting_time: '',               // ← casting_time (antigo tempoConjuracao)
    range: '',                      // ← range (antigo alcance)
    components: {
        verbal: false,
        somatic: false,
        material: false,
        raw: ''    // ← material_description
    },
    duration: '',                   // ← duration (antigo duracao)
    school: '',                     // ← school (antigo escola)
    ritual: false,
    concentration: false,           // ← concentration (antigo concentracao)
    description: '',                // ← description (antigo descricao)
    classes: [],
    tags: [],
    type: ''
  };

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
  constructor(private cdr: ChangeDetectorRef) {
    const todasPericias = Object.values(this.periciasPorAtributo).flat();
    this.pericias = Object.fromEntries(todasPericias.map(p => [p, false]));
    this.valoresPericias = Object.fromEntries(todasPericias.map(p => [p, 0]));
    
    // Calcula valores iniciais
    this.calcularValoresAutomaticos();
  }

  ngOnInit(): void {
     this.carregarClasses();
  }

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

  carregarClasses(): void {
    this.classeService.listarClasses().subscribe({
      next: (classes: any[]) => {
        console.log('🔍 CLASSES RECEBIDAS:', classes);
        
        // 🔥 MOSTRA O ID DE CADA CLASSE
        classes.forEach(classe => {
          console.log(`   Classe: ${classe.nomeClasse}, ID: ${classe.id}, idClasse: ${classe.idClasse}`);
        });
        
        this.classesDisponiveis = classes;    },
      error: (erro) => {
        console.error('❌ Erro ao carregar classes:', erro);
      }
    });
  }

    // ============================================
    // COMPARAR CLASSES (SÓ ISSO!)
    // ============================================
    compararClasses(classe1: Classe | null, classe2: Classe | null): boolean {
      if (!classe1 || !classe2) return false;
      return classe1.id === classe2.id;
    }

    // ============================================
    // QUANDO UMA CLASSE É SELECIONADA
    // ============================================
    onClasseSelecionada(classe: Classe | null): void {
      console.log('📌 Classe selecionada:', classe);
      if (classe) {
        this.personagem.classePersonagem = {
            ...classe,
            id: classe.id || classe.id || 0        
          };
        console.log('✅ Classe atribuída com ID:', this.personagem.classePersonagem.id);
        this.calcularValoresAutomaticos();
      }
    }

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
      const nivel = magia.level || '0';
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
  // IMAGEM DO PERSONAGEM
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

  removerHabilidade(index: number): void {
    if (this.personagem.habilidades) {
      this.personagem.habilidades.splice(index, 1);
      this.mostrarMensagem('🗑️ Habilidade removida.', 'info');
    }
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

  adicionarMagia(): void {
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

      // ============================================
      // 2️⃣ MONTA O DTO (com os nomes corretos)
      // ============================================
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
      if (!this.personagem.magias) {
          this.personagem.magias = [];
      }
    console.log('📋 Lista de magias atualizada:', this.personagem.magias);
    console.log('📌 Total de magias:', this.personagem.magias.length);

    this.mostrarMensagem(`✅ Magia "${dadosMagia.name}" adicionada à lista!`, 'success');
      this.personagem.magias.push(dadosMagia);
      console.log('🔹 enviando magia:', dadosMagia);

  }
  salvarPersonagem(): void {
      console.log('========================================');
      console.log('📤 DADOS DO PERSONAGEM ANTES DE SALVAR:');
      console.log('   Nome:', this.personagem.nomePersonagem);
      console.log('   Classe ID:', this.personagem.classePersonagem?.id);
      console.log('   Magias:', this.personagem.magias?.length || 0);
      console.log('========================================');

      // ============================================
      // 1️⃣ VALIDAÇÕES
      // ============================================
      if (!this.personagem.nomePersonagem?.trim()) {
          this.mostrarMensagem('⚠️ O nome do personagem é obrigatório!', 'error');
          return;
      }

      // 🔥 VALIDA A CLASSE
    const classeId = this.personagem.classePersonagem?.id;
    
    if (!classeId) {
        this.mostrarMensagem('⚠️ Selecione uma classe para o personagem!', 'error');
        console.error('❌ Classe ID é undefined:', this.personagem.classePersonagem);
        return;
    }

      if (this.personagem.nivelPersonagem < 1 || this.personagem.nivelPersonagem > 20) {
          this.mostrarMensagem('⚠️ O nível deve ser entre 1 e 20!', 'error');
          return;
      }

      // Recalcula valores
      this.calcularValoresAutomaticos();

      // ============================================
      // 2️⃣ MONTA O DTO (USANDO O classeId VALIDADO!)
      // ============================================
      const dadosParaEnviar = {
          nomePersonagem: this.personagem.nomePersonagem,
          nivelPersonagem: this.personagem.nivelPersonagem || 1,
          classeId: classeId,  // ← USA O ID QUE VOCÊ VALIDOU!
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

          // 🔥 LISTAS (TUDO JUNTO!)
          habilidades: this.personagem.habilidades || [],
          proficiencias: this.personagem.proficienciasPersonagem || [],
          inventario: this.personagem.inventarioPersonagem || [],
          magias: this.personagem.magias || [],
          pericias: this.personagem.periciasPersonagem || []
      };

      console.log('📤 Enviando DTO com listas:', dadosParaEnviar);
      console.log('📤 Classe ID:', dadosParaEnviar.classeId);  // ← DEVE SER 2!
      console.log('📤 Habilidades:', dadosParaEnviar.habilidades);
      console.log('📤 Itens:', dadosParaEnviar.inventario);
      console.log('📤 Magias:', dadosParaEnviar.magias);
      console.log('📤 Proficiências:', dadosParaEnviar.proficiencias);
      // ============================================
      // 3️⃣ CHAMA O SERVICE
      // ============================================
      this.personagemService.criarPersonagem(dadosParaEnviar).subscribe({
          next: (response) => {
              this.mostrarMensagem(
                  `✅ Personagem "${response.nomePersonagem}" salvo com sucesso! ID: ${response.id}`,
                  'success'
              );
              console.log('✅ Personagem criado:', response);
          },
          error: (error) => {
              console.error('❌ Erro ao salvar:', error);
              if (error.error) {
                  console.error('❌ Detalhes:', error.error);
              }
              this.mostrarMensagem('❌ Erro ao salvar personagem.', 'error');
          }
      });
  }
  adicionarHabilidade(): void {
      // ============================================
      // 1️⃣ VALIDAÇÕES
      // ============================================
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
          this.mostrarMensagem('⚠️ O número de usos da habilidade deve ser 0 ou mais!', 'error');
          return;
      }
      if (!this.novaHabilidade.recargaHabilidade?.trim()) {
          this.mostrarMensagem('⚠️ A recarga da habilidade é obrigatória!', 'error');
          return;
      }

      // ============================================
      // 2️⃣ MONTA O DTO
      // ============================================
      const dadosHabilidade = {
          nomeHabilidade: this.novaHabilidade.nomeHabilidade,
          origemHabilidade: this.novaHabilidade.origemHabilidade,
          descricaoHabilidade: this.novaHabilidade.descricaoHabilidade,
          usosHabilidade: this.novaHabilidade.usosHabilidade,
          recargaHabilidade: this.novaHabilidade.recargaHabilidade
      };

      // ============================================
      // 3️⃣ ADICIONA NA LISTA (NÃO SALVA NO BACKEND!)
      // ============================================
      if (!this.personagem.habilidades) {
          this.personagem.habilidades = [];
      }

      // 🔥 ADICIONA A HABILIDADE NA LISTA!
      this.personagem.habilidades.push(dadosHabilidade);

      console.log('📋 Lista de habilidades atualizada:', this.personagem.habilidades);
      console.log('📌 Total de habilidades:', this.personagem.habilidades.length);

      this.mostrarMensagem(`✅ Habilidade "${dadosHabilidade.nomeHabilidade}" adicionada!`, 'success');

      // ============================================
      // 4️⃣ LIMPA O FORMULÁRIO
      // ============================================
      this.novaHabilidade = {
          nomeHabilidade: '',
          origemHabilidade: OrigemHabilidade.OUTROS,
          descricaoHabilidade: '',
          usosHabilidade: 0,
          recargaHabilidade: ''
      };
      this.visivel['addHabilidade'] = false;
  }
  salvarItem(): void {
      console.log('🔥 salvarItem() CHAMADO!');

      // ============================================
      // 1️⃣ VALIDAÇÕES
      // ============================================
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

      // 🔥 CORRIGIDO: PREÇO
      if (this.novoItem.precoItem === undefined || this.novoItem.precoItem === null || this.novoItem.precoItem < 0) {
          this.mostrarMensagem('⚠️ O preço não pode ser negativo!', 'error');
          return;
      }

      // 🔥 CORRIGIDO: PESO (permite 0)
      if (this.novoItem.pesoItem === undefined || this.novoItem.pesoItem === null || this.novoItem.pesoItem < 0) {
          this.mostrarMensagem('⚠️ O peso não pode ser negativo!', 'error');
          return;
      }

      // ============================================
      // 2️⃣ MONTA O DTO
      // ============================================
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

      console.log('🔹 Enviando item:', dadosItem);
      console.log('📤 Dados enviados:', JSON.stringify(dadosItem, null, 2));

      // ============================================
      // 3️⃣ CHAMA O SERVICE
      // ============================================
      this.itemService.criarItem(dadosItem).subscribe({
          next: (response) => {
              console.log('✅ Item criado:', response);

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

              console.log('📌 Adicionando na lista:', novoItem);
              this.personagem.inventarioPersonagem.push(novoItem);
              this.cdr.detectChanges();

              this.mostrarMensagem(`✅ Item "${novoItem.nomeItem}" adicionado ao inventário!`, 'success');

              // ============================================
              // 4️⃣ LIMPA O FORMULÁRIO
              // ============================================
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
              if (error.error) {
                  console.error('❌ Detalhes:', error.error);
              }
              this.mostrarMensagem('❌ Erro ao salvar item. Tente novamente.', 'error');
          }
      });
  }
  salvarProficiencia(): void{
    if(!this.novaProficiencia.tipoProficiencia?.trim()){
      this.mostrarMensagem('⚠️ O tipo de proficiência é obrigatório!', 'error');
      return;
    }
    if(!this.novaProficiencia.listaProficiencias?.trim()){
      this.mostrarMensagem('⚠️ A descrição da proficiência é obrigatória!', 'error');
      return;
    }

    const dadosProficiencia = {
        tipoProficiencia: this.novaProficiencia.tipoProficiencia,
        listaProficiencias: this.novaProficiencia.listaProficiencias
    };
      console.log('🔹 enviando proficiência:', dadosProficiencia);

      this.proficienciaService.criarProficiencia(dadosProficiencia).subscribe({
      next: (response) => {

      // 🔥 ADICIONA NA LISTA COM FALLBACK
      if (!this.personagem.proficienciasPersonagem) {
          this.personagem.proficienciasPersonagem = [];
      }

    const novaProficiencia = {
        tipoProficiencia: response.tipoProficiencia || dadosProficiencia.tipoProficiencia,
        listaProficiencias: response.descricaoProficiencia || dadosProficiencia.listaProficiencias,
    };

    console.log('📌 Adicionando na lista:', novaProficiencia);
    this.personagem.proficienciasPersonagem?.push(novaProficiencia);
    this.cdr.detectChanges();
    console.log('📋 Lista atualizada:', this.personagem.proficienciasPersonagem);
    

    this.mostrarMensagem(`✅ Proficiencia "${response.tipoProfPersonagem || dadosProficiencia.tipoProficiencia}" salva!`, 'success');

      },
      error: (error) => {
          console.error('❌ Erro ao salvar:', error);
          this.mostrarMensagem('❌ Erro ao salvar proficiencia.', 'error');
      }
  });
  }

  getCaPersonagem(): number {
    return this.bonusDestreza + 10; // CA base + bônus de Destreza
  }
}