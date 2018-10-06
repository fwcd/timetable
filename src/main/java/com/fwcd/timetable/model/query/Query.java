package com.fwcd.timetable.model.query;

import java.io.IOException;

import com.fwcd.fructose.Result;
import com.fwcd.timetable.model.utils.ProgressFuture;

public interface Query {
	QueryResult perform() throws IOException;
	
	ProgressFuture<Result<QueryResult, IOException>> performAsync();
}
