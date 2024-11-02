import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private socket$!: WebSocketSubject<any>;
  private readonly WS_ENDPOINT = 'ws://localhost:7070/websocket';

  constructor() {}

  // inicia a conexão com o websocket server
  // public connect(path: string): void {
  //   this.socket$ = webSocket({
  //     url: `${this.WS_ENDPOINT}/${path}`,
  //     deserializer: (e) => e.data // quero apenas trablhar com o data da message, que é string
  //   });
  // } 

  public connect(origin: string, destiny: string): void {
    this.socket$ = webSocket({
      url: `${this.WS_ENDPOINT}/${origin}/to/${destiny}`
    });
  } 

  // enviar mensagem para servidor
  public sendMessage(message: any): void {
    if (this.socket$) {
      this.socket$.next(message);
    }
  }

  public onMessage(): Observable<any> {
    return this.socket$.asObservable();
  }

  public close(): void {
    if (this.socket$) {
      this.socket$.complete();
    }
  }
}
