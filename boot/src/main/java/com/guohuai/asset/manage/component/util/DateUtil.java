package com.guohuai.asset.manage.component.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.guohuai.asset.manage.component.exception.AMPException;

public class DateUtil {

	public static String datePattern = "yyyy-MM-dd";
	public static String datetimePattern = "yyyy-MM-dd HH:mm:ss";
	public static String timePattern = "HH:mm:ss";

	public static boolean same(java.sql.Date param0, java.sql.Date param1) {
		Calendar c0 = Calendar.getInstance();
		c0.setTimeInMillis(param0.getTime());
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(param1.getTime());
		return c0.get(Calendar.YEAR) == c1.get(Calendar.YEAR) && c0.get(Calendar.MONTH) == c1.get(Calendar.MONTH) && c0.get(Calendar.DATE) == c1.get(Calendar.DATE);
	}

	public static boolean ge(java.sql.Date param0, java.sql.Date param1) {
		Calendar c0 = Calendar.getInstance();
		c0.setTimeInMillis(param0.getTime());
		long l0 = c0.get(Calendar.YEAR) * 10000 + c0.get(Calendar.MONTH) * 100 + c0.get(Calendar.DATE);

		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(param1.getTime());
		long l1 = c1.get(Calendar.YEAR) * 10000 + c1.get(Calendar.MONTH) * 100 + c1.get(Calendar.DATE);

		return l0 >= l1;
	}

	public static String formatDate(long timestamp) {
		return format(timestamp, datePattern);
	}

	public static String formatDatetime(long timestamp) {
		return format(timestamp, datetimePattern);
	}

