import { Chat } from "./message.service";

export interface MessageInterface {
    createChat(userId: string, user2Id: string): string;
    getChats(): Chat[];
    getChatsByUser(userId: string): Chat[];
    getChatBetweenUsers(user1Id: string, user2Id: string): Chat | undefined
}