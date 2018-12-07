package fwcd.timetable.model.query;

import fwcd.fructose.Option;

public interface QueryResult {
	Option<String> getRaw();
	
	QueryOutputNode getOutputTree();
}
