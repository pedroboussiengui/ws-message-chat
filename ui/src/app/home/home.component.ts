import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from '../services/message.service';
import { ChatResDTO } from '../types/Chat';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  id!: any;
  chats: ChatResDTO[] = [];

  constructor(
    private readonly messageService: MessageService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('userId');
    this.loadChats();
  }

  loadChats() {
    this.messageService.getChatsByUser(this.id as string).subscribe({
      next: (data) => {
        this.chats = data
      },
      error: (err) => console.log(err.error.message)
    });
  }

  navigateToChat(destinyId: string): void {
    this.router.navigate([`/${this.id}/to/${destinyId}`]);
  }
}
