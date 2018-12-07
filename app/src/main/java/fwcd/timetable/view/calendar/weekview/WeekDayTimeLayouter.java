package fwcd.timetable.view.calendar.weekview;

import java.time.Duration;
import java.time.LocalTime;

public class WeekDayTimeLayouter {
	private final double hourHeight = 40;
	
	public double toPixelY(LocalTime time) {
		return (time.getHour() * hourHeight)
			+ (time.getMinute() * (hourHeight / 60))
			+ (time.getSecond() * (hourHeight / 3600));
	}
	
	public LocalTime toTime(double pixelY) {
		double secondHeight = hourHeight / 3600;
		return LocalTime.ofSecondOfDay((long) (pixelY / secondHeight));
	}
	
	public double toPixelHeight(Duration duration) {
		return duration.getSeconds() * (hourHeight / 3600);
	}
}
