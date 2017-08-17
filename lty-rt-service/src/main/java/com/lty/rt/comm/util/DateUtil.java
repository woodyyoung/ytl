package com.lty.rt.comm.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期处理工具
 * 
 * @author qiq
 *
 */
public class DateUtil {

	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE = "yyyy-MM-dd";
	public static final String DATE_YEAR = "yyyy";
	public static final String DATE_MOTH = "yyyy-MM";
	public static final String DATE_TIME_PATTERN = "yyyyMMddHHmmss";
	public static final String DATE_MINUTE_PATTERN = "yyMMddHHmm";

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {
		return new Date();
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTime() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 时间转字符串--默认格式
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDateToString(Date date) {
		return getDateTime(DATE_TIME, date);
	}

	/**
	 * 时间转字符串--按指定格式
	 * 
	 * @param pattern
	 * @param date
	 * @return
	 */
	public static String convertDateToString(String pattern, Date date) {
		return getDateTime(pattern, date);
	}

	public static String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate == null) {
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 字符串转时间--按默认格式
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(final String strDate) throws ParseException {
		return convertStringToDate(DATE_TIME, strDate);
	}

	/**
	 * 字符串转时间--按指定格式
	 * 
	 * @param pattern
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String pattern, String strDate) throws ParseException {
		SimpleDateFormat df;
		Date date;
		df = new SimpleDateFormat(pattern);

		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			// log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}

	/**
	 * 获得日期与本周一相差的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getMondayPlus(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}

	/**
	 * 获取当前日期的上一天
	 * 
	 * @return
	 */
	public static Date getYesterday() {
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DATE, -1);
		return cd.getTime();
	}

	/**
	 * 如果date比当前时间早，返回true；反之返回false
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isBypastTime(Date date) {
		Calendar cd1 = Calendar.getInstance();
		Calendar cd2 = Calendar.getInstance();
		cd2.setTime(date);
		return cd1.after(cd2);
	}

	/**
	 * 获取两个日期之间所有的日期列表（格式yyyy-MM-dd)
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getBetweenDates(String date1, String date2) throws ParseException {
		if (StringUtils.isBlank(date1) || StringUtils.isBlank(date2))
			return null;
		List<String> list = new ArrayList<String>();
		if (!date1.equals(date2)) {
			String tmp;
			if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
				tmp = date1;
				date1 = date2;
				date2 = tmp;
			}
			tmp = convertDateToString(DATE, convertStringToDate(DATE, date1));// 格式化
			while (tmp.compareTo(date2) <= 0) {
				list.add(tmp);
				tmp = addDate(DATE, tmp, "D", 1);
			}
		} else {
			list.add(date1);
		}
		return list;
	}

	/**
	 * 获取两个月份之间所有的月份列表（格式yyyy-MM)
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getBetweenMonths(String date1, String date2) throws ParseException {
		if (StringUtils.isBlank(date1) || StringUtils.isBlank(date2))
			return null;
		List<String> list = new ArrayList<String>();
		if (!date1.equals(date2)) {
			String tmp;
			if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
				tmp = date1;
				date1 = date2;
				date2 = tmp;
			}
			tmp = convertDateToString(DATE_MOTH, convertStringToDate(DATE_MOTH, date1));// 格式化
			while (tmp.compareTo(date2) <= 0) {
				list.add(tmp);
				tmp = addDate(DATE_MOTH, tmp, "M", 1);
			}
		} else {
			list.add(date1);
		}
		return list;
	}

	/**
	 * 比较两个日期的大小
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(Date date1, Date date2) {
		if (date1.getTime() > date2.getTime()) {
			return 1;
		} else if (date1.getTime() < date2.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 两个日期相差的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getGapOfTwoDate(Date date1, Date date2) {
		long day = (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) > 0 ? (date1.getTime() - date2.getTime()) / (24 * 60 * 60 * 1000) : (date2.getTime() - date1.getTime())
				/ (24 * 60 * 60 * 1000);
		return Integer.parseInt(String.valueOf(day));

	}

	/**
	 * 日期增加或者减少秒，分钟，天，月，年
	 * 
	 * @param srcDate
	 * @param type
	 *            类型 Y M D HH MM SS 年月日时分秒
	 * @param offset
	 *            （整数）
	 * @return 增加或者减少之后的日期
	 */
	public static java.util.Date addDate(Date srcDate, String type, int offset) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(srcDate);
		if (type.equals("SS")) {
			gc.add(GregorianCalendar.SECOND, offset);
		} else if (type.equals("MM")) {
			gc.add(GregorianCalendar.MINUTE, offset);
		} else if (type.equals("HH")) {
			gc.add(GregorianCalendar.HOUR, offset);
		} else if (type.equals("D")) {
			gc.add(GregorianCalendar.DATE, offset);
		} else if (type.equals("M")) {
			gc.add(GregorianCalendar.MONTH, offset);
		} else if (type.equals("Y")) {
			gc.add(GregorianCalendar.YEAR, offset);
		}
		return gc.getTime();
	}

	public static String addDate(String srcDate, String type, int offset) throws ParseException {
		return convertDateToString(addDate(convertStringToDate(srcDate), type, offset));
	}

	public static String addDate(String pattern, String srcDate, String type, int offset) throws ParseException {
		return convertDateToString(pattern, addDate(convertStringToDate(pattern, srcDate), type, offset));
	}

	/**
	 * 根据日期计算年龄
	 * 
	 * @param birthday
	 * @return
	 */
	public static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}
		return age;
	}

	/**
	 * 判断时间戳是否符合格式
	 * 
	 * @param date时间戳
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static boolean isDatePattern(String date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		boolean result = false;
		try {
			if (StringUtils.isNotBlank(date) && date.trim().length() == pattern.trim().length()) {
				sdf.setLenient(false);
				System.out.println(sdf.parse(date));
				result = true;
			}
		} catch (ParseException e) {
			result = false;
		}
		return result;
	}
}
