package org.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

public class App {
    // private static Set<WsContext> users = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static Map<String, WsContext> users = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        GsonMapperConfig gsonMapper = new GsonMapperConfig();
        var app = Javalin.create(config -> config.jsonMapper(gsonMapper))
                .start(7070);

        app.ws("/websocket/{origin}/to/{destiny}", ws -> {
            ws.onConnect(ctx -> {
                String originId = ctx.pathParam("origin");
                users.put(originId, ctx);
                System.out.println("User " + originId + " connected");
            });
            ws.onMessage(ctx -> {
                String msg = ctx.message();
                String originId = ctx.pathParam("origin");
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime());

                // String formattedMessage = String.format("%s:%s %s", userId, msg, timeStamp);
                
                var formattedMessage = new Message(originId, msg, timeStamp);

                String destiny = ctx.pathParam("destiny");

                // recupera o user específico
                WsContext targetUser = users.get(destiny);
                WsContext ownUser = users.get(originId);

                if (targetUser != null && targetUser.session.isOpen()) {
                    targetUser.send(formattedMessage);
                    ownUser.send(formattedMessage);
                }

                // Envia a mensagem para todos os clientes conectados
                // users.forEach(session -> {
                //     if (session.session.isOpen()) {
                //         session.send(formattedMessage);
                //     }
                // });
            });
            ws.onClose(ctx -> {
                String originId = ctx.pathParam("origin");
                users.remove(originId);
                System.out.println("User " + originId + " disconnected");
            });
        });
    }
}
