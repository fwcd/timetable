package fwcd.timetable.model.calendar.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskCrateModel implements Serializable {
	private static final long serialVersionUID = -1485835006395536825L;
	private List<TaskListModel> lists = new ArrayList<>();
	
	public List<TaskListModel> getLists() { return lists; }
	
	public int size() { return lists.size(); }
}
