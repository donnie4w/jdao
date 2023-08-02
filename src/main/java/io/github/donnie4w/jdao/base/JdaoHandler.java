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
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public interface JdaoHandler extends Serializable {

	public DataSource getDataSource();

	/**
	 * @return 返回连接
	 */
	public Connection getConnection();

	/**
	 * @throws JException
	 * @return QueryDao类
	 */
	public QueryBean executeQuery(String sql) throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param sql
	 * @param values
	 * @return QueryDao类
	 * @throws SQLException
	 */
	public QueryBean executeQuery(String sql, Object... values) throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param <T>
	 * @param sql
	 * @param claz
	 * @return 返回 List<T>
	 * @throws Exception
	 */
	public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param <T>
	 * @param claz
	 * @param sql
	 * @param values
	 * @return 返回 List<T>
	 * @throws JException
	 */
	public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param <T>
	 * @param claz
	 * @param sql
	 * @param values
	 * @return 返回 List<T>
	 * @throws JException
	 */
	public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param sql
	 * @param values
	 * @return 返回sql影响的条数
	 * @throws JException
	 */
	public int executeUpdate(String sql, Object... values) throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param sql
	 * @return 返回sql影响的条数
	 * @throws JException
	 */
	public int executeUpdate(String sql) throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param sql
	 * @param list
	 * @return 返回批处理影响的条数数组
	 * @throws JException
	 */
	public int[] executeBatch(String sql, List<Object[]> list) throws JException;

	/**
	 * auther donnie wu
	 *
	 * @param auto
	 * @return boolean
	 * @throws JException
	 *             事务开启或关闭
	 */
	boolean setAutoCommit(boolean auto) throws JException;

	/**
	 * auther donnie wu
	 *
	 * @throws JException
	 *             事务提交
	 */
	void commit() throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @throws SQLException
	 *             事务回滚
	 */
	void rollBack() throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @throws JException
	 *             连接关闭
	 */
	public void close() throws JException;

	/**
	 * auther donnie wu
	 * 
	 * @param con
	 * @throws JException
	 *             指定连接关闭
	 */
	public void close(Connection con) throws JException;
}