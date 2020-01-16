import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StartComponent } from './start/start.component';
import { BoardComponent } from './board/board.component';
import { EndComponent } from './end/end.component';
import { MaterialModule } from '../material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { CardWrapperComponent } from './card-wrapper/card-wrapper.component';
import { ConnectFourBoardModule } from '@beyerleinf/ngx-cfb';



@NgModule({
  declarations: [
    StartComponent, 
    BoardComponent, 
    EndComponent, 
    CardWrapperComponent, 
  ],
  imports: [
    CommonModule,
    MaterialModule,
    ReactiveFormsModule,
    ConnectFourBoardModule
  ],
  exports: [
    StartComponent, 
    BoardComponent, 
    EndComponent,
    CardWrapperComponent,
  ]
})
export class ComponentsModule { }
