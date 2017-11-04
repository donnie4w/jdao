package com.jdao.dbHandlerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
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
	private Connection conn;

	public JdaoHandlerDefaultImpl(DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		conn = dataSource.getConnection();
		conn.setAutoCommit(true);
	}

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public Connection getConnection() {
		return conn;
	}

	@Override
	public void close() throws SQLException {
		if (conn != null) {
			conn.close();
			conn = null;
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
		if (conn != null) {
			conn.commit();
		}
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
			try {
				if (ps != null)
					ps.close();
			} finally {
				if (!transaction)
					close(conn);
			}
		}
	}

	@Override
	public QueryDao executeQuery(String sql) throws SQLException {
		try {
			return new QueryDao(conn, sql);
		} finally {
			if (!transaction)
				close(conn);
		}
	}

	@Override
	public QueryDao executeQuery(String sql, Object... values) throws SQLException {
		try {
			return new QueryDao(conn, sql, values);
		} finally {
			if (!transaction)
				close(conn);
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws Exception {
		try {
			return JdaoUtil.selectDaos(conn, sql, claz, null);
		} finally {
			if (!transaction)
				close(conn);
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws Exception {
		try {
			return JdaoUtil.selectDaos(conn, sql, claz, values);
		} finally {
			if (!transaction)
				close(conn);
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
				if (!transaction)
					close(conn);
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
				if (!transaction)
					close(conn);
			}
		}
	}

	@Override
	public void rollBack() throws SQLException {
		if (conn != null)
			conn.rollback();
	}

	@Override
	public synchronized boolean setAutoCommit(boolean auto) throws SQLException {
		if (auto) {
			conn.setAutoCommit(true);
			transaction = false;
		} else {
			conn.setAutoCommit(false);
			transaction = true;
		}
		return true;
	}

	@Override
	public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws Exception {
		try {
			return JdaoUtil.selectDao(conn, sql, claz, values);
		} finally {
			if (!transaction)
				close(conn);
		}
	}
}
