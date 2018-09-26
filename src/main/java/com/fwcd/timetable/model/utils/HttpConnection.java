package com.fwcd.timetable.model.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.stream.Stream;

public class HttpConnection implements AutoCloseable {
	private final HttpURLConnection urlConnection;
	private final InputStream inputStream;
	
	public HttpConnection(HttpURLConnection urlConnection) {
		this.urlConnection = urlConnection;
		try {
			urlConnection.connect();
			inputStream = urlConnection.getInputStream();
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	public InputStream getInputStream() { return inputStream; }
	
	public String readString() {
		StringBuilder str = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line = reader.readLine();
			while (line != null) {
				str.append(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new HttpException(e);
		}
		
		return str.toString();
	}
	
	public Stream<String> readLines() {
		Stream.Builder<String> streamBuilder = Stream.builder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
		try {
			String line = reader.readLine();
			while (line != null) {
				streamBuilder.accept(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new HttpException(e);
		}
		
		return streamBuilder.build();
	}
	
	@Override
	public void close() {
		try {
			inputStream.close();
		} catch (IOException e) {
			throw new HttpException(e);
		}
		urlConnection.disconnect();
	}
}
