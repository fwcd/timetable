package fwcd.timetable.model.calendar;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import fwcd.fructose.draw.DrawColor;

/**
 * Metadata information about a calendar. Since calendar
 * entries such as appointments each hold an ID reference
 * to their calendar, the entries are *not* stored in this class.
 */
public class CalendarModel implements Serializable {
	private static final long serialVersionUID = 831554590083407654L;
	private String name;
	private DrawColor color;
	
	public CalendarModel() { this(""); }
	
	public CalendarModel(String name) { this(name, randomColor()); }
	
	public CalendarModel(String name, DrawColor color) {
		this.name = name;
		this.color = color;
	}
	
	public DrawColor getColor() { return color; }
	
	public String getName() { return name; }
	
	private static DrawColor randomColor() {
		Random random = ThreadLocalRandom.current();
		return new DrawColor(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
	
	public CalendarModel withName(String newName) { return new CalendarModel(newName, color); }
	
	public CalendarModel withColor(DrawColor newColor) { return new CalendarModel(name, newColor); }
	
	@Override
	public String toString() { return name; }
}
