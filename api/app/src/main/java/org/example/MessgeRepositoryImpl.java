package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class MessgeRepositoryImpl implements MessageRepository {

    private final MongoCollection<Document> collection;
    GsonMapperConfig gsonMapper = new GsonMapperConfig();

    public MessgeRepositoryImpl(MongoDBClient client) {
        this.collection = client.getDatabase().getCollection("message_db");
    }

    @Override
    public String createOrRecoverChat(String user1Id, String user2Id) {
        String chatId = Chat.generateChatId(user1Id, user2Id);
        Bson filter = Filters.eq("_id", chatId);
        Document document = collection.find(filter).first();

        if (document != null) {
            Chat chat = gsonMapper.fromJsonString(document.toJson(), Chat.class);
            System.out.println(chat);
            return chat.getChatId();
        }

        Chat newChat = new Chat(user1Id, user2Id);
        Document chat = new Document("_id", newChat.getChatId())
                .append("user1Id", newChat.getUser1Id())
                .append("user2Id", newChat.getUser2Id())
                .append("messages", newChat.getMessages());
        this.collection.insertOne(chat);
        return newChat.getChatId();
    }

    @Override
    public List<Chat> getChats() {
        List<Chat> chats = new ArrayList<>();
        for (Document document : collection.find()) {
            Chat chat = gsonMapper.fromJsonString(document.toJson(), Chat.class);
            chats.add(chat);
        }
        return chats;
    }

    @Override
    public List<Chat> getChatsByUser(String userId) {
        List<Chat> chats = new ArrayList<>();
        Bson filter = Filters.or(
                Filters.eq("user1Id", userId),
                Filters.eq("user2Id", userId));
        for (Document document : collection.find(filter)) {
            Chat chat = gsonMapper.fromJsonString(document.toJson(), Chat.class);
            chats.add(chat);
        }
        return chats;
    }

    @Override
    public Optional<Chat> getChatById(String chatId) {
        Document document = collection.find(Filters.eq("_id", chatId)).first();
        if (document != null) {
            Chat chat = gsonMapper.fromJsonString(document.toJson(), Chat.class);
            return Optional.of(chat);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Chat> getChatBetweenUsers(String user1Id, String user2Id) {
        String chatId = Chat.generateChatId(user1Id, user2Id);
        Bson filter = Filters.eq("_id", chatId);
        Document document = collection.find(filter).first();
        if (document != null) {
            Chat chat = gsonMapper.fromJsonString(document.toJson(), Chat.class);
            return Optional.of(chat);
        }
        return Optional.empty();
    }

    @Override
    public void updateChatById(String chatId, Message message) {
        Document messageDocument = messageToDocument(message);
        Bson filter = Filters.eq("_id", chatId);
        Bson update = Updates.push("messages", messageDocument);
        collection.updateOne(filter, update);
    }

    private Document messageToDocument(Message message) {
        return new Document("originId", message.getOriginId())
                .append("destinyId", message.getDestinyId())
                .append("content", message.getContent())
                .append("timestamp", message.getTimestamp());
    }
}
