package com.finassist.helpers;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	public static Date getStartOfDay(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getEndOfDay(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static Date getYesterday(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	public static Date getFirstDayOfWeek(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		return getStartOfDay(calendar.getTime());
	}

	public static Date getFirstDayOfMonth(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartOfDay(calendar.getTime());
	}

	public static Date getLastDayOfMonth(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getStartOfDay(calendar.getTime());
	}

	public static Date getFirstDayOfPreviousMonth(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartOfDay(calendar.getTime());
	}

	public static Date getLastDayOfPreviousMonth(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getEndOfDay(calendar.getTime());
	}

	public static Date getFirstDayOfEarlierMonth(Date d, int x) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, -x);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return getStartOfDay(calendar.getTime());
	}

	public static Date getFirstDayOfYear(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		return getStartOfDay(calendar.getTime());
	}
}
