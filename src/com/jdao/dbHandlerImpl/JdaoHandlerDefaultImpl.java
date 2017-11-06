package com.jdao.dbHandlerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import com.jdao.base.JdaoRuntimeException;
import com.jdao.base.QueryDao;
import com.jdao.base.Table;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoUtil;

/**
 * @File:jdao: com.jdao.dbHandlerImpl :JdaoHandlerDefaultImpl.java
 * @Date:2017年11月4日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc:
 */
public class JdaoHandlerDefaultImpl implements JdaoHandler {
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;
	private boolean transaction = false;
	private Connection _conn;

	public JdaoHandlerDefaultImpl(DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		// conn = dataSource.getConnection();
		// conn.setAutoCommit(true);
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public Connection getConnection() {
		try {
			return getConn();
		} catch (SQLException e) {
			throw new JdaoRuntimeException(e);
		}
	}

	@Override
	public void close() throws SQLException {
		if (_conn != null) {
			_conn.close();
			_conn = null;
		}
	}

	@Override
	public void close(Connection con) throws SQLException {
		if (con != null) {
			con.close();
		}
	}

	@Override
	public void commit() throws SQLException {
		if (_conn != null) {
			_conn.commit();
		}
	}

	synchronized Connection getConn() throws SQLException {
		if (this._conn != null) {
			return _conn;
		} else {
			Connection c = dataSource.getConnection();
			c.setAutoCommit(true);
			return c;
		}
	}

	@Override
	public int[] executeBatch(String sql, List<Object[]> list) throws SQLException {
		PreparedStatement ps = null;
		Connection c = getConn();
		try {
			ps = c.prepareStatement(sql);
			for (Object[] args : list) {
				for (int i = 1; i <= args.length; i++) {
					ps.setObject(i, args[i - 1]);
				}
				ps.addBatch();
			}
			return ps.executeBatch();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} finally {
				if (!transaction)
					close(c);
			}
		}
	}

	@Override
	public QueryDao executeQuery(String sql) throws SQLException {
		Connection c = getConn();
		try {
			return new QueryDao(c, sql);
		} finally {
			if (!transaction)
				close(c);
		}
	}

	@Override
	public QueryDao executeQuery(String sql, Object... values) throws SQLException {
		Connection c = getConn();
		try {
			return new QueryDao(c, sql, values);
		} finally {
			if (!transaction)
				close(c);
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws Exception {
		Connection c = getConn();
		try {
			return JdaoUtil.selectDaos(c, sql, claz, null);
		} finally {
			if (!transaction)
				close(c);
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws Exception {
		Connection c = getConn();
		try {
			return JdaoUtil.selectDaos(c, sql, claz, values);
		} finally {
			if (!transaction)
				close(c);
		}
	}

	@Override
	public int executeUpdate(String sql, Object... values) throws SQLException {
		PreparedStatement ps = null;
		Connection c = getConn();
		try {
			ps = c.prepareStatement(sql);
			for (int i = 1; i <= values.length; i++) {
				ps.setObject(i, values[i - 1]);
			}
			return ps.executeUpdate();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} finally {
				if (!transaction)
					close(c);
			}
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		PreparedStatement ps = null;
		Connection c = getConn();
		try {
			ps = c.prepareStatement(sql);
			return ps.executeUpdate();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} finally {
				if (!transaction)
					close(c);
			}
		}
	}

	@Override
	public void rollBack() throws SQLException {
		if (_conn != null)
			_conn.rollback();
	}

	@Override
	public synchronized boolean setAutoCommit(boolean auto) throws SQLException {
		if (auto) {
			transaction = false;
			close();
		} else {
			this._conn = getConnection();
			this._conn.setAutoCommit(false);
			transaction = true;
		}
		return true;
	}

	@Override
	public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws Exception {
		Connection c = getConn();
		try {
			return JdaoUtil.selectDao(c, sql, claz, values);
		} finally {
			if (!transaction)
				close(c);
		}
	}
}
