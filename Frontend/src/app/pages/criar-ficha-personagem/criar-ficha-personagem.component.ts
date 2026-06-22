import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms'
import fa from '@angular/common/locales/fa';
import th from '@angular/common/locales/th';

@Component({
  selector: 'app-criar-ficha-personagem',
  imports: [CommonModule, FormsModule],
  templateUrl: './criar-ficha-personagem.html',
  styleUrl: './criar-ficha-personagem.scss',
})
export class CriarFichaPersonagemComponent implements OnInit {
  id: string | null = null;
  imagemPreview: string | null = null;
  nomeArquivo: string = '';
  visivel: {[key: string]: boolean} = {
    addProficiencia: false,
    addMagia: false,
    addItem: false,
    addHabilidade: false 
  };
 listaPericias = [
  'acrobacia', 'adestrarAnimais', 'arcanismo', 'atletismo',
  'enganacao', 'historia', 'intuicao', 'intimidacao',
  'investigacao', 'medicina', 'natureza', 'percepcao',
  'atuacao', 'persuacao', 'religiao', 'prestidigitacao',
  'furtividade', 'sobrevivencia'
];

pericias: { [key: string]: boolean } = Object.fromEntries(
  this.listaPericias.map(p => [p, false])
);

valoresPericias: { [key: string]: number } = Object.fromEntries(
  this.listaPericias.map(p => [p, 0])
);

  periciasPorAtributo = {
    forca: ['atletismo'],
    destreza: ['acrobacia', 'prestidigitacao', 'furtividade'],
    inteligencia: ['arcanismo', 'historia', 'investigacao', 'natureza', 'religiao'],
    sabedoria: ['adestrarAnimais', 'intuicao', 'medicina', 'percepcao', 'sobrevivencia'],
    carisma: ['enganacao', 'intimidacao', 'atuacao', 'persuacao']
  };
  resistencias: { [key: string]: boolean } = {
    forca: false,
    destreza: false,
    constituicao: false,
    inteligencia: false,
    sabedoria: false,
    carisma: false
  };

  habilidades: any[] = [];

  novaHabilidade = {
    nome: '',
    origem: '',
    descricao: ''
  };
  proficiencias: any[]=[];

  novaProficiencia ={
    tipoProfPersonagem: '',
    listaProficiencias: ''
  };

  inventario: any [] =[];

  novoItem={
    nomeItem: '',
    raridadeItem:'',
    quantidadeItem:'',
    pesoItem: '',
    tipoItem:'',
    descricaoItem:''
  }

  valorTotalPericia: number = 0;

  forcaPersonagem: number = 0;
  destrezaPersonagem: number = 0;
  constituicaoPersonagem: number = 0;
  inteligenciaPersonagem: number = 0;
  sabedoriaPersonagem: number = 0;
  carismaPersonagem: number = 0;

  nivelPersonagem: number = 1;

