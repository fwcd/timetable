package com.fwcd.timetable.model.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RemoteHttpServer {
	private final URL url;
	
	public RemoteHttpServer(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			throw new HttpException(e);
		}
	}
	
	public RemoteHttpServer(URL url) {
		this.url = url;
	}
	
	private HttpURLConnection connectWithHttp() {
		try {
			return (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	public HttpConnection sendGetRequest() {
		HttpURLConnection connection = connectWithHttp();
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			throw new HttpException(e);
		}
		return new HttpConnection(connection);
	}
}
