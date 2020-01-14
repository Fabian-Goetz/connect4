import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  {
    path: '',
    redirectTo: 'connect-four',
    pathMatch: 'full'
  },
  {
    path: 'connect-four',
    loadChildren: () => import('./pages/game/game.module').then(m => m.GameModule)
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
