package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBClient {

    private static MongoDBClient instance;
    private static MongoClient mongoClient;
    private MongoDatabase database;

    private MongoDBClient(String url, String dbname) {
        this.mongoClient = MongoClients.create(url);
        this.database = this.mongoClient.getDatabase(dbname);
    }

    public static synchronized MongoDBClient getInstance(String url, String dbname) {
        if (instance == null) {
            instance = new MongoDBClient(url, dbname);
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
