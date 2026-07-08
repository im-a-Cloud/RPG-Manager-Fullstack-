import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Personagem } from '../../models/personagem';
import { Pericia } from '../../models/pericia';
import { Proficiencia } from '../../models/proficiencia';
import { Habilidade } from '../../models/habilidade';
import { Magia } from '../../models/magia';
import { Item } from '../../models/item';
import { Classe } from '../../models/classe';

import { RpgService } from '../../core/services/rpg.service';

@Component({
  selector: 'app-criar-ficha-personagem',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './criar-ficha-personagem.html',
  styleUrl: './criar-ficha-personagem.scss',
})
export class CriarFichaPersonagemComponent implements OnInit {

  private rpgService = inject(RpgService);

  id: string | null = null;

  imagemPreview: string | null = null;
  nomeArquivo = '';

  visivel: { [key: string]: boolean } = {
    addProficiencia: false,
    addMagia: false,
    addItem: false,
    addHabilidade: false
  };

  listaPericias = [
    'acrobacia',
    'adestrarAnimais',
    'arcanismo',
    'atletismo',
    'enganacao',
    'historia',
    'intuicao',
    'intimidacao',
    'investigacao',
    'medicina',
    'natureza',
    'percepcao',
    'atuacao',
    'persuacao',
    'religiao',
    'prestidigitacao',
    'furtividade',
    'sobrevivencia'
  ];

  periciasPorAtributo = {
    forca: ['atletismo'],
    destreza: ['acrobacia', 'prestidigitacao', 'furtividade'],
    inteligencia: [
      'arcanismo',
      'historia',
      'investigacao',
      'natureza',
      'religiao'
    ],
    sabedoria: [
      'adestrarAnimais',
      'intuicao',
      'medicina',
      'percepcao',
      'sobrevivencia'
    ],
    carisma: [
      'enganacao',
      'intimidacao',
      'atuacao',
      'persuacao'
    ]
  };

  pericias: { [key: string]: boolean } = {};

  valoresPericias: { [key: string]: number } = {};

  resistencias: { [key: string]: boolean } = {
    forca: false,
    destreza: false,
    constituicao: false,
    inteligencia: false,
    sabedoria: false,
    carisma: false
  };

  forcaPersonagem = 10;
  destrezaPersonagem = 10;
  constituicaoPersonagem = 10;
  inteligenciaPersonagem = 10;
  sabedoriaPersonagem = 10;
  carismaPersonagem = 10;

  nivelPersonagem = 1;

  habilidades: any[] = [];

  novaHabilidade = {
    nome: '',
    origem: '',
    descricao: ''
  };

  proficiencias: any[] = [];

  novaProficiencia = {
    tipoProfPersonagem: '',
    listaProficiencias: ''
  };

  inventario: any[] = [];

  itemSelecionado: any = null;

  novoItem = {
    nomeItem: '',
    raridadeItem: '',
    quantidadeItem: '',
    pesoItem: '',
    tipoItem: '',
    descricaoItem: ''
  };

  magias: any[] = [];

  magiaSelecionada: any = null;

  novaMagia = {
    nomeMagia: '',
    nivelMagia: '0',
    tempoConjuracaoMagia: '',
    areaMagia: '',
    componentesMagia: {
      V: false,
      S: false,
      M: false
    },
    componentesMaterial: '',
    ritualMagia: false,
    concentracaoMagia: false,
    duracaoMagia: '',
    escolaMagia: '',
    descricaoMagia: ''
  };

  constructor() {

    const todasPericias =
      Object.values(this.periciasPorAtributo).flat();

    this.pericias =
      Object.fromEntries(
        todasPericias.map(p => [p, false])
      );

    this.valoresPericias =
      Object.fromEntries(
        todasPericias.map(p => [p, 0])
      );
  }

  ngOnInit(): void {

  }

  get bonusForca() {
    return this.rpgService.calcularBonusAtributo(this.forcaPersonagem);
  }

  get bonusDestreza() {
    return this.rpgService.calcularBonusAtributo(this.destrezaPersonagem);
  }

  get bonusConstituicao() {
    return this.rpgService.calcularBonusAtributo(this.constituicaoPersonagem);
  }

  get bonusInteligencia() {
    return this.rpgService.calcularBonusAtributo(this.inteligenciaPersonagem);
  }

  get bonusSabedoria() {
    return this.rpgService.calcularBonusAtributo(this.sabedoriaPersonagem);
  }

  get bonusCarisma() {
    return this.rpgService.calcularBonusAtributo(this.carismaPersonagem);
  }

  get bonusProficiencia() {
    return this.rpgService.calcularBonusProficiencia(this.nivelPersonagem);
  }

  getBonusAtributo(atributo: string) {
    return this.rpgService.getBonusAtributo(
      atributo,
      {
        forca: this.forcaPersonagem,
        destreza: this.destrezaPersonagem,
        constituicao: this.constituicaoPersonagem,
        inteligencia: this.inteligenciaPersonagem,
        sabedoria: this.sabedoriaPersonagem,
        carisma: this.carismaPersonagem
      }
    );
  }

