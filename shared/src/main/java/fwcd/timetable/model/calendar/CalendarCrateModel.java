package fwcd.timetable.model.calendar;

import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CalendarCrateModel implements Serializable {
	private static final long serialVersionUID = -1914115703122727566L;
	private static final Gson GSON = CalendarSerializationUtils.newGson();
	private static final Type CALENDARS_TYPE = new TypeToken<List<CalendarModel>>() {}.getType();
	
	private final List<CalendarModel> calendars = new ArrayList<>();
	
	public List<CalendarModel> getCalendars() { return calendars; }
	
	public void saveAsJsonTo(Appendable writer) {
		GSON.toJson(calendars, CALENDARS_TYPE, writer);
	}
	
	public void loadFromJsonIn(Reader reader) {
		GSON.fromJson(reader, CALENDARS_TYPE);
	}
	
	public void createNewCalendars() {
		calendars.clear();
		calendars.add(new CalendarModel("Calendar"));
	}
}
