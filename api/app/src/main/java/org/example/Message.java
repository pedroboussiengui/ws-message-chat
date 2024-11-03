package org.example;

public class Message {
    private String originId;
    private String destinyId;
    private String content;
    private String timestamp;

    public Message(String originId, String destinyId, String content, String timestamp) {
        this.originId = originId;
        this.destinyId = destinyId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getOriginId() {
        return originId;
    }

    public String getDestinyId() {
        return destinyId;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
