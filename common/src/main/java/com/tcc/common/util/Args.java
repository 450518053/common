package com.tcc.common.util;

import java.util.Collection;

/**                    
 * @Filename Args.java
 *
 * @Description 
 *
 * @author tan 2016年2月4日
 *
 * @email 450518053@qq.com
 * 
 */
public class Args {
	
	public static <T> T notNull(final T argument, final String name) {
		if (argument == null) {
			throw new IllegalArgumentException(name + " may not be null");
		}
		return argument;
	}
	
	public static <T extends CharSequence> T notEmpty(final T argument, final String name) {
		if (StringUtils.isEmpty(notNull(argument, name))) {
			throw new IllegalArgumentException(name + " may not be empty");
		}
		return argument;
	}
	
	public static <T extends CharSequence> T notBlank(final T argument, final String name) {
		if (StringUtils.isBlank(notNull(argument, name))) {
			throw new IllegalArgumentException(name + " may not be blank");
		}
		return argument;
	}
	
	public static <T extends Collection<?>> T notEmpty(final T argument, final String name) {
		if (notNull(argument, name).size() == 0) {
			throw new IllegalArgumentException(name + " may not be empty");
		}
		return argument;
	}
	
}
