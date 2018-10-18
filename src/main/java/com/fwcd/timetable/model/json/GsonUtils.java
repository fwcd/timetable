package com.fwcd.timetable.model.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
	private GsonUtils() {}
	
	public static GsonBuilder buildGson() {
		return new GsonBuilder()
			.registerTypeAdapterFactory(new GsonPostDeserializationFactory());
	}
	
	public static Gson newGson() {
		return buildGson().create();
	}
}
