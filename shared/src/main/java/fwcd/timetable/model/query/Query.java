package fwcd.timetable.model.query;

import java.io.IOException;

import fwcd.fructose.Result;
import fwcd.timetable.model.utils.CompletableProgressFuture;

public interface Query {
	CompletableProgressFuture<? extends Result<? extends QueryResult, ? extends IOException>> performAsync();
}
