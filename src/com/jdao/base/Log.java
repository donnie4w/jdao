package com.jdao.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private Logger logger;
	private boolean isLog;
	private Class<?> clazz;
	Object o = new Object();
	private static Handler handler = null;
	private static Map<Handler, Byte> mapHandler = new ConcurrentHashMap<Handler, Byte>();

	static {
		handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
	}

	private Log(boolean on, Class<?> clazz) {
		isLog = on;
		this.clazz = clazz;
		createLog();
	}

	private void createLog() {
		synchronized (o) {
			if (logger == null && isLog) {
				logger = Logger.getLogger(clazz == null ? "" : clazz.getName());
				logger.setUseParentHandlers(false);
				logger.setLevel(Level.ALL);
				synchronized (mapHandler.getClass()) {
					if (!mapHandler.containsKey(handler)) {
						mapHandler.put(handler, (byte) 0);
						logger.addHandler(handler);
					}
				}
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
