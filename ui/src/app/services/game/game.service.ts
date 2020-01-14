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

  public start(players: any) {
    const url = this.baseUrl + '/create-game';

    const body = [
      {
        name: this.getPlayers().names.namePlayerOne,
        color: 'red'
      },
      {
        name: this.getPlayers().names.namePlayerTwo,
        color: 'blue'
      }
    ]
    return this.http.post(url, body).toPromise();
  }

  public makeMove() {

  }

}
