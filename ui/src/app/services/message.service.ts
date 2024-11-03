import { Injectable } from '@angular/core';
import { MessageInterface } from '../types/message.interface';
import { ChatResDTO } from '../types/Chat';
import { Message } from '../websocket/websocket.component';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageService implements MessageInterface {

  private readonly URL_BASE = "http://localhost:7070/api/chats"

  constructor(private readonly http: HttpClient) {}

  getChatsByUser(userId: string): Observable<ChatResDTO[]> {
    const url = `${this.URL_BASE}/${userId}`;
    return this.http.get<ChatResDTO[]>(url);
  }

  getMessageBetweenUsers(user1Id: string, user2Id: string): Observable<Message[]> {
    const url = `${this.URL_BASE}/${user1Id}/and/${user2Id}/messages`;
    return this.http.get<Message[]>(url);
  }
}
