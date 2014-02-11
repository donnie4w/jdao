package com.jdao.dbHandlerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import com.jdao.base.QueryDao;
import com.jdao.base.Table;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-13
 * @verion 1.0 注意每次使用需创建实例
 */
public class JdaoHandlerImpl implements JdaoHandler {

	private final static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
	private DataSource dataSource;
	private Connection conn = null;
	private boolean transaction = false;

	public JdaoHandlerImpl(String db) {
		if (!dataSourceMap.containsKey(db)) {
			synchronized (JdaoHandlerImpl.class) {
				if (!dataSourceMap.containsKey(db)) {
					dataSource = new ComboPooledDataSource(db);
					dataSourceMap.put(db, dataSource);
				}
			}
		} else {
			dataSource = dataSourceMap.get(db);
		}
		getConnection();
	}

	public Connection getConnection() {
		if (conn == null) {
			synchronized (JdaoHandlerImpl.class) {
				if (conn == null) {
					try {
						conn = dataSource.getConnection();
						conn.setAutoCommit(true);
						return conn;
					} catch (SQLException e) {
						return null;
					}
				}
			}
		}
		return conn;
	}

	@Override
	public void close() throws SQLException {
		if (conn != null)
			conn.close();
	}

	@Override
	public void close(Connection con) throws SQLException {
		if (con != null)
			con.close();
	}

	@Override
	public void commit() throws SQLException {
		if (conn != null)
			conn.commit();
	}

	@Override
	public int[] executeBatch(String sql, List<Object[]> list) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (Object[] args : list) {
				for (int i = 1; i <= args.length; i++) {
					ps.setObject(i, args[i - 1]);
				}
				ps.addBatch();
			}
			return ps.executeBatch();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	@Override
	public QueryDao executeQuery(String sql) throws SQLException {
		try {
			return new QueryDao(conn, sql);
		} finally {
			if (!transaction && conn != null)
				conn.close();
		}
	}

	@Override
	public QueryDao executeQuery(String sql, Object... values) throws SQLException {
		try {
			return new QueryDao(conn, sql, values);
		} finally {
			if (!transaction && conn != null)
				conn.close();
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws Exception {
		try {
			return JdaoUtil.selectDaos(conn, sql, claz, null);
		} finally {
			if (!transaction && conn != null)
				conn.close();
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws Exception {
		try {
			return JdaoUtil.selectDaos(conn, sql, claz, values);
		} finally {
			if (!transaction && conn != null)
				conn.close();
		}
	}

	@Override
	public int executeUpdate(String sql, Object... values) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 1; i <= values.length; i++) {
				ps.setObject(i, values[i - 1]);
			}
			return ps.executeUpdate();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} finally {
				if (!transaction && conn != null)
					conn.close();
			}
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			return ps.executeUpdate();
		} finally {
			try {
				if (ps != null)
					ps.close();
			} finally {
				if (!transaction && conn != null)
					conn.close();
			}
		}
	}

	@Override
	public void rollBack() throws SQLException {
		if (conn != null)
			conn.rollback();
	}

	@Override
	public boolean setAutoCommit(boolean auto) throws SQLException {
		transaction = auto;
		conn.setAutoCommit(auto);
		return false;
	}

	@Override
	public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws Exception {
		try {
			return JdaoUtil.selectDao(conn, sql, claz, values);
		} finally {
			if (!transaction && conn != null)
				conn.close();
		}
	}
}
