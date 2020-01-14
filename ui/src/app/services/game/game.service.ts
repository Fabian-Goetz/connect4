import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private players;
  private currentPlayer;
  private winner;
  private board;
  private simpleBoard = [
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0]
  ];
  private status = 'START';
  private baseUrl = 'http://localhost:9000';

  constructor(private http: HttpClient) { }

  public getWinner() {
    return this.winner;
  }

  public setWinner(player: any) {
    this.winner = player;
  }

  public getCurrentPlayer() {
    return this.currentPlayer;
  }

  public setCurrentPlayer(player: any) {
    this.currentPlayer = player;
  }

  public getSimpleBoard() {
    return this.simpleBoard;
  }

  public setSimpleBoard(board: any) {
    this.simpleBoard = board;
  }

  public getBoard() {
    return this.board;
  }

  public setBoard(board: any) {
    this.board = board;
  }

  public getPlayers() {
    return this.players;
  }

  public setPlayers(players) {
    this.players = players;
  }

  public getStatus() {
    return this.status;
  }

  public setStatus(status) {
    this.status = status;
  }

  public start() {
    const url = this.baseUrl + '/create-game';
    const body = this.getPlayers();
    return this.http.post(url, body).toPromise();
  }

  public makeMove(col: number) {
    console.log('col: ', col);
    const url = this.baseUrl + '/insert-chip';
    const body = {
      "round": {
        "board": this.getBoard(),
        "players": this.getPlayers(),
        "currentPlayer": this.getCurrentPlayer(),
        "roundNumber": 0,
        "isOver": false
      },
      "column": col
    };
    return this.http.post(url, body).toPromise();
  }

  public reset() {
    this.simpleBoard = [
      [0, 0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0, 0],
      [0, 0, 0, 0, 0, 0, 0]
    ];
    const board: any = this.getBoard();
    board.chips = [];
    this.setBoard(board);
  }

  public transformBoard() {
    const board: any = this.getBoard();
    const simpleBoard = this.getSimpleBoard();
    const playerOne = this.getPlayers()[0].name;

    board.chips.forEach((chip: any) => {
      const number = chip.player.name === playerOne ? 1 : 2;
      simpleBoard[chip.position.y][chip.position.x] = number;
    });

    this.setSimpleBoard(simpleBoard);
  }

}
