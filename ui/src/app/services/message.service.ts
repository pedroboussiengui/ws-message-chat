import { Injectable } from '@angular/core';
import { Message } from '../websocket/websocket.component';
import { BehaviorSubject } from 'rxjs';

export class Chat {
  public chatId: string;
  public user1Id: string;
  public user2Id: string;
  public messages: Message[];

  constructor(user1Id: string, user2Id: string) {
    this.user1Id = user1Id;
    this.user2Id = user2Id;
    this.chatId = this.generateChatId(user1Id, user2Id);
    this.messages = [];
  }

  private generateChatId(user1Id: string, user2Id: string): string {
    // Ordena os IDs para garantir uma chave consistente
    return user1Id < user2Id ? `${user1Id}_${user2Id}` : `${user2Id}_${user1Id}`;
  }

  public getChatId(): string {
    return this.chatId;
  }

  public getMessages(): Message[] {
    return this.messages;
  }

  public addMessage(message: Message): void {
    this.messages.push(message);
  }

  public getOther(userId: string): string {
    return userId === this.user1Id ? this.user2Id : this.user1Id;
  }

  public getLasMessage(): string | null {
    return this.messages.length > 0 ? this.messages[this.messages.length - 1].content : null;
  }
}

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private chatsSubject = new BehaviorSubject<Chat[]>([]);
  chats$ = this.chatsSubject.asObservable();

  constructor() {
    // this.generateMockChats();
  }

  createChat(user1Id: string, user2Id: string): Chat {
    const chatId = this.generateChatId(user1Id, user2Id);
  
    // Verifica se o chat já existe
    const existingChat = this.chatsSubject.getValue().find(chat => chat.getChatId() === chatId);
    if (existingChat) {
      return existingChat; // Retorna o chat existente
    }
    
    // Se não existir, cria um novo chat
    const chat = new Chat(user1Id, user2Id);
    this.chatsSubject.next([...this.chatsSubject.getValue(), chat]); 
    console.log(`Chat criado: ${chat.getChatId()}`);
    return chat; // Retorna o chat criado
  }

  getChats(): Chat[] {
    return this.chatsSubject.getValue();
  }

  getChatsByUser(userId: string): Chat[] {
    return this.chatsSubject.getValue().filter(chat => chat.user1Id === userId || chat.user2Id === userId);
  }

  // Busca um chat específico entre dois usuários
  getChatBetweenUsers(user1Id: string, user2Id: string): Chat | undefined {
    const chatId = this.generateChatId(user1Id, user2Id);
    return this.chatsSubject.getValue().find(chat => chat.getChatId() === chatId);
  }

  // Método para gerar o chatId de maneira consistente
  private generateChatId(user1Id: string, user2Id: string): string {
    return user1Id < user2Id ? `${user1Id}_${user2Id}` : `${user2Id}_${user1Id}`;
  }

  private generateMockChats(): void {
    const userPairs = [
      { user1Id: '1', user2Id: '2' },
      { user1Id: '1', user2Id: '3' },
      { user1Id: '1', user2Id: '4' },
      { user1Id: '2', user2Id: '3' }
    ];

    userPairs.forEach(pair => {
      const chat = new Chat(pair.user1Id, pair.user2Id);

      // Adiciona mensagens fictícias entre os usuários com timestamps
      chat.addMessage(new Message(pair.user1Id, pair.user2Id, 'Hello! How are you?', new Date().toISOString()));
      chat.addMessage(new Message(pair.user2Id, pair.user1Id, 'I am good, thanks! And you?', new Date().toISOString()));
      chat.addMessage(new Message(pair.user1Id, pair.user2Id, 'Busy with work, but all is well.', new Date().toISOString()));
      chat.addMessage(new Message(pair.user2Id, pair.user1Id, 'Glad to hear! Let’s catch up soon!', new Date().toISOString()));

      this.chatsSubject.next([...this.chatsSubject.getValue(), chat]); 
    });
  }

}
