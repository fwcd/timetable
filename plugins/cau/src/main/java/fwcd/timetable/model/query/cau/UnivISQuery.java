package fwcd.timetable.model.query.cau;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import fwcd.fructose.Observable;
import fwcd.fructose.Result;
import fwcd.timetable.model.query.Parameter;
import fwcd.timetable.model.query.Query;
import fwcd.timetable.model.utils.CompletableProgressFuture;
import fwcd.timetable.model.utils.HttpConnection;
import fwcd.timetable.model.utils.RemoteHttpServer;

public class UnivISQuery implements Query {
	private static final String BASE_URL = "http://univis.uni-kiel.de/prg";
	private final UnivISXmlParser parser = new UnivISXmlParser();
	private final List<Parameter> parameters;
	
	public UnivISQuery(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public CompletableProgressFuture<Result<UnivISQueryResult, IOException>> performAsync() {
		return CompletableProgressFuture.supplyAsync(this::perform);
	}
	
	private Result<UnivISQueryResult, IOException> perform(Observable<Double> progress) {
		try {
			String url = BASE_URL + toQueryString(parameters) + "&show=xml&noimports=1";
			RemoteHttpServer server = new RemoteHttpServer(new URL(url));
			
			System.out.println("Querying " + url); // TODO: Proper logging
			
			try (HttpConnection connection = server.sendGetRequest()) {
				return Result.of(parser.parse(connection.readString(progress)));
			}
		} catch (IOException e) {
			return Result.ofFailure(e);
		}
	}
	
	private String toQueryString(List<Parameter> parameters) throws IOException {
		StringBuilder str = new StringBuilder();
		boolean first = true;
		for (Parameter parameter : parameters) {
			if (first) {
				str.append('?');
				first = false;
			} else {
				str.append('&');
			}
			str.append(parameter.getKey()).append('=').append(URLEncoder.encode(parameter.getValue(), StandardCharsets.UTF_8.name()));
		}
		return str.toString();
	}
}
