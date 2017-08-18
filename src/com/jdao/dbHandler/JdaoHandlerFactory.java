package com.jdao.dbHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.jdao.dbHandlerImpl.JdaoHandlerImplSingleTon2;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */

public class JdaoHandlerFactory {
	private final static Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<String, DataSource>();

	public static void setDataSource(String dbname, DataSource dataSource) {
		dataSourceMap.put(dbname, dataSource);
	}

	public static void delDataSource(String dbname) {
		dataSourceMap.remove(dbname);
	}

	public static JdaoHandler getJdaoHandler(String dbname) {
		return new JdaoHandlerImplSingleTon2(dataSourceMap.get(dbname));
	}

	public static JdaoHandler getJdaoHandler(DataSource dataSource) {
		return new JdaoHandlerImplSingleTon2(dataSource);
	}

}
