package com.jdao.action;

import com.jdao.util.CreateDaoUtil;

public class CreateDaoTest {

	// CREATE TABLE `hstest` (`id` int(10) DEFAULT NULL, `value` varchar(50) DEFAULT '', `rowname` varchar(50) DEFAULT '') ENGINE=InnoDB
	// 创建 Hstest.java
	public static void createHstest() throws Exception {
		// 包名：com.jdao.action
		// 表名：hstest
		// 生产Hstest.java路径：javaPath
		// 数据库连接：DataSourceTest.getByDruid().getConnection()
		String javaPath = System.getProperty("user.dir") + "/test/com/jdao/action";
		CreateDaoUtil.createFile("com.jdao.action", "hstest", javaPath, DataSourceTest.getByDruid().getConnection(), "utf-8");
	}
}
