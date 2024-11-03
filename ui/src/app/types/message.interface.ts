import { Observable } from "rxjs";
import { Message } from "../websocket/websocket.component";
import { ChatResDTO } from "./Chat";

export interface MessageInterface {
    getChatsByUser(userId: string): Observable<ChatResDTO[]>;
    getMessageBetweenUsers(user1Id: string, user2Id: string): Observable<Message[]>
}