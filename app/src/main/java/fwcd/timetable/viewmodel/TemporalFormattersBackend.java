package fwcd.timetable.viewmodel;

import java.time.format.DateTimeFormatter;

import fwcd.fructose.Observable;
import fwcd.fructose.ReadOnlyObservable;
import fwcd.timetable.viewmodel.settings.TimeTableAppSettings;

public class TemporalFormattersBackend implements TemporalFormatters {
	private final Observable<DateTimeFormatter> dateFormatter = new Observable<>(DateTimeFormatter.ISO_DATE);
	private final Observable<DateTimeFormatter> timeFormatter = new Observable<>(DateTimeFormatter.ISO_TIME);
	private final Observable<DateTimeFormatter> dateTimeFormatter = new Observable<>(DateTimeFormatter.ISO_DATE_TIME);
	private final Observable<DateTimeFormatter> yearMonthFormatter = new Observable<>(DateTimeFormatter.ofPattern("MM.yyyy"));
	private final Observable<String> rawDateFormat = new Observable<>("");
	private final Observable<String> rawDateTimeFormat = new Observable<>("");
	
	public TemporalFormattersBackend(ReadOnlyObservable<TimeTableAppSettings> settings) {
		settings.listenAndFire(it -> {
			rawDateFormat.set(it.getDateFormat());
			rawDateTimeFormat.set(it.getDateTimeFormat());
			
			dateFormatter.set(DateTimeFormatter.ofPattern(it.getDateFormat()));
			timeFormatter.set(DateTimeFormatter.ofPattern(it.getTimeFormat()));
			dateTimeFormatter.set(DateTimeFormatter.ofPattern(it.getDateTimeFormat()));
			yearMonthFormatter.set(DateTimeFormatter.ofPattern(it.getYearMonthFormat()));
		});
	}
	
	public void setDateFormatter(DateTimeFormatter dateFormatter) { this.dateFormatter.set(dateFormatter); }
	
	public void setTimeFormatter(DateTimeFormatter timeFormatter) { this.timeFormatter.set(timeFormatter); }
	
	public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) { this.dateTimeFormatter.set(dateTimeFormatter); }
	
	public void setYearMonthFormatter(DateTimeFormatter yearMonthFormatter) { this.yearMonthFormatter.set(yearMonthFormatter); }
	
	@Override
	public DateTimeFormatter getDateFormatter() { return dateFormatter.get(); }
	
	@Override
	public DateTimeFormatter getTimeFormatter() { return timeFormatter.get(); }
	
	@Override
	public DateTimeFormatter getDateTimeFormatter() { return dateTimeFormatter.get(); }
	
	@Override
	public String getRawDateFormat() { return rawDateFormat.get(); }
	
	@Override
	public String getRawDateTimeFormat() { return rawDateTimeFormat.get(); }
}
