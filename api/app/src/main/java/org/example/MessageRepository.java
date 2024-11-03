package org.example;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    String createOrRecoverChat(String user1Id, String user2Id);
    List<Chat> getChats();
    List<Chat> getChatsByUser(String userId);
    Optional<Chat> getChatBetweenUsers(String user1Id, String user2Id);
    Optional<Chat> getChatById(String chatId);
    void updateChatById(String chatId, Message message);
}
