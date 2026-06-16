import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms'

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

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

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

  voltar() {
    this.router.navigate(['/characters']);
  }
}