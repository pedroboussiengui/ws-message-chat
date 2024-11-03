export interface Message {
    originId: string,
    destinyId: string,
    content: string,
    timestamp: string
}

export interface Chat {
    chatId: string;
    user1Id: string;
    user2Id: string;
    messages: Message[];
}

export interface ChatResDTO {
    otherUser: string;
    lastMessage: string;
}