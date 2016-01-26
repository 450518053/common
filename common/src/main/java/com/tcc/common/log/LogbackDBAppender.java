package com.tcc.common.log;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBAppenderBase;

/**                    
 * @Filename LogbackDBAppender.java
 *
 * @Description 
 * 
 * @see http://logback.qos.ch/manual/appenders.html 待学习
 *
 * @author tan 2016年2月1日
 *
 * @email 450518053@qq.com
 * 
 */
public class LogbackDBAppender extends DBAppenderBase<ILoggingEvent> {

	/**
	 * @return
	 * @see ch.qos.logback.core.db.DBAppenderBase#getGeneratedKeysMethod()
	 */
	@Override
	protected Method getGeneratedKeysMethod() {
		return null;
	}

	/**
	 * @return
	 * @see ch.qos.logback.core.db.DBAppenderBase#getInsertSQL()
	 */
	@Override
	protected String getInsertSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param eventObject
	 * @param connection
	 * @param statement
	 * @throws Throwable
	 * @see ch.qos.logback.core.db.DBAppenderBase#subAppend(java.lang.Object, java.sql.Connection, java.sql.PreparedStatement)
	 */
	@Override
	protected void subAppend(	ILoggingEvent eventObject, Connection connection,
								PreparedStatement statement) throws Throwable {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param eventObject
	 * @param connection
	 * @param eventId
	 * @throws Throwable
	 * @see ch.qos.logback.core.db.DBAppenderBase#secondarySubAppend(java.lang.Object, java.sql.Connection, long)
	 */
	@Override
	protected void secondarySubAppend(	ILoggingEvent eventObject, Connection connection,
										long eventId) throws Throwable {
		// TODO Auto-generated method stub
		
	}

}
