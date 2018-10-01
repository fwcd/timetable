package com.fwcd.timetable.view.calendar;

import java.util.Collections;
import java.util.List;

import com.fwcd.fructose.Option;
import com.fwcd.timetable.model.calendar.AppointmentModel;

public class OverlappingEventNode {
	private final Option<AppointmentModel> value;
	private final List<OverlappingEventNode> childs;
	
	public OverlappingEventNode(AppointmentModel value) {
		this.value = Option.of(value);
		childs = Collections.emptyList();
	}
	
	public OverlappingEventNode(List<OverlappingEventNode> childs) {
		this.childs = childs;
		value = Option.empty();
	}
	
	public boolean isLeaf() { return childs.isEmpty(); } 
	
	public Option<AppointmentModel> getValue() { return value; }
	
	public List<OverlappingEventNode> getChilds() { return childs; }
}
