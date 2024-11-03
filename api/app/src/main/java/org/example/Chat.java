package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.annotations.SerializedName;

public class Chat {
    @SerializedName("_id")
    private String chatId;
    public String user1Id;
    public String user2Id;
    public List<Message> messages;

    public Chat(String user1Id, String user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.chatId = generateChatId(user1Id, user2Id);
        this.messages = new ArrayList<>();
    }

    public static String generateChatId(String user1Id, String user2Id) {
        if (Integer.valueOf(user1Id) < Integer.valueOf(user2Id))
            return String.format("%s_%s", user1Id, user2Id);
        return String.format("%s_%s", user2Id, user1Id);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUser1Id() {
        return user1Id;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public String getOther(String userId) {
        if (userId.equals(this.user1Id))
            return user2Id;
        return user1Id;
    }    
    
    public Optional<String> getLasMessage() {
        if (!this.messages.isEmpty()) {
            String msg = this.messages.get(this.messages.size() - 1).getContent();
            return Optional.of(msg);
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Chat [chatId=" + chatId + ", user1Id=" + user1Id + ", user2Id=" + user2Id + ", messages=" + messages
                + "]";
    }
}