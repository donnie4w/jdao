package com.jdao.base;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoHandlerFactory;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-3-7
 * @verion 1.0.3 创建Dao对象工厂类
 */
public class DaoFactory {
	// private static JdaoHandler jdao;
	private static FieldFilter field;
	private static DataSource defaultDataSource;
	private static Map<Object, DataSource> jdaoMap = new ConcurrentHashMap<>();

	// public static Map<Class<?>, JdaoHandler> jdaoMap = new ConcurrentHashMap<Class<?>, JdaoHandler>();
	// public static Map<String, JdaoHandler> jdaoPackageMap = new ConcurrentHashMap<String, JdaoHandler>();
	public static Map<Class<?>, FieldFilter> fieldMap = new ConcurrentHashMap<Class<?>, FieldFilter>();
	// static Map<Class<?>,DataSource> classMap = new ConcurrentHashMap<>();
	// static Map<String,DataSource> packageMap = new ConcurrentHashMap<>();

	private DaoFactory() {
	}

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

	// public static void setJdaoHandler(JdaoHandler jdaoHandler) {
	// jdao = jdaoHandler;
	// }

	public static void SetDefaultDataSource(DataSource ds) {
		defaultDataSource = ds;
	}

	public static void setFieldFilter(FieldFilter fieldFilter) {
		field = fieldFilter;
	}

	// /**
	// * @param <T>
	// * @param clazz
	// * @return
	// */
	// public static <T extends Table<?>> T createDao(Class<T> clazz) {
	// T t = null;
	// try {
	// t = clazz.newInstance();
	// t.setJdaoHandler(jdaoMap.containsKey(clazz) ? jdaoMap.get(clazz) : jdao);
	// } catch (Exception e) {
	// }
	// return t;
	// }

	// /**
	// * @param <T>
	// * @param clazz
	// * @param tableName
	// * @return
	// */
	// public static <T extends Table<?>> T createDao(Class<T> clazz, String tableName) {
	// T t = null;
	// try {
	// t = clazz.getConstructor(String.class).newInstance(tableName);
	// t.setJdaoHandler(jdaoMap.containsKey(clazz) ? jdaoMap.get(clazz) : jdao);
	// } catch (Exception e) {
	// }
	// return t;
	// }

	// public static JdaoHandler getJaoHandler() {
	// return jdao;
	// }

	/**
	 * 注册dao类的jdaohandler属性，若已经注册，则返回false.
	 * 
	 * @param clazz
	 * @param jdao
	 * @return
	 */
	// public static boolean dataSourceRegister(Class<?> clazz, JdaoHandler jdao) {
	// if (!jdaoMap.containsKey(clazz)) {
	// jdaoMap.put(clazz, jdao);
	// return true;
	// } else {
	// return false;
	// }
	// }

	/**
	 * 强制注册dao类的jdaohandler属性，若已经注册，则更新。
	 * 
	 * @param clazz
	 * @param jdao
	 * @return
	 */
	// public static void dataSourceForceRegister(Class<?> clazz, JdaoHandler jdao) {
	// jdaoMap.put(clazz, jdao);
	// }

	// public static boolean dataSourceRegister4package(String packageName, JdaoHandler jdao) throws IOException, ClassNotFoundException {
	// jdaoPackageMap.put(packageName, jdao);
	// return true;
	// }
	//
	// public static void register4Field(Class<?> clazz, FieldFilter fieldFilter) {
	// if (!fieldMap.containsKey(clazz)) {
	// fieldMap.put(clazz, fieldFilter);
	// }
	// }

	public static FieldFilter getField() {
		return field;
	}

	public static void setField(FieldFilter field) {
		DaoFactory.field = field;
	}

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
