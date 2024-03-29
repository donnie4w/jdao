/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private Logger logger;
	private boolean isLog;
	// Object o = new Object();
	// private static Handler handler = null;
	// private static Map<Handler, Byte> mapHandler = new ConcurrentHashMap<Handler,
	// Byte>();

	// static {
	// handler = new ConsoleHandler();
	// handler.setLevel(Level.ALL);
	// }

	private Log(boolean on, Class<?> clazz) {
		isLog = on;
		logger = Logger.getLogger(clazz == null ? "" : clazz.getName());
		logger.setLevel(Level.INFO);
		// createLog();
	}

	// private void createLog() {
	// synchronized (o) {
	// if (logger == null && isLog) {
	// logger = Logger.getLogger(clazz == null ? "" : clazz.getName());
	// logger.setUseParentHandlers(false);
	// logger.setLevel(Level.INFO);
	// synchronized (mapHandler.getClass()) {
	// if (!mapHandler.containsKey(handler)) {
	// mapHandler.put(handler, (byte) 0);
	// logger.addHandler(handler);
	// }
	// }
	// }
	// }
	// }

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
	}

	public void log(String log) {
		if (isLog) {
			logger.log(Level.INFO, log);
		}
	}

	public static void main(String[] args) {
		Log log = Log.newInstance(true, Log.class);
		log.log("111111111");

		log = Log.newInstance(false, Table.class);
		log.isLog(true, Table.class);
		log.log("222222222");
	}
}
