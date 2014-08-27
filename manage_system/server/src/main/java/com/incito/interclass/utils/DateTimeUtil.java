/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.incito.interclass.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

// TODO: Auto-generated Javadoc
/**
 * 描述：日期处理类.
 * 
 * @author zhaoqp
 * @date：2013-1-18 上午8:48:25
 * @version v1.0
 */
public class DateTimeUtil {

	/** The tag. */
	private static String TAG = "AbDateUtil";

	/** 时间日期格式化到年月日时分秒. */
	public static String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";

	/** 时间日期格式化到年月日. */
	public static String dateFormatYMD = "yyyy-MM-dd";

	/** 时间日期格式化到年月. */
	public static String dateFormatYM = "yyyy-MM";

	/** 时间日期格式化到年月日时分. */
	public static String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

	/** 时间日期格式化到月日. */
	public static String dateFormatMD = "MM/dd";

	/** 时分秒. */
	public static String dateFormatHMS = "HH:mm:ss";

	/** 时分. */
	public static String dateFormatHM = "HH:mm";
	private static long day = 7;

	/**
	 * 获得当前时间戳
	 * 
	 * @return
	 */
	public static String getTimestamp() {
		String unixTimeGMT;
		try {
			long unixTime = System.currentTimeMillis();
			unixTimeGMT = unixTime + "";
		} catch (Exception e) {
			// TODO: handle exception
			unixTimeGMT = "";
		}
		return unixTimeGMT;

	}

	/**
	 * 获得当前时间戳
	 * 
	 * @return
	 */
	public static long getIntTimestamp() {
		long unixTimeGMT = 0;
		try {
			unixTimeGMT = System.currentTimeMillis();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return unixTimeGMT;

	}

	/**
	 * 返回时间戳间隔
	 * 
	 * @return
	 */
	public static boolean compareTimestamp(long currentTimestap,
			long oldTimestap) {
		Boolean isExceed = false;
		if (gapTimestamp(currentTimestap, oldTimestap) > 86400 * day) {
			isExceed = true;
		}
		return isExceed;
	}

	public static long gapTimestamp(long currentTimestap, long oldTimestap) {
		return (currentTimestap - oldTimestap);
	}

	/**
	 * 对时间戳格式进行格式化，保证时间戳长度为13位
	 * 
	 * @param timestamp
	 *            时间戳
	 * @return 返回为13位的时间戳
	 */
	public static String formatTimestamp(String timestamp) {
		if (timestamp == null || "".equals(timestamp)) {
			return "";
		}
		String tempTimeStamp = timestamp + "00000000000000";
		StringBuffer stringBuffer = new StringBuffer(tempTimeStamp);
		return tempTimeStamp = stringBuffer.substring(0, 13);
	}

	/**
	 * 根据 timestamp 生成各类时间状态串
	 * 
	 * @param timestamp
	 *            距1970 00:00:00 GMT的秒数
	 * @param format
	 *            格式
	 * @return 时间状态串(如：刚刚5分钟前)
	 */
	public static String getTimeState(String timestamp, String format) {
		if (timestamp == null || "".equals(timestamp)) {
			return "";
		}

		try {
			timestamp = formatTimestamp(timestamp);
			long _timestamp = Long.parseLong(timestamp);
			if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
				return "刚刚";
			} else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
				return ((System.currentTimeMillis() - _timestamp) / 1000 / 60)
						+ "分钟前";
			} else {
				Calendar now = Calendar.getInstance();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(_timestamp);
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
					SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
					return sdf.format(c.getTime());
				}
				if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
						&& c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
						&& c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
					return sdf.format(c.getTime());
				} else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
					SimpleDateFormat sdf = null;
					if (format != null && !format.equalsIgnoreCase("")) {
						sdf = new SimpleDateFormat(format);

					} else {
						sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
					}

