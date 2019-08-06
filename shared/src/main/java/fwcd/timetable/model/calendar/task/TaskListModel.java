package fwcd.timetable.model.calendar.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskListModel implements Serializable {
	private static final long serialVersionUID = 3478629580505983160L;
	private String name;
	private List<TaskModel> tasks = new ArrayList<>();
	
	protected TaskListModel() {}
	
	public TaskListModel(String name) {
		this.name = name;
	}
	
	public String getName() { return name; }
	
	public List<TaskModel> getTasks() { return tasks; }
}
