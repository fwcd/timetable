package com.fwcd.timetable.model.utils;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Automatically calls the {@link PostDeserializable} interface
 * after deserialization.
 */
public class GsonPostDeserializationFactory implements TypeAdapterFactory {
	// Source: https://medium.com/@elye.project/post-processing-on-gson-deserialization-26ce5790137d
	
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
		return new TypeAdapter<T>() {
			@Override
			public void write(JsonWriter out, T value) throws IOException {
				delegate.write(out, value);
			}
			
			@Override
			public T read(JsonReader in) throws IOException {
				T value = delegate.read(in);
				if (value instanceof PostDeserializable) {
					((PostDeserializable) value).postDeserialize();
				}
				return value;
			}
		};
	}
}
