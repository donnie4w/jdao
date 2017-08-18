package com.jdao.dbHandlerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import com.jdao.base.QueryDao;
import com.jdao.base.Table;
import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoUtil;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0 JdaoHandler implement
 */
public class JdaoImpl4JdbcTemplate implements JdaoHandler {

	private static final long serialVersionUID = 1L;
	JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void close() throws SQLException {
	}

	@Override
	public void commit() throws SQLException {
	}

	@Override
	public QueryDao executeQuery(String sql) throws SQLException {
		Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
		try {
			return new QueryDao(con, sql);
		} finally {
			DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
	}

	@Override
	public QueryDao executeQuery(String sql, Object... values) throws SQLException {
		Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
		try {
			return new QueryDao(con, sql, values);
		} finally {
			DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws Exception {
		Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
		try {
			return JdaoUtil.selectDaos(con, sql, claz, null);
		} finally {
			DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
	}

	@Override
	public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws Exception {
		Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
		try {
			return JdaoUtil.selectDaos(con, sql, claz, values);
		} finally {
			DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
	}

	@Override
	public int executeUpdate(String sql, Object... values) throws SQLException {
		return jdbcTemplate.update(sql, values);
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		return jdbcTemplate.update(sql);
	}

	@Override
	public Connection getConnection() {
		return DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
	}

	@Override
	public void rollBack() throws SQLException {
	}

	@Override
	public boolean setAutoCommit(boolean auto) throws SQLException {
		return false;
	}

	@Override
	public void close(Connection con) throws SQLException {
		DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
	}

	@Override
	public int[] executeBatch(String sql, final List<Object[]> list) throws SQLException {
		BatchPreparedStatementSetter bpss = null;
		bpss = new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return list.size();
			}

			public void setValues(PreparedStatement ps, int index) throws SQLException {
				Object[] oo = list.get(index);
				int length = oo.length;
				for (int i = 1; i <= length; i++) {
					ps.setObject(i, oo[i - 1]);
				}
			}

		};
		return jdbcTemplate.batchUpdate(sql, bpss);
	}

	@Override
	public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws Exception {
		Connection con = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
		try {
			return JdaoUtil.selectDao(con, sql, claz, values);
		} finally {
			DataSourceUtils.releaseConnection(con, jdbcTemplate.getDataSource());
		}
	}

}
