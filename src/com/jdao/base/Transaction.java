package com.jdao.base;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoHandlerFactory;

/**
 * @File:jdao: com.jdao.base :Transaction.java
 * @Date:2017年11月4日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: 事务管理
 */
public class Transaction {
	JdaoHandler jh;

	public Transaction(JdaoHandler jh) throws SQLException {
		this.jh = jh;
		this.jh.setAutoCommit(false);
	}

	public Transaction(DataSource ds) throws SQLException {
		this.jh = JdaoHandlerFactory.getJdaoHandler(ds);
		this.jh.setAutoCommit(false);
	}

	public Transaction(DBUtils<?> db) throws SQLException {
		this.jh = db.getJdaoHandler();
		this.jh.setAutoCommit(false);
	}

	public JdaoHandler getJdaoHandler() {
		return jh;
	}

	/**
	 * @throws SQLException
	 *             事务提交
	 */
	public void commit() throws SQLException {
		this.jh.commit();
	}

	/**
	 * @throws SQLException
	 *             事务回滚
	 */
	public void rollBackAndClose() throws SQLException {
		this.jh.rollBack();
		this.close();
	}

	/**
	 * @throws SQLException
	 *             事务提交
	 */
	public void commitAndClose() throws SQLException {
		this.jh.commit();
		this.close();
	}

	/**
	 * @throws SQLException
	 *             事务回滚
	 */
	public void rollBack() throws SQLException {
		this.jh.rollBack();
	}

	/**
	 * @throws SQLException
	 *             连接关闭
	 */
	public void close() throws SQLException {
		this.jh.close();
	}

}
