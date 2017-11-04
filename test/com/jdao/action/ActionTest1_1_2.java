package com.jdao.action;

import java.util.Properties;
import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.jdao.base.DaoFactory;
import com.jdao.base.QueryDao;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoHandlerFactory;
import com.jdao.util.CreateDaoUtil;

/**
 * @File:jdao: com.jdao.action :ActionTest1_1_2.java
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: 测试 druid
 */
@Deprecated
public class ActionTest1_1_2 {

	public static DataSource getDataSource() throws Exception {
		Properties p = new Properties();
		p.load(ActionTest1_1_2.class.getClassLoader().getResourceAsStream("com/jdao/action/druid.properties"));
		return DruidDataSourceFactory.createDataSource(p);
	}

	public static void createHstest() throws Exception {
		CreateDaoUtil.createFile("com.jdao.action", "hstest", System.getProperty("user.dir") + "\\test\\com\\jdao\\action", getDataSource().getConnection(), "utf-8");
	}

	public static void Test() throws Exception {
		// JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());
		// DaoFactory.setJdaoHandler(jdaohandler);
		// QueryDao qd = new QueryDao("select * from hstest where id>10 limit ?,1",0);
		DaoFactory.setDefaultDataSource(getDataSource());
		QueryDao qd = new QueryDao().PageTurn("select * from hstest where id>0", 0, 1);
		qd.setLoggerOn(true);
		System.out.println(qd.getTotalcount());

		Hstest h = new Hstest();
		h.setLoggerOn(true);
		h.setPageTurn(true);
		h.where(Hstest.ID.GE(1));
		h.limit(0, 1);
		h = h.queryById();
		System.out.println(h.getTotalcount());
		System.out.println(h.getId());
	}

	public static void Test2() throws Exception {
		JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());
		jdaohandler.executeUpdate("insert into hstest (id,rowname,value) values(1,\"donnie\",\"wuxiaodong\")");
	}

	public static void main(String[] args) throws Exception {
		// createHstest();
		Test();
		// Test2();

	}
}