  constructor(
    private route: ActivatedRoute,
    private router: Router
    
  ) {
    const todasPericias = Object.values(this.periciasPorAtributo).flat();
    this.pericias = Object.fromEntries(todasPericias.map(p => [p, false]));
    this.valoresPericias = Object.fromEntries(todasPericias.map(p => [p, 0]));
  }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    console.log('ID do personagem:', this.id);
  }

  onSelectedFile(event: any) {
    const file = event.target.files[0];
    
    if (file) {
      this.nomeArquivo = file.name;
      
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.imagemPreview = e.target.result;
        console.log('Imagem carregada com sucesso!'); // Debug
      };
      reader.readAsDataURL(file);
    } else {
      console.log('Nenhum arquivo selecionado');
    }
  }

  removerImagem() {
    this.imagemPreview = null;
    this.nomeArquivo = '';
    const fileInput = document.getElementById('imagemPersonagem') as HTMLInputElement;

    if (fileInput) {
      fileInput.value = '';
    }
    console.log('Imagem removida'); // Debug
  }

  toggleSecao(secao: string){
    this.visivel[secao] = !this.visivel[secao];
  }

  mostrarSecao(secao: string){
    this.visivel[secao] = true;
  }

  esconderSecao(secao: string){
    this.visivel[secao] = false;
  }

  calcularBonusAtributo(valor: number): number{
    return Math.floor((valor - 10)/2);
  }

  get bonusForca(): number{
    return this.calcularBonusAtributo(this.forcaPersonagem);
  }
  get bonusDestreza(): number{
    return this.calcularBonusAtributo(this.destrezaPersonagem);
  }
  get bonusConstituicao(): number{
    return this.calcularBonusAtributo(this.constituicaoPersonagem);
  }
  get bonusInteligencia(): number{
    return this.calcularBonusAtributo(this.inteligenciaPersonagem);
  }
  get bonusSabedoria(): number{
    return this.calcularBonusAtributo(this.sabedoriaPersonagem);
  }
  get bonusCarisma(): number{
    return this.calcularBonusAtributo(this.carismaPersonagem);
  }

  calcularBonusProficiencia(valor: number):number{
    return Math.floor((valor - 1)/4) +2; 
  }

  getAtributoPorPericia(pericia: string): string{
    for (const[atributo, pericias] of Object.entries(this.periciasPorAtributo)){
      if(pericias.includes(pericia)){
        return atributo;
      }
    }
    return '';
  }

  getBonusAtributo(atributo: string): number {
    switch(atributo) {
      case 'forca': return this.calcularBonusAtributo(this.forcaPersonagem);
      case 'destreza': return this.calcularBonusAtributo(this.destrezaPersonagem);
      case 'constituicao': return this.calcularBonusAtributo(this.constituicaoPersonagem);
      case 'inteligencia': return this.calcularBonusAtributo(this.inteligenciaPersonagem);
      case 'sabedoria': return this.calcularBonusAtributo(this.sabedoriaPersonagem);
      case 'carisma': return this.calcularBonusAtributo(this.carismaPersonagem);
      default: return 0;
    }
  }

  getValorPericia(nome: string):number{
    const atributo = this.getAtributoPorPericia(nome);
    const bonusAtributo = this.getBonusAtributo(atributo);

    if(this.pericias[nome]){
      return bonusAtributo + this.calcularBonusProficiencia(this.nivelPersonagem);
    }
    return bonusAtributo;
  }

  get bonusProficiencia():number{
    return this.calcularBonusProficiencia(this.nivelPersonagem);
  }

  getTesteResistencia(atributo:string):number{
    const bonusAtributo = this.getBonusAtributo(atributo);
    
    if(this.resistencias[atributo]){
    return bonusAtributo + this.calcularBonusProficiencia(this.nivelPersonagem);
    }
    return bonusAtributo;
  }

  voltar() {
    this.router.navigate(['/characters']);
  }

  adicionarHabilidade(){
    if(this.novaHabilidade.nome.trim() && this.novaHabilidade.origem.trim()){
      this.habilidades.push({
        nome: this.novaHabilidade.nome,
        origem: this.novaHabilidade.origem,
        descricao: this.novaHabilidade.descricao || 'Sem descrição'
      });
      this.novaHabilidade = {
        nome: '',
        origem: '',
        descricao: ''
      };
    }else{
      alert('Por favor, preencha os campos de nome e origem');
    }
  }
 adicionarProficiencia() {
    // Verifica se os campos estão preenchidos
    if (this.novaProficiencia.tipoProfPersonagem.trim() && 
        this.novaProficiencia.listaProficiencias.trim()) {
      
      // Adiciona a nova proficiência à lista
      this.proficiencias.push({
        tipo: this.novaProficiencia.tipoProfPersonagem,
        lista: this.novaProficiencia.listaProficiencias
      });

      // Limpa o formulário
      this.novaProficiencia = {
        tipoProfPersonagem: '',
        listaProficiencias: ''
      };
    } else {
      alert('Por favor, preencha o Tipo e a Lista de proficiências!');
    }
  }
    adicionarItem() {
    // Validação
    if (!this.novoItem.nomeItem.trim()) {
      alert('O campo Nome do Item é obrigatório!');
      return;
    }

    if (!this.novoItem.quantidadeItem || Number(this.novoItem.quantidadeItem) <= 0) {
      alert('A quantidade deve ser maior que 0!');
      return;
    }

    // Adiciona o item
    this.inventario.push({
      nome: this.novoItem.nomeItem,
      raridade: this.novoItem.raridadeItem || 'Comum',
      quantidade: Number(this.novoItem.quantidadeItem),
      peso: Number(this.novoItem.pesoItem) || 0,
      tipo: this.novoItem.tipoItem || 'Diversos',
      descricao: this.novoItem.descricaoItem || 'Sem descrição'
    });

    // Limpa o formulário
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
      return total + (item.peso * item.quantidade);
    }, 0);
  }
   get quantidadeTotal(): number {
    return this.inventario.reduce((total, item) => {
      return total + item.quantidade;
    }, 0);
  }
}