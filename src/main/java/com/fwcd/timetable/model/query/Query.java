package com.fwcd.timetable.model.query;

import java.io.IOException;

import com.fwcd.fructose.Result;
import com.fwcd.timetable.model.utils.ProgressFuture;

public interface Query {
	ProgressFuture<? extends Result<? extends QueryResult, ? extends IOException>> performAsync();
}
