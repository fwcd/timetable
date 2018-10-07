package com.fwcd.timetable.model.query;

import java.util.List;
import java.util.stream.Stream;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.AppointmentModel;

public interface QueryOutputNode {
	String getName();
	
	Option<AppointmentModel> asAppointment();
	
	List<? extends QueryOutputNode> getChilds();
	
	default Stream<AppointmentModel> streamAppointments() {
		return Stream.concat(
			asAppointment().stream(),
			getChilds().stream().flatMap(it -> it.streamAppointments())
		);
	}
}
