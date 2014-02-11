package com.jdao.action;

import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jdao.dao.Hstest;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandlerImpl.JdaoHandlerFactory;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0 事务测试  jdbcTemplate 事务
 */
public class ActionTest2 {
	JdaoHandler jdao = JdaoHandlerFactory.getDBHandler4spring();

	@Test
	public void insertBetch() throws Exception {
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
	public void insertBetch2() throws Exception {
		// insert into hstest (id,rowname,value) values(?,?,?)
		Hstest t = new Hstest();
		t.setJdaoHandler(jdao);
		t.setId(3);
		t.setRowname("donnie3");
		t.setValue("wuxiaodong3");
		t.addBatch();
		// t.setId(3);
		t.setId(4);
		t.setRowname("donnie4");
		t.setValue("wuxiaodong4");
		t.addBatch();
		t.batchForSave();
	}

	@Test
	public void insertBetch3() throws Exception {
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
	 */
	@Test
	public void transactionTest() {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setName(ActionTest2.class.getName());
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		def.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
		PlatformTransactionManager ptm = JdaoHandlerFactory.getTransactionManager();
		TransactionStatus status = ptm.getTransaction(def);
		try {
			insertBetch();
			insertBetch2();
			ptm.commit(status);
		} catch (Exception e) {
			ptm.rollback(status);
			e.printStackTrace();
		}
	}

	@Test
	public void transactionTest2() {
		try {
			insertBetch3();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ActionTest2 at2 = new ActionTest2();
		at2.transactionTest();
		at2.transactionTest2();
	}
}