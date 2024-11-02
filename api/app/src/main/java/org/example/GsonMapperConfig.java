package org.example;

import java.lang.reflect.Type;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.javalin.json.JsonMapper;

public class GsonMapperConfig implements JsonMapper {
    private final Gson gson;

    // Construtor para inicializar o Gson
    public GsonMapperConfig() {
        this.gson = new GsonBuilder().create();
    }

    @Override
    public String toJsonString(@NotNull Object obj, @NotNull Type type) {
        return gson.toJson(obj, type);
    }

    @Override
    public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
        return gson.fromJson(json, targetType);
    }
}
