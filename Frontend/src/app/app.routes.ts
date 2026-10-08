import { Routes } from '@angular/router';

import {Home} from './pages/home/home';
import { CharacterDetailComponent } from './pages/character-detail/character-detail.component';
import { CriarFichaPersonagemComponent} from './pages/criar-ficha-personagem/criar-ficha-personagem.component';
import { TesteConexao } from './features/personagens/pages/teste-conexao/teste-conexao';
import{ HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'teste-conexao', component: TesteConexao},
    {path: 'characters-detail', component: CharacterDetailComponent},
    {path: 'criar-ficha', component: CriarFichaPersonagemComponent}
];
