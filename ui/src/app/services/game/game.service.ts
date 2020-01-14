import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private players;
  private status = 'START';
  private baseUrl = 'http://localhost:9000';

  constructor(private http: HttpClient) { }

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
    const body = { players: this.getPlayers() };
    return this.http.post(url, body).toPromise();
  }

  public makeMove(col: number) {
    const url = this.baseUrl + '/insert-chip';
    const body = {
      "round": {
        "board": {
          "chips": [],
          "width": 7,
          "height": 6
        },
        "players": this.getPlayers(),
        "currentPlayer": {
          "name": "Fabian",
          "color": "red",
          "hasTurn": false
        },
        "roundNumber": 0,
        "isOver": false
      },
      "column": col
    };
    return this.http.post(url, body).toPromise();
  }

}
