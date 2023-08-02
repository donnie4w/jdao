/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

public class Jdao {
	static DataSource defaultDataSource;
	static Map<Class<?>, DataSource> classMap = new ConcurrentHashMap<>();
	static Map<String, DataSource> packageMap = new ConcurrentHashMap<>();

	public static void setDefaultDataSource(DataSource ds) {
		defaultDataSource = ds;
	}

	public static void setDataSource(Class<?> clz, DataSource ds) {
		classMap.put(clz, ds);
	}

	public static void setDataSource(String packageName, DataSource ds) {
		packageMap.put(packageName, ds);
	}

	public static JdaoHandler getDefaultJdaoHandler() throws SQLException {
		return JdaoHandlerFactory.getJdaoHandler(defaultDataSource);
	}

	public static JdaoHandler getJdaoHandler(Class<?> clz) throws SQLException {
		if (classMap.containsKey(clz)) {
			return JdaoHandlerFactory.getJdaoHandler(classMap.get(clz));
		}
		return null;
	}

	public static JdaoHandler getJdaoHandler(String packageName) throws SQLException {
		if (packageMap.containsKey(packageName)) {
			return JdaoHandlerFactory.getJdaoHandler(packageMap.get(packageName));
		}
		return null;
	}
}
