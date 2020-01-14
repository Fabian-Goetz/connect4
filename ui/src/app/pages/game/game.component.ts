import { Component, OnInit } from '@angular/core';
import { GameService } from 'src/app/services/game/game.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  public players;

  constructor(private gameService: GameService) { }

  ngOnInit() { }

  public async startGame(names: { namePlayerOne: string, namePlayerTwo: string }) {
    console.log(names);
    this.gameService.setPlayers(names);
    this.gameService.start(names.namePlayerOne, names.namePlayerTwo).then(data => {
      console.log(data);
      this.gameService.setStatus('PLAYING');
    });
  }

  public again() {
    this.gameService.setStatus('PLAYING');
  }

  public quit() {
    this.gameService.setStatus('START');
    console.log('quit');
  }

  test() {
    this.gameService.makeMove().then(data => {
      console.log(data);
    });
  }

}
