package fwcd.timetable.model.utils;

public final class ParseUtils {
	private ParseUtils() {}
	
	public static boolean isNumeric(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
