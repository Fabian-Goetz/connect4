import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameComponent } from './game.component';
import { MaterialModule } from 'src/app/material.module';
import { Routes, RouterModule } from '@angular/router';
import { ComponentsModule } from 'src/app/components/components.module';


const routes: Routes = [
  {
    path: '',
    component: GameComponent
  }
];

@NgModule({
  declarations: [GameComponent],
  imports: [
    MaterialModule,
    CommonModule,
    RouterModule.forChild(routes),
    ComponentsModule,
  ]
})
export class GameModule { }
