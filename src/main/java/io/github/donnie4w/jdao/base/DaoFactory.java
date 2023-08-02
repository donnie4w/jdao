/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

/**
 * Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * date 2013-3-7
 * verion 1.0.3 创建Dao对象工厂类
 */
public class DaoFactory {
//	private static FieldFilter field;
	private static DataSource defaultDataSource;
	private static Map<Object, DataSource> jdaoMap = new ConcurrentHashMap<>();
//	public static Map<Class<?>, FieldFilter> fieldMap = new ConcurrentHashMap<Class<?>, FieldFilter>();

	private DaoFactory() {}

	public static void setDataSource(Object o, DataSource ds) {
		jdaoMap.put(o, ds);
	}

	public static void setDataSource(Class<?> clz, DataSource ds) {
		jdaoMap.put(clz, ds);
	}

	public static void setDataSource(String packageName, DataSource ds) {
		jdaoMap.put(packageName, ds);
	}

	public static JdaoHandler getDefaultHandler() {
		return JdaoHandlerFactory.getJdaoHandler(defaultDataSource);
	}

	public static JdaoHandler getJdaoHandler(Object o) {
		if (jdaoMap.containsKey(o)) {
			return JdaoHandlerFactory.getJdaoHandler(jdaoMap.get(o));
		}
		return getDefaultHandler();
	}

	public static JdaoHandler getJdaoHandler(Class<?> clz, String packageName) {
		if (jdaoMap.containsKey(clz)) {
			return JdaoHandlerFactory.getJdaoHandler(jdaoMap.get(clz));
		} else if (packageName != null && jdaoMap.containsKey(packageName)) {
			return JdaoHandlerFactory.getJdaoHandler(jdaoMap.get(packageName));
		}
		return getDefaultHandler();
	}

	public static void registDefaultDataSource(DataSource ds) {
		defaultDataSource = ds;
	}

//	public static void setFieldFilter(FieldFilter fieldFilter) {
//		field = fieldFilter;
//	}

//	public static FieldFilter getField() {
//		return field;
//	}

//	public static void setField(FieldFilter field) {
//		DaoFactory.field = field;
//	}

	public static Cache getCache() {
		return CacheBeen.newInstance();
	}

	public static void main(String[] args) {
		try {
			// DaoFactory.dataSourceRegister4package("junit.framework", null);
			System.out.println(DaoFactory.class.getName() + " | " + DaoFactory.class.getPackage().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
