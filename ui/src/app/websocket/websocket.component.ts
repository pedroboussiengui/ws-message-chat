import { Component, OnDestroy, OnInit } from '@angular/core';
import { WebsocketService } from '../services/websocket.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { Chat, MessageService } from '../services/message.service';

export class Message {
  constructor(
    public originId: string,
    public destinyId: string,
    public content: string,
    public timestamp: string
  ) { }
}

@Component({
  selector: 'app-websocket',
  templateUrl: './websocket.component.html',
  styleUrl: './websocket.component.scss'
})
export class WebsocketComponent implements OnInit, OnDestroy {
  // messages: Message[] = [];

  public chat!: Chat;

  public subscription!: Subscription;
  public origin!: any;
  public destiny!: any;
  public toSend!: string;

  constructor(
    private readonly webSocketService: WebsocketService,
    private readonly messageService: MessageService,
    private readonly route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.origin = this.route.snapshot.paramMap.get('origin');
    this.destiny = this.route.snapshot.paramMap.get('destiny');
    if (this.origin && this.destiny) {
      const foundChat = this.messageService.getChatBetweenUsers(this.origin, this.destiny);
      if (foundChat) {
        this.chat = foundChat;
      } else {
        this.chat = this.messageService.createChat(this.origin, this.destiny);
      } 
      this.webSocketService.connect(this.origin, this.destiny);
      this.subscription = this.webSocketService.onMessage().subscribe(
        (message) => {
          console.log(this.messageService.getChats());
          const originId = message.originId;
          const destinyId = message.destinyId;
          const content = message.content;
          const timestamp = message.timestamp;
          this.chat.addMessage(new Message(originId, destinyId, content, timestamp));
        },
        (error) => {
          console.error("Erro na conexão", error);
        }
      );
    }
  }

  sendMessage(): void {
    console.log(this.origin)
    if (this.toSend.trim()) {
      this.webSocketService.sendMessage(this.toSend);
      this.toSend = '';
    }
  }

  ngOnDestroy(): void {
    this.webSocketService.close();
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
