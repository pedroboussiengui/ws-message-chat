package org.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

public class App {
    private static final Map<String, WsContext> users = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        GsonMapperConfig gsonMapper = new GsonMapperConfig();
        var app = Javalin.create(config -> config.jsonMapper(gsonMapper))
                .start(7070);
        
        MongoDBClient mongoDBClient = MongoDBClient.getInstance("mongodb://localhost:27017", "ws_chat");
        MessageRepository messageRepository = new MessgeRepositoryImpl(mongoDBClient);

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

                // salva a mensagem
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
