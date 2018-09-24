package com.fwcd.timetable.model.language;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

import com.fwcd.fructose.io.InputStreamable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LanguageParser {
	private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();
	private final Gson gson = new Gson();
	
	public Language parseFromJson(InputStreamable source) {
		Map<String, String> map = source.mapStream(in -> {
			try (Reader reader = new InputStreamReader(in)) {
				return gson.fromJson(reader, MAP_TYPE);
			}
		});
		return new Language(map);
	}
}
