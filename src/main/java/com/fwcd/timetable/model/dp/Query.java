package com.fwcd.timetable.model.dp;

import java.util.List;

public interface Query {
	List<ParameterKey> getKeys();
	
	QueryResult perform(List<Parameter> parameters);
}
