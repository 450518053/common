package com.tcc.common.id;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**                    
 * @Filename IDCreater.java
 *
 * @Description id生成器
 * 					现只生成了不重复的时间戳，其他待拓展
 *
 * @author tan 2016年1月19日
 *
 * @email 450518053@qq.com
 * 
 */
public class IDCreater {
	
	/**
	 * 时间字符串长度
	 */
	public static final int			TIME_FLIED_LEN			= 20;
															
	/**
	 * id长度
	 */
	public static final int			ID_LEN					= TIME_FLIED_LEN;
															
	private static AtomicLong		lastTime				= new AtomicLong(0);
															
	/**
	 * 时间缓存
	 */
	private static volatile long	lastTimeCache			= 0l;
	/**
	 * 时间字符串缓存
	 */
	private static volatile String	lastTimeStrCache		= null;
	/**
	 * 每毫秒内生成id最大数
	 */
	private static final int		COUNT_IN_MILL_SECOND	= 1000;
															
	/**
	 * 17位的时间格式
	 */
	private static final String		TIME_FORMAT				= "yyyyMMddHHmmssSSS";
															
	/**
	 * 生成长度为20的业务id
	 * 		暂时只根据时间戳生成
	 * @return
	 */
	public static String newID() {
		StringBuilder idBuilder = new StringBuilder(ID_LEN);
		appendTime(idBuilder);
		return idBuilder.toString();
	}
	
	/**
	 * 添加20位时间
	 * @param idBuilder
	 */
	private static void appendTime(final StringBuilder idBuilder) {
		Date now = new Date();
		//1.获取时间标识
		long time = createTime(now.getTime());
		String timeStr;
		//2.长度17位的当前时间
		if (lastTimeCache == now.getTime()) {
			timeStr = lastTimeStrCache;
		} else {
			timeStr = new SimpleDateFormat(TIME_FORMAT)
				.format(new Date(time / COUNT_IN_MILL_SECOND));
			lastTimeStrCache = timeStr;
			lastTimeCache = now.getTime();
		}
		idBuilder.append(timeStr);
		//3.添加毫秒后的3位精确数字,保证在并发下不会生成重复的
		String dt = Long.toString(time);
		idBuilder.append(dt.substring(dt.length() - 3));
	}
	
	/**
	 * 根据当前时间(毫秒)获取到唯一的时间标识
	 * @param currentTimeMillis 当前毫秒时间
	 * @return 时间标识
	 */
	private static long createTime(final long currentTimeMillis) {
		long timeMillis = currentTimeMillis * COUNT_IN_MILL_SECOND;
		while (true) {
			long last = lastTime.get();
			if (timeMillis > last) {
				if (lastTime.compareAndSet(last, timeMillis)) {
					break;
				}
			} else {
				if (lastTime.compareAndSet(last, last + 1)) {
					timeMillis = last + 1;
					break;
				}
			}
		}
		return timeMillis;
	}
	
}
