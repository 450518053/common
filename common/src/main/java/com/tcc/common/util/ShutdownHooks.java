package com.tcc.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.tcc.common.log.Logger;
import com.tcc.common.log.LoggerFactory;

/**                    
 * @Filename ShutdownHooks.java
 *
 * @Description 系统关闭钩子辅助工具类
 *
 * @author tan 2016年2月19日
 *
 * @email 450518053@qq.com
 * 
 */
public class ShutdownHooks {
	
	private static final Logger		logger	= LoggerFactory.getLogger(ShutdownHooks.class);
											
	public static List<TaskWrapper>	tasks	= new ArrayList<TaskWrapper>();
											
	private static AtomicInteger	index	= new AtomicInteger();
											
	/**
	 * 添加关闭钩子
	 * @param runnable 钩子内容
	 * @param hookName 钩子名称
	 */
	public static void addShutdownHook(Runnable runnable, String hookName) {
		if (runnable != null) {
			TaskWrapper taskwrapper = new TaskWrapper(runnable, hookName);
			Thread thread = new Thread(taskwrapper, "ShutdownHook" + index.incrementAndGet());
			taskwrapper.setShutdownhook(thread);
			tasks.add(taskwrapper);
			Runtime.getRuntime().addShutdownHook(thread);
		}
	}
	
	/**
	 * 执行所有shutdown hook,建议在容器关闭时执行.
	 * <p/>
	 * 避免容器关闭后,classloader关闭,容器加载类失败.
	 */
	public static void shutdownAll() {
		for (TaskWrapper task : tasks) {
			task.run();
			//清除shutdownhook
			try {
				Runtime.getRuntime().removeShutdownHook(task.getShutdownhook());
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		//如果是容器reload,需要清理资源,在shutdown时不需要清理资源
		tasks.clear();
	}
	
	private static class TaskWrapper implements Runnable {
		
		private Runnable	runnable;
							
		private String		hookName;
							
		private boolean		isRunned	= false;
										
		private Thread		shutdownhook;
							
		/**
		 * 构建一个<code>ShutdownHooks.java</code>
		 * @param runnable
		 * @param hookName
		 */
		public TaskWrapper(Runnable runnable, String hookName) {
			this.runnable = runnable;
			this.hookName = hookName;
		}
		
		public void run() {
			synchronized (this) {
				if (!isRunned) {
					logger.info("[SHUTDOWNHOOK-{}]开始执行", hookName);
					isRunned = true;
					try {
						this.runnable.run();
						logger.info("[SHUTDOWNHOOK-{}]执行结束", hookName);
					} catch (Exception e) {
						logger.error("[SHUTDOWNHOOK-{}]执行失败", hookName, e);
					}
				}
			}
		}
		
		public Thread getShutdownhook() {
			return shutdownhook;
		}
		
		public void setShutdownhook(Thread shutdownhook) {
			this.shutdownhook = shutdownhook;
		}
	}
	
}
