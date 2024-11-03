import { Component, OnDestroy, OnInit } from '@angular/core';
import { WebsocketService } from '../services/websocket.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from '../services/message.service';

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
  messages: Message[] = [];

  // public chat!: Chat;

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
      this.loadMessages(this.origin, this.destiny);
      this.webSocketService.connect(this.origin, this.destiny);
      this.subscription = this.webSocketService.onMessage().subscribe({
        next: (message) => {
          const originId = message.originId;
          const destinyId = message.destinyId;
          const content = message.content;
          const timestamp = message.timestamp;
          this.messages.push(new Message(originId, destinyId, content, timestamp));
        },
        error: (err) => {
          console.error("Erro na conexão", err.error.message);
        }
      });
    }
  }

  loadMessages(origin: string, destiny: string) {
    this.messageService.getMessageBetweenUsers(origin, destiny).subscribe({
      next: (data) => {
        this.messages = data;
      },
      error: (err) => {
        console.error(err.error.message);
      }
    });
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


