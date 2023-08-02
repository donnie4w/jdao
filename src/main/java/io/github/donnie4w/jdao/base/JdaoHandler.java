/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

/**
 * Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * date 2013-1-10
 * verion 1.0
 */
public interface JdaoHandler extends Serializable {

	public DataSource getDataSource();

	/**
	 *  返回连接
	 */
	public Connection getConnection();

	public QueryBean executeQuery(String sql) throws JException;

	public QueryBean executeQuery(String sql, Object... values) throws JException;

	public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws JException;

	public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws JException;

	public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws JException;

	/**
	 *  返回sql影响的条数
	 */
	public int executeUpdate(String sql, Object... values) throws JException;

	/**
	 *  返回sql影响的条数
	 */
	public int executeUpdate(String sql) throws JException;

	/**
	 * 返回批处理影响的条数数组
	 */
	public int[] executeBatch(String sql, List<Object[]> list) throws JException;

	/**
	 * 事务开启或关闭
	 */
	boolean setAutoCommit(boolean auto) throws JException;

	/**
	 *   事务提交
	 */
	void commit() throws JException;

	/**
	 *
	 *   事务回滚
	 */
	void rollBack() throws JException;

	/**
	 *   连接关闭
	 */
	public void close() throws JException;

	/**
	 *   指定连接关闭
	 */
	public void close(Connection con) throws JException;
}