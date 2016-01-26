package com.tcc.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**                    
 * @Filename DateUtils.java
 *
 * @Description 日期工具类
 *
 * @author tan 2016年2月3日
 *
 * @email 450518053@qq.com
 * 
 */
public class DateUtils {
	
	/**
	* 完整时间 yyyy-MM-dd HH:mm:ss
	*/
	public static final String	simple				= "yyyy-MM-dd HH:mm:ss";
													
	/**
	 * 年月日 yyyy-MM-dd
	 */
	public static final String	dtSimple			= "yyyy-MM-dd";
	
	/**
	 * 年月日(无下划线) yyyyMMdd
	 */
	public static final String	dtShort				= "yyyyMMdd";
													
	/**
	 * 年月日时分秒(无下划线) yyyyMMddHHmmss
	 */
	public static final String	dtLong				= "yyyyMMddHHmmss";
													
	public static final long	MILL_SECOND_IN_DAY	= 1000 * 60 * 60 * 24;
													
	/**
	 * 获取格式
	 * @param format
	 * @return
	 */
	public static final DateFormat getFormat(String format) {
		return new SimpleDateFormat(format);
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static final String simpleFormat(Date date) {
		if (date == null) {
			return "";
		}
		return getFormat(simple).format(date);
	}
	
	/**
	 * yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static final String dtSimpleFormat(Date date) {
		if (date == null) {
			return "";
		}
		return getFormat(dtSimple).format(date);
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss 日期格式转换为日期
	 * @param strDate
	 * @return
	 */
	public static final Date simpleParse(String strDate) {
		if (strDate == null) {
			return null;
		}
		try {
			return getFormat(simple).parse(strDate);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * yyyy-MM-dd 日期格式转换为日期
	 *
	 * @param strDate
	 * @return
	 */
	public static final Date dtSimpleParse(String strDate) {
		if (strDate == null) {
			return null;
		}
		try {
			return getFormat(dtSimple).parse(strDate);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 获取上一天
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDay(String strDate) throws ParseException {
		Date tempDate = DateUtils.dtSimpleParse(strDate);
		Calendar cad = Calendar.getInstance();
		cad.setTime(tempDate);
		cad.add(Calendar.DATE, -1);
		return DateUtils.dtSimpleFormat(cad.getTime());
	}
	
	/**
	 * 获取下一天 返回 dtSimple 格式字符
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static String getNextDay(Date date) throws ParseException {
		Calendar cad = Calendar.getInstance();
		cad.setTime(date);
		cad.add(Calendar.DATE, 1);
		return DateUtils.dtSimpleFormat(cad.getTime());
	}
	
	/**
	 * 判断时间是否在指定时间段中
	 * @param cur   比较时间
	 * @param start 起始时间(不包括)
	 * @param end   结束时间(不包括)
	 * @return 结果
	 */
	public static final boolean isIn(final Date cur, final Date start, final Date end) {
		return cur.after(start) && cur.before(end);
	}
	
	/**
	 * 校验start与end相差的天数，是否满足end-start lessEqual than days
	 * @param start
	 * @param end
	 * @param days
	 * @return
	 */
	public static boolean checkDays(Date start, Date end, int days) {
		int g = countDays(start, end);
		return g <= days;
	}
	
	/**
	 * 返回日期相差天数，向下取整数
	 * @param dateStart 一般前者小于后者dateEnd
	 * @param dateEnd
	 * @return
	 */
	public static int countDays(Date dateStart, Date dateEnd) {
		if ((dateStart == null) || (dateEnd == null)) {
			return -1;
		}
		return (int) ((dateEnd.getTime() - dateStart.getTime()) / MILL_SECOND_IN_DAY);
	}
	
	/**
	 * 取得相差的天数
	 * @param startDate 格式为 2008-08-01
	 * @param endDate   格式为 2008-08-01
	 * @return
	 * @throws ParseException 
	 */
	public static int countDays(String startDate, String endDate) throws ParseException {
		Date tempDate1 = null;
		Date tempDate2 = null;
		tempDate1 = DateUtils.dtSimpleParse(startDate);
		tempDate2 = DateUtils.dtSimpleParse(endDate);
		return (int) ((tempDate2.getTime() - tempDate1.getTime()) / MILL_SECOND_IN_DAY);
	}
	
	/**
	 * 获取date那天的开始时间，00:00:00
	 * @param date
	 * @return
	 */
	public static Date getStartTimeOfTheDate(Date date) {
		if (date == null) {
			return null;
			
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 获取date那天的结束时间，23:59:
	 * @param date
	 * @return
	 */
	public static Date getEndTimeOfTheDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
}
