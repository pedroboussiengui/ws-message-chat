package org.example.dto;

import org.example.Chat;

public class ChatResDTO {
    private String otherUser;
    private String lastMessage;

    public ChatResDTO(String currentUser, Chat chat) {
        this.otherUser = chat.getOther(currentUser);
        this.lastMessage = !chat.getLasMessage().isEmpty() ? chat.getLasMessage().get() : null;
    }

    public String getOtherUser() {
        return otherUser;
    }
    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }
    public String getLastMessage() {
        return lastMessage;
    }
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
