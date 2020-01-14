import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.scss']
})
export class StartComponent implements OnInit {

  public form: FormGroup;
  @Output('start') start: EventEmitter<any> = new EventEmitter<any>();

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.form = this.fb.group({
      namePlayerOne: ['', [Validators.required]],
      namePlayerTwo: ['', [Validators.required]],
    });
  }

  public onStartGame(names: any) {
    this.start.emit(names);
  }

}
