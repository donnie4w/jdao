/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import javax.sql.DataSource;

/**
 * @File:jdao: Transaction.java
 * @Date:2017年11月4日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: 事务管理
 */
public class Transaction {
	JdaoHandler jh;

	public Transaction(JdaoHandler jh) throws JException {
		this.jh = jh;
		this.jh.setAutoCommit(false);
	}

	public Transaction(DataSource ds) throws JException {
		this.jh = JdaoHandlerFactory.getJdaoHandler(ds);
		this.jh.setAutoCommit(false);
	}

	public Transaction(DBUtils<?> db) throws JException {
		this.jh = db.getJdaoHandler();
		this.jh.setAutoCommit(false);
	}

	public JdaoHandler getJdaoHandler() {
		return jh;
	}

	/**
	 * @throws JException
	 *             事务提交
	 */
	public void commit() throws JException {
		this.jh.commit();
	}

	/**
	 * @throws JException
	 *             事务回滚并关闭连接
	 */
	public synchronized void rollBackAndClose() throws JException {
		this.jh.rollBack();
		this.close();
	}

	/**
	 * @throws JException
	 *             事务提交并关闭链接
	 */
	public synchronized void commitAndClose() throws JException {
		this.jh.commit();
		this.close();
	}

	/**
	 * @throws JException
	 *             事务回滚
	 */
	public void rollBack() throws JException {
		this.jh.rollBack();
	}

	/**
	 * @throws JException
	 *             连接关闭
	 */
	public void close() throws JException {
		this.jh.close();
	}

}
