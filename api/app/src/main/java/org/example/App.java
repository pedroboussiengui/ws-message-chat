/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        var app = Javalin.create(/*config*/)
            .start(7070);

        app.ws("/websocket/{path}", ws -> {
            ws.onConnect(ctx -> System.out.println("Connected"));
            ws.onMessage(ctx -> {
                String msg = ctx.message();
                
                ctx.send(msg);
            });
        });
    }
}