					return sdf.format(c.getTime());
				} else {
					SimpleDateFormat sdf = null;
					if (format != null && !format.equalsIgnoreCase("")) {
						sdf = new SimpleDateFormat(format);

					} else {
						sdf = new SimpleDateFormat("yyyy年M月d日 HH:mm:ss");
					}
					return sdf.format(c.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 描述：String类型的日期时间转化为Date类型.
	 * 
	 * @param strDate
	 *            String形式的日期时间
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return Date Date类型日期时间
	 */
	public static Date getDateByFormat(String strDate, String format) {
		if (strDate == null || format == null)
			return null;
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = mSimpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	/**
	 * 描述：获取偏移之后的Date.
	 * 
	 * @param date
	 *            日期时间
	 * @param calendarField
	 *            Calendar属性，对应offset的值，
	 *            如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset
	 *            偏移(值大于0,表示+,值小于0,表示－)
	 * @return Date 偏移之后的日期时间
	 */
	public Date getDateByOffset(Date date, int calendarField, int offset) {
		Calendar c = new GregorianCalendar();
		try {
			c.setTime(date);
			c.add(calendarField, offset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c.getTime();
	}

	/**
	 * 描述：获取指定日期时间的字符串(可偏移).
	 * 
	 * @param strDate
	 *            String形式的日期时间
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @param calendarField
	 *            Calendar属性，对应offset的值，
	 *            如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset
	 *            偏移(值大于0,表示+,值小于0,表示－)
	 * @return String String类型的日期时间
	 */
	public static String getStringByOffset(String strDate, String format,
			int calendarField, int offset) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(mSimpleDateFormat.parse(strDate));
			c.add(calendarField, offset);
			mDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mDateTime;
	}

	/**
	 * 描述：Date类型转化为String类型(可偏移).
	 * 
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @param calendarField
	 *            the calendar field
	 * @param offset
	 *            the offset
	 * @return String String类型日期时间
	 */
	public static String getStringByOffset(Date date, String format,
			int calendarField, int offset) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(date);
			c.add(calendarField, offset);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：Date类型转化为String类型.
	 * 
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getStringByFormat(Date date, String format) {
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
		String strDate = null;
		try {
			strDate = mSimpleDateFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取指定日期时间的字符串,用于导出想要的格式.
	 * 
	 * @param strDate
	 *            String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
	 * @param format
	 *            输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 转换后的String类型的日期时间
	 */
	public static String getStringByFormat(String strDate, String format) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			c.setTime(mSimpleDateFormat.parse(strDate));
			SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
			mDateTime = mSimpleDateFormat2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;
	}

	/**
	 * 描述：获取milliseconds表示的日期时间的字符串.
	 * 
	 * @param milliseconds
	 *            the milliseconds
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 日期时间字符串
	 */
	public static String getStringByFormat(long milliseconds, String format) {
		String thisDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			thisDateTime = mSimpleDateFormat.format(milliseconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return thisDateTime;
	}

	/**
	 * 描述：获取表示当前日期时间的字符串.
	 * 
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String String类型的当前日期时间
	 */
	public static String getCurrentDate(String format) {
		String curDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			curDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curDateTime;

	}

	/**
	 * 描述：获取表示当前日期时间的字符串(可偏移).
	 * 
	 * @param format
	 *            格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @param calendarField
	 *            Calendar属性，对应offset的值，
	 *            如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
	 * @param offset
	 *            偏移(值大于0,表示+,值小于0,表示－)
	 * @return String String类型的日期时间
	 */
	public static String getCurrentDateByOffset(String format,
			int calendarField, int offset) {
		String mDateTime = null;
		try {
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			Calendar c = new GregorianCalendar();
			c.add(calendarField, offset);
			mDateTime = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;

	}

	/**
	 * 描述：计算两个日期所差的天数.
	 * 
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的天数
	 */
	public static int getOffectDay(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		// 先判断是否同年
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0) {
			maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 + maxDays;
		} else if (y1 - y2 < 0) {
			maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 - maxDays;
		} else {
			day = d1 - d2;
		}
		return day;
	}

	/**
	 * 描述：计算两个日期所差的天数.
	 * 
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的天数
	 */
	public static int getOffectDay(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		// 先判断是否同年
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0) {
			maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 + maxDays;
		} else if (y1 - y2 < 0) {
			maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 - maxDays;
		} else {
			day = d1 - d2;
		}
		return day;
	}

	/**
	 * 描述：计算两个日期所差的小时数.
	 * 
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的小时数
	 */
	public static int getOffectHour(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
		int h = 0;
		int day = getOffectDay(date1, date2);
		h = h1 - h2 + day * 24;
		return h;
	}

	/**
	 * 描述：计算两个日期所差的分钟数.
	 * 
	 * @param date1
	 *            第一个时间的毫秒表示
	 * @param date2
	 *            第二个时间的毫秒表示
	 * @return int 所差的分钟数
	 */
	public static int getOffectMinutes(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int m1 = calendar1.get(Calendar.MINUTE);
		int m2 = calendar2.get(Calendar.MINUTE);
		int h = getOffectHour(date1, date2);
		int m = 0;
		m = m1 - m2 + h * 60;
		return m;
	}

	/**
	 * 描述：获取本周一.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfWeek(String format) {
		return getDayOfWeek(format, Calendar.MONDAY);
	}

	/**
	 * 描述：获取本周日.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfWeek(String format) {
		return getDayOfWeek(format, Calendar.SUNDAY);
	}

	/**
	 * 描述：获取本周的某一天.
	 * 
	 * @param format
	 *            the format
	 * @param calendarField
	 *            the calendar field
	 * @return String String类型日期时间
	 */
	private static String getDayOfWeek(String format, int calendarField) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			int week = c.get(Calendar.DAY_OF_WEEK);
			if (week == calendarField) {
				strDate = mSimpleDateFormat.format(c.getTime());
			} else {
				int offectDay = calendarField - week;
				if (calendarField == Calendar.SUNDAY) {
					offectDay = 7 - Math.abs(offectDay);
				}
				c.add(Calendar.DATE, offectDay);
				strDate = mSimpleDateFormat.format(c.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取本月第一天.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getFirstDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			// 当前月的第一天
			c.set(GregorianCalendar.DAY_OF_MONTH, 1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;

	}

	/**
	 * 描述：获取本月最后一天.
	 * 
	 * @param format
	 *            the format
	 * @return String String类型日期时间
	 */
	public static String getLastDayOfMonth(String format) {
		String strDate = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
			// 当前月的最后一天
			c.set(Calendar.DATE, 1);
			c.roll(Calendar.DATE, -1);
			strDate = mSimpleDateFormat.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * 描述：获取表示当前日期的0点时间毫秒数.
	 * 
	 * @return the first time of day
	 */
	public static long getFirstTimeOfDay() {
		Date date = null;
		try {
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate + " 00:00:00", dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 描述：获取表示当前日期24点时间毫秒数.
	 * 
	 * @return the last time of day
	 */
	public static long getLastTimeOfDay() {
		Date date = null;
		try {
			String currentDate = getCurrentDate(dateFormatYMD);
			date = getDateByFormat(currentDate + " 24:00:00", dateFormatYMDHMS);
			return date.getTime();
		} catch (Exception e) {
		}
		return -1;
	}

	/**
	 * 描述：判断是否是闰年()
	 * <p>
	 * (year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
	 * 
	 * @param year
	 *            年代（如2012）
	 * @return boolean 是否为闰年
	 */
	public static boolean isLeapYear(int year) {
		if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 描述：根据时间返回格式化后的时间的描述. 小于1小时显示多少分钟前 大于1小时显示今天＋实际日期，大于今天全部显示实际时间
	 * 
	 * @param strDate
	 *            the str date
	 * @param outFormat
	 *            the out format
	 * @return the string
	 */
	public static String formatDateStr2Desc(String strDate, String outFormat) {

		DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c2.setTime(df.parse(strDate));
			c1.setTime(new Date());
			int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
			if (d == 0) {
				int h = getOffectHour(c1.getTimeInMillis(),
						c2.getTimeInMillis());
				if (h > 0) {
					return "今天" + getStringByFormat(strDate, dateFormatHM);
					// return h + "小时前";
				} else if (h < 0) {
					// return Math.abs(h) + "小时后";
				} else if (h == 0) {
					int m = getOffectMinutes(c1.getTimeInMillis(),
							c2.getTimeInMillis());
					if (m > 0) {
						return m + "分钟前";
					} else if (m < 0) {
						// return Math.abs(m) + "分钟后";
					} else {
						return "刚刚";
					}
				}

			} else if (d > 0) {
				if (d == 1) {
					// return "昨天"+getStringByFormat(strDate,outFormat);
				} else if (d == 2) {
					// return "前天"+getStringByFormat(strDate,outFormat);
				}
			} else if (d < 0) {
				if (d == -1) {
					// return "明天"+getStringByFormat(strDate,outFormat);
				} else if (d == -2) {
					// return "后天"+getStringByFormat(strDate,outFormat);
				} else {
					// return Math.abs(d) +
					// "天后"+getStringByFormat(strDate,outFormat);
				}
			}

			String out = getStringByFormat(strDate, outFormat);
			if (out != null && out.length() > 0) {
				return out;
			}
		} catch (Exception e) {
		}

		return strDate;
	}

	/**
	 * 取指定日期为星期几.
	 * 
	 * @param strDate
	 *            指定日期
	 * @param inFormat
	 *            指定日期格式
	 * @return String 星期几
	 */
	public static String getWeekDay(String strDate, String inFormat) {
		String week = "星期日";
		Calendar calendar = new GregorianCalendar();
		DateFormat df = new SimpleDateFormat(inFormat);
		try {
			calendar.setTime(df.parse(strDate));
		} catch (Exception e) {
			return "错误";
		}
		int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		switch (intTemp) {
		case 0:
			week = "星期日";
			break;
		case 1:
			week = "星期一";
			break;
		case 2:
			week = "星期二";
			break;
		case 3:
			week = "星期三";
			break;
		case 4:
			week = "星期四";
			break;
		case 5:
			week = "星期五";
			break;
		case 6:
			week = "星期六";
			break;
		}
		return week;
	}

	/**
	 * 取指定日期的星期值.
	 * 
	 * @param strDate
	 *            指定日期
	 * @param inFormat
	 *            指定日期格式
	 * @return int 星期几
	 */
	public static int getWeekNumber(String strDate, String inFormat) {
		Calendar cal = new GregorianCalendar();

		DateFormat df = new SimpleDateFormat(inFormat);
		try {
			cal.setTime(df.parse(strDate));
		} catch (Exception e) {
			return -1;
		}
		int weeknumber = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return weeknumber;
	}

	/**
	 * 根据年月日获取年龄
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return
	 */
	public static int getAge(int year, int month, int day) {
		int age = 0;
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.YEAR) == year) {
			if (calendar.get(Calendar.MONTH) == month) {
				if (calendar.get(Calendar.DAY_OF_MONTH) >= day) {
					age = calendar.get(Calendar.YEAR) - year + 1;
				} else {
					age = calendar.get(Calendar.YEAR) - year;
				}
			} else if (calendar.get(Calendar.MONTH) > month) {
				age = calendar.get(Calendar.YEAR) - year + 1;
			} else {
				age = calendar.get(Calendar.YEAR) - year;
			}
		} else {
			age = calendar.get(Calendar.YEAR) - year;
		}
		if (age < 0) {
			return 0;
		}
		return age;
	}

	/**
	 * 根据月日获取星座
	 * 
	 * @param month
	 *            月
	 * @param day
	 *            日
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
			return "水瓶座";
		} else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
			return "双鱼座";
		} else if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
			return "白羊座";
		} else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
			return "金牛座";
		} else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
			return "双子座";
		} else if ((month == 6 && day >= 22) || (month == 7 && day <= 22)) {
			return "巨蟹座";
		} else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
			return "狮子座";
		} else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
			return "处女座";
		} else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
			return "天秤座";
		} else if ((month == 10 && day >= 24) || (month == 11 && day <= 22)) {
			return "天蝎座";
		} else if ((month == 11 && day >= 23) || (month == 12 && day <= 21)) {
			return "射手座";
		} else if ((((month != 12) || (day < 22)))
				&& (((month != 1) || (day > 19)))) {
			return "魔蝎座";
		}
		return "";
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		System.out.println(formatDateStr2Desc("2012-3-2 12:2:20",
				"MM月dd日  HH:mm"));
	}

}