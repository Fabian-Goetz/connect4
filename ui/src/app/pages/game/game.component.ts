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
    const players = [
        {
          "name": names.namePlayerOne,
          "color": "blue",
          "hasTurn": false
        },
        {
          "name": names.namePlayerTwo,
          "color": "yellow",
          "hasTurn": false
        }
      ];

    this.gameService.setPlayers(players);
    this.gameService.start().then(data => {
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

  public makeMove(col: number) {
    this.gameService.makeMove(col).then(data => {
      console.log(data);
    });
  }

}
