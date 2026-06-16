import { Routes } from '@angular/router';

import {Home} from './pages/home/home';
import { CharacterDetailComponent } from './pages/character-detail/character-detail.component';
import { CriarFichaPersonagemComponent} from './pages/criar-ficha-personagem/criar-ficha-personagem.component';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'characters-detail', component: CharacterDetailComponent},
    {path: 'criar-ficha', component: CriarFichaPersonagemComponent}
];
