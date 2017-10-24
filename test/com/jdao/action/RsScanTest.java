package com.jdao.action;

import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.jdao.base.DaoFactory;
import com.jdao.base.DBUtils;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoHandlerFactory;
import com.jdao.util.CreateDaoUtil;

public class RsScanTest {

	public static DataSource getDataSource() throws Exception {
		Properties p = new Properties();
		p.load(ActionTest1_1_2.class.getClassLoader().getResourceAsStream("com/jdao/action/druid.properties"));
		return DruidDataSourceFactory.createDataSource(p);
	}

	public static void createHstest() throws Exception {
		CreateDaoUtil.createFile("com.jdao.action", "hstest", System.getProperty("user.dir") + "\\test\\com\\jdao\\action", getDataSource().getConnection(), "utf-8");
	}

	static {
		try {
			JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());
			DaoFactory.setJdaoHandler(jdaohandler);
			// 可以对不同DBUtils子类 设置不同的数据源
			DaoFactory.dataSourceForceRegister(RsTest.class, jdaohandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testInsert() throws Exception {
		RsTest rt = new RsTest();
		System.out.println(rt.execute("insert into hstest(`value`,`rowname`)values(?,?),(?,?) ", "wu1", "66", "wu2", "77"));
	}

	/**
	 * 针对分页封装的方法
	 * 
	 * @throws Exception
	 */
	public static void testSelectListPage() throws Exception {
		RsTest rt = new RsTest();
		// 分页查询方法
		rt.selectListPage(0, 20, "select * from hstest");
		System.out.println(rt.rsList().size());
		// selectListPage 会返回 totalcount
		List<RsTest> list = rt.rsList();
		for (RsTest r : list) {
			System.out.println(r.getString("value"));
		}
	}

	/**
	 * 单行返回
	 * 
	 * @throws Exception
	 */
	public static void testSelect() throws Exception {
		RsTest rt = new RsTest();
		rt.select("select * from hstest where id=?", 1);
		System.out.println(rt.getString("value"));
		rt.select("select * from hstest where id=?", 2);
		System.out.println(rt.getString("value"));
	}

	/**
	 * 多行返回
	 * 
	 * @throws Exception
	 */
	public static void testSelectList() throws Exception {
		RsTest rt = new RsTest();
		rt.selectList("select * from hstest where id>?", 0);
		System.out.println(rt.rsList().size());
		List<RsTest> list = rt.rsList();
		for (RsTest r : list) {
			System.out.println(r.getString("value"));
		}
	}

	public static void main(String[] args) throws Exception {
		testSelect();
		System.out.println("=================");
		testSelectList();
	}
}

/**
 * @File:jdao: com.jdao :RsScanTest.java
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: RsTest CRUD 操作类
 */
class RsTest extends DBUtils<RsTest> {
}
