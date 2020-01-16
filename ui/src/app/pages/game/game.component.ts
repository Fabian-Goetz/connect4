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
    this.gameService.setCurrentPlayer(players[0]);
    this.gameService.start().then((data: any) => {
      console.log(data);
      this.gameService.setBoard(data.game.rounds[0].board);
      this.gameService.setStatus('PLAYING');
    });
  }

  public again() {
    this.gameService.reset();
    this.gameService.setStatus('PLAYING');
    console.log(this.gameService.getSimpleBoard());
    console.log(this.gameService.getBoard());
  }

  public quit() {
    this.gameService.reset();
    this.gameService.setStatus('START');
    console.log('quit');
  }

}
