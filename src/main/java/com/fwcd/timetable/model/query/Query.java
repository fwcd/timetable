package com.fwcd.timetable.model.query;

import java.util.List;

import com.fwcd.fructose.Result;

public interface Query {
	List<ParameterKey> getKeys();
	
	Result<QueryResult, Exception> perform(List<Parameter> parameters);
}
