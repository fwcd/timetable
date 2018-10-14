package com.fwcd.timetable.model.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
	private GsonUtils() {}
	
	public static Gson newGson(Class<?>... polymorphicTypes) {
		GsonBuilder builder = new GsonBuilder()
			.registerTypeAdapterFactory(new GsonPostDeserializationFactory());
		
		for (Class<?> polymorphicType : polymorphicTypes) {
			builder.registerTypeAdapter(polymorphicType, new PolymorphicSerializer<>());
		}
		
		return builder.create();
	}
}
