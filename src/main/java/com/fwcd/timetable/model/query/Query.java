package com.fwcd.timetable.model.query;

import java.io.IOException;

public interface Query {
	QueryResult perform() throws IOException;
}
