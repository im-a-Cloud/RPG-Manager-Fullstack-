import { Routes } from '@angular/router';

import { CharacterDetailComponent } from './pages/character-detail/character-detail.component';
import { CriarFichaPersonagemComponent} from './pages/criar-ficha-personagem/criar-ficha-personagem.component';
import { TesteConexao } from './features/personagens/pages/teste-conexao/teste-conexao';
import { HomeComponent } from './pages/home/home.component';
import { CadastroComponent } from './pages/cadastro/cadastro';
import { LoginComponent } from './pages/login/login';

export const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'cadastro', component: CadastroComponent},
    {path: 'login', component: LoginComponent },
    {path: 'teste-conexao', component: TesteConexao},
    { path: 'personagem/:id', component: CharacterDetailComponent },
    {path: 'criar-ficha', component: CriarFichaPersonagemComponent}
];
