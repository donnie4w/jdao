package com.jdao.action;

import java.util.Properties;
import javax.sql.DataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataSourceTest {
	public static DataSource getByC3p0(String db) {
		return new ComboPooledDataSource(db);
	}

	public static DataSource getByDruid() {
		try {
			Properties p = new Properties();
			p.load(DataSourceTest.class.getClassLoader().getResourceAsStream("com/jdao/action/druid.properties"));
			return DruidDataSourceFactory.createDataSource(p);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
