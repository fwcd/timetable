package fwcd.timetable.model.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import fwcd.fructose.Observable;
import fwcd.fructose.Option;

public class HttpConnection implements AutoCloseable {
	private final HttpURLConnection urlConnection;
	private final InputStream inputStream;
	
	public HttpConnection(HttpURLConnection urlConnection) throws IOException {
		this.urlConnection = urlConnection;
		urlConnection.connect();
		inputStream = urlConnection.getInputStream();
	}
	
	public InputStream getInputStream() { return inputStream; }
	
	public String readString() throws IOException { return readString(Option.empty()); }
	
	public String readString(Observable<Double> progress) throws IOException { return readString(Option.of(progress)); }
	
	private String readString(Option<Observable<Double>> progress) throws IOException {
		StringBuilder str = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		long contentLength = urlConnection.getContentLengthLong();
		
		String line = reader.readLine();
		while (line != null) {
			if (progress.isPresent()) {
				progress.unwrap().set((double) str.length() / (double) contentLength);
			}
			str.append(line);
			line = reader.readLine();
		}
		
		return str.toString();
	}
	
	public Stream<String> readLines(Charset charset) throws IOException {
		Stream.Builder<String> streamBuilder = Stream.builder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
		
		String line = reader.readLine();
		while (line != null) {
			streamBuilder.accept(line);
			line = reader.readLine();
		}
		
		return streamBuilder.build();
	}
	
	@Override
	public void close() throws IOException {
		inputStream.close();
		urlConnection.disconnect();
	}
}
