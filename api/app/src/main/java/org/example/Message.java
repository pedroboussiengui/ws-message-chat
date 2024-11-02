package org.example;

public class Message {
    private String originId;
    private String content;
    private String timestamp;

    public Message(String originId, String content, String timestamp) {
        this.originId = originId;
        this.content = content;
        this.timestamp = timestamp;
    }
}
