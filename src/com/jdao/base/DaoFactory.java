package com.jdao.base;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.jdao.dbHandler.JdaoHandler;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-3-7
 * @verion 1.0.3 创建Dao对象工厂类
 */
public class DaoFactory {
	private static JdaoHandler jdao;
	private static FieldFilter field;
	public static Map<Class<?>, JdaoHandler> jdaoMap = new ConcurrentHashMap<Class<?>, JdaoHandler>();

	public static Map<Class<?>, FieldFilter> fieldMap = new ConcurrentHashMap<Class<?>, FieldFilter>();

	private DaoFactory() {
	}

	public static void setJdaoHandler(JdaoHandler jdaoHandler) {
		jdao = jdaoHandler;
	}

	public static void setFieldFilter(FieldFilter fieldFilter) {
		field = fieldFilter;
	}

	/**
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T extends Table<?>> T createDao(Class<T> clazz) {
		T t = null;
		try {
			t = clazz.newInstance();
			t.setJdaoHandler(jdaoMap.containsKey(clazz) ? jdaoMap.get(clazz) : jdao);
		} catch (Exception e) {
		}
		return t;
	}

	/**
	 * @param <T>
	 * @param clazz
	 * @param tableName
	 * @return
	 */
	public static <T extends Table<?>> T createDao(Class<T> clazz, String tableName) {
		T t = null;
		try {
			t = clazz.getConstructor(String.class).newInstance(tableName);
			t.setJdaoHandler(jdaoMap.containsKey(clazz) ? jdaoMap.get(clazz) : jdao);
		} catch (Exception e) {
		}
		return t;
	}

	public static JdaoHandler getJaoHandler() {
		return jdao;
	}

	/**
	 * 注册dao类的jdaohandler属性，若已经注册，则返回false.
	 * 
	 * @param clazz
	 * @param jdao
	 * @return
	 */
	public static boolean dataSourceRegister(Class<?> clazz, JdaoHandler jdao) {
		if (!jdaoMap.containsKey(clazz)) {
			jdaoMap.put(clazz, jdao);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 强制注册dao类的jdaohandler属性，若已经注册，则更新。
	 * 
	 * @param clazz
	 * @param jdao
	 * @return
	 */
	public static void dataSourceForceRegister(Class<?> clazz, JdaoHandler jdao) {
		jdaoMap.put(clazz, jdao);
	}

	/**
	 * 根据包名注册数据源,指定包名的类将批量注册指定的jdao类
	 * 
	 * @param packageName
	 * @param jdao
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static boolean dataSourceRegister4package(String packageName, JdaoHandler jdao) throws IOException, ClassNotFoundException {
		String packagePath = packageName.replace('.', '/');
		URL url = Thread.currentThread().getContextClassLoader().getResource(packagePath);
		String protocol = url.getProtocol();
		if ("file".equals(protocol)) {
			File file = new File(url.getFile());
			File[] filelist = file.listFiles();
			for (File f : filelist) {
				if (f.isFile() && f.getName().endsWith(".class") && !f.getName().contains("$")) {
					String className = packageName + "." + f.getName().substring(0, f.getName().lastIndexOf("."));
					Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
					dataSourceForceRegister(clazz, jdao);
				}
			}
		} else if ("jar".equals(protocol)) {
			String[] jar = url.getPath().split("!");
			String jarFilePath = jar[0].substring(jar[0].indexOf("/"));
			@SuppressWarnings("resource")
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> jarEntrys = jarFile.entries();
			while (jarEntrys.hasMoreElements()) {
				JarEntry jarEntry = jarEntrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class") && !entryName.contains("$") && !entryName.replaceFirst(packagePath + "/", "").contains("/")
						&& entryName.startsWith(packagePath)) {
					Class<?> clazz = Thread.currentThread().getContextClassLoader()
							.loadClass(entryName.replaceAll("/", ".").replaceAll(".class", ""));
					dataSourceForceRegister(clazz, jdao);
				}
			}
		} else {
			return false;
		}
		return true;
	}

	public static void register4Field(Class<?> clazz, FieldFilter fieldFilter) {
		if (!fieldMap.containsKey(clazz)) {
			fieldMap.put(clazz, fieldFilter);
		}
	}

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
			DaoFactory.dataSourceRegister4package("junit.framework", null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
