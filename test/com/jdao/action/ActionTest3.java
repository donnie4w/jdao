package com.jdao.action;

import java.sql.SQLException;

import org.junit.Test;
import com.jdao.dbHandler.JdaoHandler;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-13
 * @verion 1.0 事务测试
 */
@Deprecated
public class ActionTest3 {

	@Test
	public void insertBetch(JdaoHandler jdao) throws Exception {
		// insert into hstest (id,rowname,value) values(?,?,?)
		Hstest t = new Hstest();
		t.setJdaoHandler(jdao);
		t.setId(1);
		t.setRowname("donnie1");
		t.setValue("wuxiaodong1");
		t.addBatch();
		t.setId(2);
		t.setRowname("donnie2");
		t.setValue("wuxiaodong2");
		t.addBatch();
		t.batchForSave();
	}

	@Test
	public void insertBetch2(JdaoHandler jdao) throws Exception {
		// insert into hstest (id,rowname,value) values(?,?,?)
		Hstest t = new Hstest();
		t.setJdaoHandler(jdao);
		t.setId(3);
		t.setRowname("donnie3");
		t.setValue("wuxiaodong3");
		t.addBatch();
		// 制作一个重复主键异常
		// t.setId(3);
		t.setId(4);
		t.setRowname("donnie4");
		t.setValue("wuxiaodong4");
		t.addBatch();
		t.batchForSave();
	}

	@Test
	public void insertBetch3(JdaoHandler jdao) throws Exception {
		// insert into hstest (id,rowname,value) values(?,?,?)
		Hstest t = new Hstest();
		t.setJdaoHandler(jdao);
		t.setId(5);
		t.setRowname("donnie5");
		t.setValue("wuxiaodong5");
		t.addBatch();
		t.setId(6);
		t.setRowname("donnie6");
		t.setValue("wuxiaodong6");
		t.addBatch();
		t.batchForSave();
	}

	/**
	 * jdbcTemplate 事务测试 auther donnie wu
	 * 
	 * @throws SQLException
	 */
	@Test
	public void transactionTest() throws SQLException {
		JdaoHandler jdao = com.jdao.dbHandler.JdaoHandlerFactory.getJdaoHandler(DataSourceTest.getByDruid());
		// 开启事务
		try {
			jdao.setAutoCommit(false);
			insertBetch(jdao);
			insertBetch2(jdao);
			jdao.commit();
		} catch (Exception e) {
			jdao.rollBack();
			e.printStackTrace();
		} finally {
			jdao.close();
		}
	}

	public static void main(String[] args) throws Exception {
		ActionTest3 at2 = new ActionTest3();
		at2.transactionTest();
	}
}