package fwcd.timetable.model.query.cau;

import fwcd.fructose.Option;
import fwcd.timetable.model.query.QueryOutputNode;
import fwcd.timetable.model.query.QueryResult;

public class UnivISQueryResult implements QueryResult {
	private final Option<String> raw;
	private final QueryOutputNode outputTree;
	
	public UnivISQueryResult(String raw, String calendarName) {
		this.raw = Option.of(raw);
		outputTree = new UnivISQueryOutputNode.Empty("Kein Ergebnis");
	}
	
	public UnivISQueryResult(QueryOutputNode outputNode) {
		this.outputTree = outputNode;
		raw = Option.empty();
	}
	
	@Override
	public Option<String> getRaw() { return raw; }
	
	@Override
	public QueryOutputNode getOutputTree() { return outputTree; }
}
