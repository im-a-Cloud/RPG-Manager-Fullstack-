import { Component,inject } from '@angular/core';
import {CommonModule} from '@angular/common';

import { PersonagemService } from '../../../../core/services/personagem.service';
import { Personagem } from '../../../../models/personagem';


@Component({
  selector: 'app-teste-conexao',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './teste-conexao.html',
  styleUrl: './teste-conexao.scss',
})
export class TesteConexao {
  private personagemService = inject(PersonagemService);

  personagens: Personagem[] = [];

}