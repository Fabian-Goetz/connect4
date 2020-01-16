import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-end',
  templateUrl: './end.component.html',
  styleUrls: ['./end.component.scss']
})
export class EndComponent implements OnInit {

  @Input() winner: string = '';
  @Output('again') again: EventEmitter<any> = new EventEmitter<any>();
  @Output('quit') quit: EventEmitter<any> = new EventEmitter<any>();

  constructor() { }

  ngOnInit() {}

  public onPlayAgain() {
    this.again.emit(true);
  }

  public onQuit() {
    this.quit.emit(true);
  }

}
