import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chat, MessageService } from '../services/message.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  id!: any;
  chats: Chat[] = [];

  constructor(
    private readonly messageService: MessageService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('userId');
    this.messageService.chats$.subscribe(chats => {
      this.chats = this.messageService.getChatsByUser(this.id as string);
      console.log(this.chats.length);
    });
  }

  navigateToChat(destinyId: string): void {
    this.router.navigate([`/${this.id}/to/${destinyId}`]);
  }
}
