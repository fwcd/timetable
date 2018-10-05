package com.fwcd.timetable.model.calendar;

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
import com.fwcd.fructose.time.LocalDateInterval;
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
public class ParsedRecurrence {
	private static final Pattern RECURRENCE_PATTERN = Pattern.compile("^(d|w|m)(\\d+)(?: (.+))?$");
	private static final Pattern MONTH_SUB_MODE_PATTERN = Pattern.compile("^d(\\d+)$");
	private static final Pattern WEEK_SUB_MODE_PATTERN = Pattern.compile("^w(\\d+) (\\d+)$");
	
	private final Observable<String> raw = new Observable<>("");
	private final Observable<Option<Recurrence>> parsed = new Observable<>(Option.empty());
	
	public ParsedRecurrence(ReadOnlyObservable<LocalDateInterval> dateInterval) {
		ObservableUtils.dualListen(dateInterval, raw, (dates, str) -> {
			parsed.set(parse(dates.getStart(), str));
		});
	}
	
	private Option<Recurrence> parse(LocalDate start, String str) {
		Matcher matcher = RECURRENCE_PATTERN.matcher(str);
		if (matcher.find()) {
			String modechar = matcher.group(1);
			int distance = Integer.parseInt(matcher.group(2));
			Option<String> args = Option.ofNullable(matcher.group(matcher.group(3)));
			switch (modechar) {
				case "d": return Option.of(new DailyRecurrence(start, distance));
				case "w": return args.flatMap(it -> parseWeeklyRecurrence(start, it, distance));
				case "m": return args.flatMap(it -> parseMonthlyRecurrence(start, it, distance));
				case "y": return Option.of(new YearlyRecurrence(start, distance));
				default: break;
			}
		}
		return Option.empty();
	}
	
	private Option<Recurrence> parseWeeklyRecurrence(LocalDate start, String args, int weeksBetweenRepeats) {
		try {
			Set<DayOfWeek> weekDays = Arrays.stream(args.split(","))
				.map(String::trim)
				.map(Integer::parseInt)
				.map(DayOfWeek::of)
				.collect(Collectors.toSet());
			return Option.of(new WeeklyRecurrence(start, weekDays, weeksBetweenRepeats));
		} catch (NumberFormatException e) {
			return Option.empty();
		}
	}
	
	private Option<Recurrence> parseMonthlyRecurrence(LocalDate start, String args, int monthsBetweenRepeats) {
		// TODO: Parse custom month filters instead of allowing all months
		Set<Month> months = Arrays.stream(Month.values()).collect(Collectors.toSet());
		
		Matcher weekSubMatcher = WEEK_SUB_MODE_PATTERN.matcher(args);
		if (weekSubMatcher.find()) {
			int weekOfMonth = Integer.parseInt(weekSubMatcher.group(1));
			DayOfWeek dayOfWeek = DayOfWeek.of(Integer.parseInt(weekSubMatcher.group(2)));
			return Option.of(new MonthlyWeekRecurrence(start, months, weekOfMonth, dayOfWeek, monthsBetweenRepeats));
		}
		
		Matcher monthSubMatcher = MONTH_SUB_MODE_PATTERN.matcher(args);
		if (monthSubMatcher.find()) {
			int dayOfMonth = Integer.parseInt(monthSubMatcher.group(1));
			return Option.of(new MonthlyDayRecurrence(start, months, dayOfMonth, monthsBetweenRepeats));
		}
		
		return Option.empty();
	}
	
	public Observable<String> getRaw() { return raw; }
	
	public ReadOnlyObservable<Option<Recurrence>> getParsed() { return parsed; }
}
