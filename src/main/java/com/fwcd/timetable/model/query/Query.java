package com.fwcd.timetable.model.query;

import java.io.IOException;

import com.fwcd.fructose.Result;
import com.fwcd.timetable.model.utils.CompletableProgressFuture;

public interface Query {
	CompletableProgressFuture<? extends Result<? extends QueryResult, ? extends IOException>> performAsync();
}
