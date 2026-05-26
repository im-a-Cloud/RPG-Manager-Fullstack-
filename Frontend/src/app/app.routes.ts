import { Routes } from '@angular/router';

import {Home} from './pages/home/home';

import { CharacterDetailComponent } from './pages/character-detail/character-detail.component';

export const routes: Routes = [
    {path: '', component: Home},
    {path: 'characters-detail', component: CharacterDetailComponent
    }
];