  getValorPericia(nome: string) {
    return this.rpgService.getValorPericia(
      nome,
      this.pericias,
      this.nivelPersonagem,
      {
        forca: this.forcaPersonagem,
        destreza: this.destrezaPersonagem,
        constituicao: this.constituicaoPersonagem,
        inteligencia: this.inteligenciaPersonagem,
        sabedoria: this.sabedoriaPersonagem,
        carisma: this.carismaPersonagem
      },
      this.periciasPorAtributo
    );
  }

  getTesteResistencia(atributo: string) {
    return this.rpgService.getTesteResistencia(
      atributo,
      this.resistencias,
      this.nivelPersonagem,
      {
        forca: this.forcaPersonagem,
        destreza: this.destrezaPersonagem,
        constituicao: this.constituicaoPersonagem,
        inteligencia: this.inteligenciaPersonagem,
        sabedoria: this.sabedoriaPersonagem,
        carisma: this.carismaPersonagem
      }
    );


  }
    onSelectedFile(event: any): void {

    const file = event.target.files[0];

    if (file) {

      this.nomeArquivo = file.name;

      const reader = new FileReader();

      reader.onload = (e: any) => {
        this.imagemPreview = e.target.result;
      };

      reader.readAsDataURL(file);
    }

  }

  removerImagem(): void {

    this.imagemPreview = null;
    this.nomeArquivo = '';

    const fileInput = document.getElementById(
      'imagemPersonagem'
    ) as HTMLInputElement;

    if (fileInput) {
      fileInput.value = '';
    }

  }

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

    // Depois substituímos pelo Router quando
    // você terminar a navegação da aplicação.

    window.history.back();

  }

  adicionarHabilidade(): void {

    if (
      this.novaHabilidade.nome.trim() &&
      this.novaHabilidade.origem.trim()
    ) {

      this.habilidades.push({

        nome: this.novaHabilidade.nome,
        origem: this.novaHabilidade.origem,
        descricao:
          this.novaHabilidade.descricao || 'Sem descrição'

      });

      this.novaHabilidade = {

        nome: '',
        origem: '',
        descricao: ''

      };

    } else {

      alert('Preencha Nome e Origem da habilidade.');

    }

  }

  adicionarProficiencia(): void {

    if (
      this.novaProficiencia.tipoProfPersonagem.trim() &&
      this.novaProficiencia.listaProficiencias.trim()
    ) {

      this.proficiencias.push({

        tipo: this.novaProficiencia.tipoProfPersonagem,
        lista: this.novaProficiencia.listaProficiencias

      });

      this.novaProficiencia = {

        tipoProfPersonagem: '',
        listaProficiencias: ''

      };

    } else {

      alert(
        'Preencha o Tipo e a Lista de Proficiências.'
      );

    }
  }
  adicionarItem(): void {

    if (!this.novoItem.nomeItem.trim()) {
      alert('Informe o nome do item.');
      return;
    }
    this.inventario.push({
      ...this.novoItem
    });
    this.novoItem = {
      nomeItem: '',
      raridadeItem: '',
      quantidadeItem: '',
      pesoItem: '',
      tipoItem: '',
      descricaoItem: ''
    };
  }
  get pesoTotal(): number {

    return this.inventario.reduce((total, item) => {

      const peso = Number(item.pesoItem) || 0;
      const quantidade = Number(item.quantidadeItem) || 1;

      return total + (peso * quantidade);

    }, 0);

  }

  get quantidadeTotal(): number {

    return this.inventario.reduce((total, item) => {

      return total + (Number(item.quantidadeItem) || 1);

    }, 0);

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

  getItemIndex(item: any): number {

    return this.inventario.indexOf(item);

  }

  removerItem(item: any): void {

    const index = this.inventario.indexOf(item);

    if (index > -1) {

      this.inventario.splice(index, 1);

      if (this.itemSelecionado === item) {
        this.itemSelecionado = null;
      }

    }

  }  adicionarMagia(): void {

    if (!this.novaMagia.nomeMagia.trim()) {
      alert('Informe o nome da magia.');
      return;
    }

    this.magias.push({
      ...this.novaMagia
    });

    this.novaMagia = {
      nomeMagia: '',
      nivelMagia: '0',
      tempoConjuracaoMagia: '',
      areaMagia: '',
      componentesMagia: {
        V: false,
        S: false,
        M: false
      },
      componentesMaterial: '',
      ritualMagia: false,
      concentracaoMagia: false,
      duracaoMagia: '',
      escolaMagia: '',
      descricaoMagia: ''
    };

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

  getMagiaIndex(magia: any): number {

    return this.magias.indexOf(magia);

  }

  removerMagia(magia: any): void {

    const index = this.magias.indexOf(magia);

    if (index > -1) {

      this.magias.splice(index, 1);

      if (this.magiaSelecionada === magia) {
        this.magiaSelecionada = null;
      }
    }
  }
  get magiasPorNivel() {

    const grupos: { [nivel: string]: any[] } = {};

    for (const magia of this.magias) {

      const nivel = magia.nivelMagia;

      if (!grupos[nivel]) {
        grupos[nivel] = [];
      }

      grupos[nivel].push(magia);

    }

    return grupos;

  }

  getNivelFormatado(nivel: string): string {

    if (nivel === '0') {
      return 'Truques';
    }

    return `${nivel}º Nível`;

  }

  getMagiasPorNivel(nivel: string): any[] {

    return this.magiasPorNivel[nivel] || [];
  }
  testeClique(magia: any): void {
    console.log("CLIQUE FUNCIONOU", magia);
  }
}