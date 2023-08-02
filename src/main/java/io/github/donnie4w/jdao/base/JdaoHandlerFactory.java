/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class JdaoHandlerFactory {
	// private final static Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<String, DataSource>();
	//
	// public static void setDataSource(String dbname, DataSource dataSource) {
	// dataSourceMap.put(dbname, dataSource);
	// }
	//
	// public static void delDataSource(String dbname) {
	// dataSourceMap.remove(dbname);
	// }
	//
	// public static JdaoHandler getJdaoHandler(String dbname) {
	// return new JdaoHandlerDefaultImpl(dataSourceMap.get(dbname));
	// }

	public static JdaoHandler getJdaoHandler(DataSource dataSource) {
		try {
			return new JdaoHandlerDefaultImpl(dataSource);
		} catch (SQLException e) {
			throw new JRuntimeException(e);
		}
	}
}
