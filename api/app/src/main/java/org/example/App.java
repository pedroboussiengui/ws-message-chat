package org.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.example.dto.ChatResDTO;

import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.websocket.WsContext;

public class App {
    private static final Map<String, WsContext> users = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        GsonMapperConfig gsonMapper = new GsonMapperConfig();
        var app = Javalin.create(config -> config.jsonMapper(gsonMapper))
                .start(7070);
        
        MongoDBClient mongoDBClient = MongoDBClient.getInstance("mongodb://localhost:27017", "ws_chat");
        MessageRepository messageRepository = new MessgeRepositoryImpl(mongoDBClient);

        app.get("/api/chats/{userId}", ctx -> {
            String userId = ctx.pathParam("userId");
            List<Chat> chats = messageRepository.getChatsByUser(userId);
            List<ChatResDTO> response = chats.stream()
                .map(chat -> new ChatResDTO(userId, chat))
                .toList();
            ctx.json(response);
        });

        app.get("/api/chats/{user1Id}/and/{user2Id}/messages", ctx -> {
            String user1Id = ctx.pathParam("user1Id");
            String user2Id = ctx.pathParam("user2Id");
            Optional<Chat> chat = messageRepository.getChatBetweenUsers(user1Id, user2Id);
            if (chat.isEmpty()) throw new NotFoundResponse("Chat not found");
            List<Message> messages = chat.get().getMessages();
            ctx.json(messages);
        });

        app.ws("/websocket/{origin}/to/{destiny}", ws -> {
            ws.onConnect(ctx -> {
                String originId = ctx.pathParam("origin");
                users.put(originId, ctx);
                System.out.println("User " + originId + " connected");
            });
            ws.onMessage(ctx -> {
                String msg = ctx.message();
                String originId = ctx.pathParam("origin");
                String destinyId = ctx.pathParam("destiny");
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());
                
                // monta a json message
                var formattedMessage = new Message(originId, destinyId, msg, timeStamp);

                // cria ou recupera o chat
                String chatIdCriado = messageRepository.createOrRecoverChat(originId, destinyId);

                // salva a mensagem na lista de mensagens do chat
                messageRepository.updateChatById(chatIdCriado, formattedMessage);

                // recupera o user target e o próprio user remetente
                WsContext targetUser = users.get(destinyId);
                WsContext ownUser = users.get(originId);
                
                // envia a mensagem apenas para os 2
                if (targetUser != null && targetUser.session.isOpen()) {
                    targetUser.send(formattedMessage);
                    ownUser.send(formattedMessage);
                }
            });
            ws.onClose(ctx -> {
                String originId = ctx.pathParam("origin");
                users.remove(originId);
                System.out.println("User " + originId + " disconnected");
            });
        });
    }
}
