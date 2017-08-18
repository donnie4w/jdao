package com.jdao.action;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.jdao.base.DaoFactory;
import com.jdao.base.QueryDao;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoHandlerFactory;

/**
 * @File:jdao: com.jdao.action :ActionTest1_1_2.java
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: 测试 druid
 */
public class ActionTest1_1_2 {

	public static DataSource getDataSource() throws Exception {
		Properties p = new Properties();
		InputStream inStream = ActionTest1_1_2.class.getClassLoader().getResourceAsStream("druid.properties");
		p.load(inStream);
		DataSource dds = DruidDataSourceFactory.createDataSource(p);
		return dds;
	}

	public static void Test() throws Exception {
		JdaoHandler jdaohandler = JdaoHandlerFactory.getJdaoHandler(getDataSource());
		DaoFactory.setJdaoHandler(jdaohandler);
		QueryDao qd = new QueryDao("select * from tablea limit 1");
		String userid = qd.field2String("userid");
		System.out.println("userid==>" + userid);
	}

	public static void main(String[] args) throws Exception {
		Test();
	}
}
