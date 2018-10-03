package com.fwcd.timetable.model.query;

import com.fwcd.fructose.Result;

public interface Query {
	Result<? extends QueryResult, Exception> perform();
}
