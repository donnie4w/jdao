package com.jdao.base;

import java.sql.Connection;
import java.sql.SQLException;

import com.jdao.dbHandler.JdaoHandler;

public class UpdateDao {

	public static void execute(String sql, Object... objects) throws SQLException {
		Connection con = null;
		JdaoHandler jdao = null;
		try {
			jdao = DaoFactory.getJdaoHandler(UpdateDao.class);
			con = jdao.getConnection();
			execute_(con, sql, objects);
		} finally {
			jdao.close(con);
		}
	}

	public static void execute(JdaoHandler jdao, String sql, Object... objects) throws SQLException {
		Connection con = null;
		try {
			if (jdao == null) {
				jdao = DaoFactory.getJdaoHandler(UpdateDao.class);
			}
			con = jdao.getConnection();
			execute_(con, sql, objects);
		} finally {
			jdao.close(con);
		}
	}

	private static void execute_(Connection conn, String sql, Object... objects) {

	}

}
