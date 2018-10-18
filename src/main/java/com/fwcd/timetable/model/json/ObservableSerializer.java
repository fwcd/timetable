package com.fwcd.timetable.model.json;

import java.lang.reflect.Type;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.ReadOnlyObservable;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ObservableSerializer<T> implements JsonSerializer<ReadOnlyObservable<T>>, JsonDeserializer<Observable<T>> {
	@Override
	public JsonElement serialize(ReadOnlyObservable<T> src, Type typeOfSrc, JsonSerializationContext context) {
		return context.serialize(src.get());
	}
	
	@Override
	public Observable<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		return new Observable<>(context.deserialize(json, GsonUtils.getTypeArgument(typeOfT)));
	}
}
