package com.fwcd.timetable.model.query;

import com.fwcd.fructose.Option;

public interface QueryResult {
	Option<String> getRaw();
	
	QueryOutputNode getOutputTree();
}
