package com.fwcd.timetable.model.language;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fwcd.fructose.io.InputStreamable;
import com.fwcd.timetable.model.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LanguageParser {
	private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();
	private static final Gson GSON = GsonUtils.buildGson().create();
	
	public Language parseFromJson(String name, InputStreamable source) {
		Map<String, String> map = source.mapStream(in -> {
			try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
				return GSON.fromJson(reader, MAP_TYPE);
			}
		});
		return new Language(name, map);
	}
}
