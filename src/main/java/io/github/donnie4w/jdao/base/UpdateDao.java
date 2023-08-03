/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.sql.Connection;
@Deprecated
public class UpdateDao {

	public static void execute(String sql, Object... objects) throws JException {
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

	public static void execute(JdaoHandler jdao, String sql, Object... objects) throws JException {
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
