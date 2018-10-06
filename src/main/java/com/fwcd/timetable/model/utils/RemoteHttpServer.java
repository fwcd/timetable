package com.fwcd.timetable.model.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteHttpServer {
	private final URL url;
	
	public RemoteHttpServer(String url) throws IOException {
		this.url = new URL(url);
	}
	
	public RemoteHttpServer(URL url) {
		this.url = url;
	}
	
	private HttpURLConnection connectWithHttp() throws IOException {
		return (HttpURLConnection) url.openConnection();
	}
	
	public HttpConnection sendGetRequest() throws IOException {
		HttpURLConnection connection = connectWithHttp();
		connection.setRequestMethod("GET");
		return new HttpConnection(connection);
	}
}
