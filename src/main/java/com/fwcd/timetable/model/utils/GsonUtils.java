package com.fwcd.timetable.model.utils;

import com.google.gson.GsonBuilder;

public class GsonUtils {
	private GsonUtils() {}
	
	public static GsonBuilder buildGson() {
		return new GsonBuilder()
			.registerTypeAdapterFactory(new GsonPostDeserializationFactory());
	}
}
