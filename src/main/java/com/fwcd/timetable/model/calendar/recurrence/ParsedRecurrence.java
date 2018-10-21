package com.fwcd.timetable.model.calendar.recurrence;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fwcd.fructose.Observable;
import com.fwcd.fructose.Option;
import com.fwcd.fructose.ReadOnlyObservable;
import com.fwcd.fructose.time.LocalDateTimeInterval;
import com.fwcd.timetable.model.utils.ObservableUtils;

/**
 * A recurrence that is parsed using the following scheme
 * (represented using a grammar similar to EBNF):
 * 
 * <pre>
 * recurrence = modechar, number (distance between repetitions), ' ', args
 * modechar = 'd' (daily) | 'w' (weekly) | 'm' (monthly) | 'y' (yearly)
 * digit = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 0
 * number = digit, { digit }
 * args =
 *       ? case weekly  ? number, { ',', number } (days of the week)
 *       ? case monthly ? monthsubmode | weeksubmode
 * monthsubmode = 'd', number (day of the month)
 * weeksubmode = 'w', number (week of the month), ' ', number (day of the week)
 * </pre>
 */
public class ParsedRecurrence implements Serializable {
	private static final long serialVersionUID = -1134267101451984444L;
	private static final Pattern RECURRENCE_PATTERN = Pattern.compile("^(d|w|m)(\\d+)(?: (.+))?$");
	private static final Pattern MONTH_SUB_MODE_PATTERN = Pattern.compile("^d(\\d+)$");
	private static final Pattern WEEK_SUB_MODE_PATTERN = Pattern.compile("^w(\\d+) (\\d+)$");
	
	private final Observable<String> raw = new Observable<>("");
	private final Observable<Option<Recurrence>> parsed = new Observable<>(Option.empty());
	
	public ParsedRecurrence(ReadOnlyObservable<LocalDateTimeInterval> dateTimeInterval, ReadOnlyObservable<Option<LocalDate>> recurrenceEnd, Set<LocalDate> excludes) {
		ObservableUtils.triListen(dateTimeInterval, raw, recurrenceEnd, (dates, str, recEnd) -> {
			parsed.set(parse(str, dates.getStart().toLocalDate(), recEnd, excludes));
		});
	}
	
	private Option<Recurrence> parse(String str, LocalDate start, Option<LocalDate> end, Set<LocalDate> excludes) {
		Matcher matcher = RECURRENCE_PATTERN.matcher(str);
		Option<Recurrence> result = Option.empty();
		
		if (matcher.find()) {
			String modechar = matcher.group(1);
			int distance = Integer.parseInt(matcher.group(2));
			Option<String> args = Option.ofNullable(matcher.group(3));
			switch (modechar) {
				case "d": result = Option.of(new DailyRecurrence(start, end, distance)); break;
				case "w": result = args.flatMap(it -> parseWeeklyRecurrence(it, start, end, distance)); break;
				case "m": result = args.flatMap(it -> parseMonthlyRecurrence(it, start, end, distance)); break;
				case "y": result = Option.of(new YearlyRecurrence(start, end, distance)); break;
				default: break;
			}
		}
		
		if (excludes.size() > 0) {
			return result.map(it -> new ExcludingRecurrence(it, excludes));
		} else {
			return result;
		}
	}
	
	private Option<Recurrence> parseWeeklyRecurrence(String args, LocalDate start, Option<LocalDate> end, int weeksBetweenRepeats) {
		try {
			Set<DayOfWeek> weekDays = Arrays.stream(args.split(","))
				.map(String::trim)
				.map(Integer::parseInt)
				.map(DayOfWeek::of)
				.collect(Collectors.toSet());
			if (weekDays.isEmpty()) {
				weekDays = Arrays.stream(DayOfWeek.values()).collect(Collectors.toSet());
			}
			return Option.of(new WeeklyRecurrence(start, end, weekDays, weeksBetweenRepeats));
		} catch (NumberFormatException e) {
			return Option.empty();
		}
	}
	
	private Option<Recurrence> parseMonthlyRecurrence(String args, LocalDate start, Option<LocalDate> end, int monthsBetweenRepeats) {
		// TODO: Parse custom month filters instead of allowing all months
		Set<Month> months = Arrays.stream(Month.values()).collect(Collectors.toSet());
		
		Matcher weekSubMatcher = WEEK_SUB_MODE_PATTERN.matcher(args);
		if (weekSubMatcher.find()) {
			int weekOfMonth = Integer.parseInt(weekSubMatcher.group(1));
			DayOfWeek dayOfWeek = DayOfWeek.of(Integer.parseInt(weekSubMatcher.group(2)));
			return Option.of(new MonthlyWeekRecurrence(start, end, months, weekOfMonth, dayOfWeek, monthsBetweenRepeats));
		}
		
		Matcher monthSubMatcher = MONTH_SUB_MODE_PATTERN.matcher(args);
		if (monthSubMatcher.find()) {
			int dayOfMonth = Integer.parseInt(monthSubMatcher.group(1));
			return Option.of(new MonthlyDayRecurrence(start, end, months, dayOfMonth, monthsBetweenRepeats));
		}
		
		return Option.empty();
	}
	
	public Observable<String> getRaw() { return raw; }
	
	public ReadOnlyObservable<Option<Recurrence>> getParsed() { return parsed; }
}