	public static Date parseDate(String date, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			throw AMPException.getException(10002, date);
		}
	}

	public static String format(long timestamp, String pattern) {
		return new SimpleDateFormat(pattern).format(new Date(timestamp));
	}

	public static String format(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}
	
	public int getDaysBetweenTwoDate(Date sdate, Date edate) {
		long days = (edate.getTime() - sdate.getTime()) / (1000*3600*24);
		return Integer.parseInt(String.valueOf(days));
	}
	
	public static java.sql.Date formatUtilToSql(Date date) {
		String sdate = format(date, datePattern);
		return java.sql.Date.valueOf(sdate);
	}

	/**
	 * 格式化 sql date 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(java.sql.Date sqlDate) {
		Date date = new Date(sqlDate.getTime());
		return new SimpleDateFormat(datePattern).format(date);
	}

	/**
	 * 将字符串转换成默认格式（XXXX-XX-XX）的sql date
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static java.sql.Date parseToSqlDate(String date) {
		try {
			Date sdate = new SimpleDateFormat(datePattern).parse(date);
			return new java.sql.Date(sdate.getTime());
		} catch (ParseException e) {
			throw AMPException.getException(10002, date);
		}
	}
	
	/**
	 * 月份增加
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addMonth(Date date, int count) {
		Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, count);
        date = parseDate(format(calender.getTime(), datePattern), datePattern);
        
        return date;
	}
	
	/**
	 * 获取指定日期的前一天
	 * @param date
	 * @return
	 */
	public static Date lastDate(Date date) {
		Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DATE, -1);
        date = parseDate(format(calender.getTime(), datePattern), datePattern);
        
        return date;
	}
	
	/**
	 * 天数增加
	 * @param date
	 * @param count
	 * @return
	 */
	public static Date addDay(Date date, int count) {
		Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DATE, count);
        
        return calender.getTime();
	}
	
	public static int compare_sql_date(java.sql.Date src, java.sql.Date src1) {
		Date date = new Date(src1.getTime());
		return compare_date(src, date);
	}
	
	/**
	 * 比较日期大小，判断是否超越参照日期
	 * 
	 * @param src
	 * @param src
	 * @return boolean; true:DATE1>DATE2;
	 */
	public static int compare_date(java.sql.Date src, Date src1) {

		String date1 = convertDate2String(datePattern, src);
		String date2 = convertDate2String(datePattern, src1);
		DateFormat df = new SimpleDateFormat(datePattern);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 比较日期大小，判断是否小于或等于当前日期
	 * 
	 * @param src
	 * @return boolean; true:DATE1<=DATE2;
	 */
	public static boolean compare_current(java.sql.Date src) {

		String date1 = convertDate2String(datePattern, src);
		String date2 = convertDate2String(datePattern, getCurrDate());
		DateFormat df = new SimpleDateFormat(datePattern);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 比较日期大小，判断是否小于或等于当前日期
	 * 
	 * @param src
	 * @return boolean; true:DATE1<DATE2;
	 */
	public static boolean compare_current_(java.sql.Date src) {

		String date1 = convertDate2String(datePattern, src);
		String date2 = convertDate2String(datePattern, getCurrDate());
		DateFormat df = new SimpleDateFormat(datePattern);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() >= dt2.getTime()) {
				return false;
			} else {
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 比较两个日期是否相等
	 * 
	 * @param src
	 * @param src
	 * @return boolean; true:DATE1>DATE2;
	 */
	public static boolean equal_date(java.sql.Date src, Date src1) {
		if (null == src || null == src1) {
			return false;
		}

		String date1 = convertDate2String(datePattern, src);
		String date2 = convertDate2String(datePattern, src1);
		DateFormat df = new SimpleDateFormat(datePattern);
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() == dt2.getTime()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return true;
	}
	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}
	
	/**
	 * 获取当前月的天数
	 * @return
	 */
	public static int getDaysOfMonth(Date date){
		Calendar calender = Calendar.getInstance(Locale.CHINA);
		calender.setTime(date);
		int days = calender.getActualMaximum(Calendar.DATE);
		return days;
	}

	/**
	 * 获取两个时间的时间间隔
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getDaysBetween(Date sdate, Date edate) {
		Calendar beginDate = Calendar.getInstance();
		beginDate.setTime(sdate);
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(edate);
		if (beginDate.after(endDate)) {
			Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int days = endDate.get(Calendar.DAY_OF_YEAR)
				- beginDate.get(Calendar.DAY_OF_YEAR) + 1;
		int year = endDate.get(Calendar.YEAR);
		if (beginDate.get(Calendar.YEAR) != year) {
			beginDate = (Calendar) beginDate.clone();
			do {
				days += beginDate.getActualMaximum(Calendar.DAY_OF_YEAR);
				beginDate.add(Calendar.YEAR, 1);
			} while (beginDate.get(Calendar.YEAR) != year);
		}
		return days;
	}

	/**
	 * 获取当前日期 格式:XXXX-XX-XX
	 * @param date
	 * @return
	 */
	public static Date getCurrDate() {
		String sdate = format(System.currentTimeMillis(), datePattern);
		
		return parseDate(sdate, datePattern);
	}

	/**
	 * 获取当前日期 格式:XXXX-XX-XX
	 * @param date
	 * @return
	 */
	public static Date getCurrDate(Timestamp time) {
		String sdate = format(time.getTime(), datePattern);
		
		return parseDate(sdate, datePattern);
	}
	
	/**
	 * 获取系统当前SQL类型的Timestamp
	 * 
	 * @return 当前时间
	 */
	public static Timestamp getSqlCurrentDate() {
		return new Timestamp(Clock.DEFAULT.getCurrentTimeInMillis());
	}

	/**
	 * 获取当前日期的当前日历
	 * @param date
	 * @return
	 */
	public static int getDayFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		return c.get(Calendar.DATE);
	}
	
	/**
	 * 获取当前日期的年份
	 * @param date
	 * @return
	 */
	public static int getYearFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		return c.get(Calendar.YEAR);
	}
	
	/**
	 * 获取当前日期的月份
	 * @param date
	 * @return
	 */
	public static int getMonthFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH) + 1;
		
		return month;
	}
	
	/**
	 * 获取当前日期的月份-1
	 * @param date
	 * @return
	 */
	public static int getMonthDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		
		return month;
	}
	
	//日期转换格式化
	public static long longToDate(long time){

		try {
			SimpleDateFormat sf=new SimpleDateFormat(datePattern);
			String date=sf.format(new Date(time));
			return sf.parse(date).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0l;
	}
	
	
	public static int longToInt(String format,long time){

		try {
			SimpleDateFormat sf=new SimpleDateFormat(format);
			int date=Integer.valueOf(sf.format(new Date(time)));
			return date;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void main(String[] args) {
		Date date = addDay(getCurrDate(), 1);
		System.out.println(formatUtilToSql(date));
	}

}
