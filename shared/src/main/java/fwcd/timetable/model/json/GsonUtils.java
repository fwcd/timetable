package fwcd.timetable.model.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import fwcd.fructose.Observable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
	private GsonUtils() {}
	
	public static GsonBuilder buildGson() {
		return new GsonBuilder()
			.registerTypeAdapter(Observable.class, new ObservableSerializer<>())
			.registerTypeAdapterFactory(new GsonPostDeserializationFactory());
	}
	
	public static Gson newGson() {
		return buildGson().create();
	}
	
	/**
	 * Uses reflection to determine the generic type
	 * argument of the provided type.
	 */
	static Type getTypeArgument(Type context) {
		// Source: https://github.com/google/gson/blob/master/gson/src/main/java/com/google/gson/internal/%24Gson%24Types.java
		// License: http://www.apache.org/licenses/LICENSE-2.0
		// Copyright (C) 2008 Google Inc.
		// Modified by fwcd
		
		Type base = context;
		
		if (base instanceof WildcardType) {
			base = ((WildcardType) base).getUpperBounds()[0];
		}
		if (base instanceof ParameterizedType) {
			return ((ParameterizedType) base).getActualTypeArguments()[0];
		}
		
		return Object.class;
	}
}
