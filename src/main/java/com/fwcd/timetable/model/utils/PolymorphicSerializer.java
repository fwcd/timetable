package com.fwcd.timetable.model.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PolymorphicSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		JsonElement content = context.serialize(src);
		JsonObject combined = new JsonObject();
		combined.add("v", content);
		combined.addProperty("c", src.getClass().getName());
		return combined;
	}
	
	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		JsonObject combined = json.getAsJsonObject();
		try {
			return context.deserialize(combined.get("v"), Class.forName(combined.get("c").getAsString()));
		} catch (ClassNotFoundException e) {
			throw new JsonParseException(e);
		}
	}
}
