import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { GameService } from 'src/app/services/game/game.service';

@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  constructor(private gameService: GameService) { }

  ngOnInit() { }

  onDrop(col: number) {
    this.gameService.makeMove(col).then((data: any) => {
      console.log('Current Player: ', data.game.currentPlayer)
      if (data.game.isOver === true) {
        this.gameService.setWinner(data.game.winner);
        this.gameService.setStatus('FINISH');
      }
      this.gameService.setBoard(data.game.board);
      this.gameService.setCurrentPlayer(data.game.currentPlayer);
      this.gameService.transformBoard();
      console.log(data);
    });
  }

}
