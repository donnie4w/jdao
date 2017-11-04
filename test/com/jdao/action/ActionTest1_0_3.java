package com.jdao.action;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.jdao.base.DaoFactory;
import com.jdao.base.QueryDao;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-3-7
 * @verion 1.0.3 1_0_3 版本的新功能测试
 */
@Deprecated
public class ActionTest1_0_3 {
	// static JdaoHandler jdao = JdaoHandlerFactory.getDBHandler4c3p0SingleTon();
	static {
		// 给每个dao(包括QueryDao查询类)注册不同的数据源操作对象。
		// DaoFactory.dataSourceRegister(Hstest.class, jdao);
		// DaoFactory.dataSourceRegister(QueryDao.class, jdao);

		// 如果是统一数据源，可以用统一设置DaoFactory.setJdaoHandler(jdao)对所有操作都可以生效;

		// JdaoHandler优先级：
		// dao.setJdaoHandler(jdao) >
		// DaoFactory.dataSourceRegister(Hstest.class, jdao) >
		// DaoFactory.setJdaoHandler(jdao)
		DaoFactory.setDefaultDataSource(DataSourceTest.getByDruid());
	}

	@Test
	static void daoTest() throws SQLException {
		Hstest hs = new Hstest();
		hs.setRowname("wuxiaoodong1_0_3");
		hs.setValue("value1_0_3");
		// 获得mysql插入数据的ID
		System.out.println("insert id 为  " + hs.saveAndGetLastInsertId4MYSQL());
	}

	@Test
	static void qureyDaoTest() throws SQLException {
		QueryDao qd = new QueryDao("select id,rowname,value from hstest where id=?", 3);
		System.out.println(qd.fieldValue("rowname"));
	}

	@Test
	static void qureyDaoTest2() throws Exception {
		List<Hstest> list = QueryDao.queryForBeens(Hstest.class, "select id,rowname,value from hstest where id=?", 1);
		System.out.println(list.get(0).getValue());
	}

	public static void main(String[] args) throws Exception {
		// ActionTest1_0_3.daoTest();
		ActionTest1_0_3.qureyDaoTest();
		ActionTest1_0_3.qureyDaoTest2();
	}

}
