package com.jdao.base;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private Logger logger;
	private boolean isLog;
	private Class<?> clazz;
	Object o = new Object();

	private Log(boolean on, Class<?> clazz) {
		isLog = on;
		this.clazz = clazz;
		createLog();
	}

	private void createLog() {
		synchronized (o) {
			if (logger == null && isLog) {
				logger = Logger.getLogger(clazz == null ? "" : clazz.getName());
				Handler handler = new ConsoleHandler();
				logger.setUseParentHandlers(false);
				logger.setLevel(Level.ALL);
				handler.setLevel(Level.ALL);
				logger.addHandler(handler);
			}
		}
	}

	public static Log newInstance() {
		return new Log(false, null);
	}

	public static Log newInstance(Class<?> clazz) {
		return new Log(false, clazz);
	}

	public static Log newInstance(boolean on, Class<?> clazz) {
		return new Log(on, clazz);
	}

	public void isLog(boolean isLog, Class<?> clazz) {
		this.isLog = isLog;
		this.clazz = clazz;
		createLog();
	}

	public void log(String log) {
		if (isLog) {
			logger.log(Level.FINE, log);
		}
	}
}
