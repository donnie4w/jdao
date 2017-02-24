package com.jdao.dbHandler;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.jdao.base.QueryDao;
import com.jdao.base.Table;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public interface JdaoHandler extends Serializable{
	/**
	 * @return  返回连接
	 */
	public Connection getConnection();

	/**
	 * @throws SQLException
	 * @return QueryDao类
	 */
	public QueryDao executeQuery(String sql) throws SQLException;
	
    /**
     * auther donnie wu
     * @param sql
     * @param values
     * @return QueryDao类
     * @throws SQLException
     */
	public QueryDao executeQuery(String sql, Object... values) throws SQLException;

	/**
	 * auther donnie wu
	 * @param <T>
	 * @param sql
	 * @param claz
	 * @return  返回 List<T>
	 * @throws Exception
	 */
	public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws Exception;

	
    /**
     * auther donnie wu
     * @param <T>
     * @param claz
     * @param sql
     * @param values
     * @return 返回 List<T>
     * @throws Exception
     */
	public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws Exception;

    /**
     * auther donnie wu
     * @param <T>
     * @param claz
     * @param sql
     * @param values
     * @return 返回 List<T>
     * @throws Exception
     */
	public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws Exception;

	
	/**
	 * auther donnie wu
	 * @param sql
	 * @param values
	 * @return  返回sql影响的条数
	 * @throws SQLException
	 */
	public int executeUpdate(String sql, Object... values) throws SQLException;

	/**
	 * auther donnie wu
	 * @param sql
	 * @param values
	 * @return  返回sql影响的条数
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException;

	/**
	 * auther donnie wu
	 * @param sql
	 * @param list
	 * @return  返回批处理影响的条数数组
	 * @throws SQLException
	 */
	public int[] executeBatch(String sql, List<Object[]> list) throws SQLException;

	/**
	 * auther donnie wu
	 * 
	 * @param auto
	 * @return boolean
	 * @throws SQLException
	 *             事务开启或关闭
	 */
	public boolean setAutoCommit(boolean auto) throws SQLException;

	/**
	 * auther donnie wu
	 * @throws SQLException
	 * 事务提交
	 */
	public void commit() throws SQLException;

    /**
     * auther donnie wu
     * @throws SQLException
     * 事务回滚
     */
	public void rollBack() throws SQLException;

	/**
	 * auther donnie wu
	 * @throws SQLException
	 * 连接关闭
	 */
	public void close() throws SQLException;

	/**
	 * auther donnie wu
	 * @param con
	 * @throws SQLException
	 * 指定连接关闭
	 */
	public void close(Connection con) throws SQLException;
}