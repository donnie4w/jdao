package com.jdao.base;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jdao.dbHandler.JdaoHandler;
import com.jdao.dbHandler.JdaoHandlerFactory;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-2-2
 * @verion 1.0.1 存储过程调用
 */
public class ProcedureCall {

	private String sql;
	private Params[] params;

	private Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
	private Map<Integer, Object> inTypesMap = new HashMap<Integer, Object>();
	private Map<Integer, Integer> outTypesMap = new HashMap<Integer, Integer>();

	public ProcedureCall(Connection conn, String sql, Params... params) throws SQLException {
		this.sql = sql;
		this.params = params;
		parseTypes();
		call_(conn);
	}

	public ProcedureCall(JdaoHandler jdao, String sql, Params... params) throws SQLException {
		this.sql = sql;
		this.params = params;
		parseTypes();
		call(jdao);
	}

	public ProcedureCall(DataSource ds, String sql, Params... params) throws SQLException {
		this.sql = sql;
		this.params = params;
		parseTypes();
		call(JdaoHandlerFactory.getJdaoHandler(ds));
	}

	public ProcedureCall(String sql, Params... params) throws SQLException {
		this.sql = sql;
		this.params = params;
		parseTypes();
		call(DaoFactory.getJdaoHandler(ProcedureCall.class, null));
	}

	public Object fieldIndex(int i) {
		return valuesMap.get(i);
	}

	final void call(JdaoHandler jdao) throws SQLException {
		Connection con = null;
		try {
			con = jdao.getConnection();
			call_(con);
		} finally {
			jdao.close(con);
		}
	}

	final void call_(Connection conn) throws SQLException {
		CallableStatement stmt = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("{call ").append(sql).append("(");
			if (params != null) {
				int i = 0;
				int length = params.length;
				while (length-- > 0) {
					sb.append("?");
					if (i < length) {
						sb.append(",");
					}
				}
			}
			sb.append(")}");
			System.out.println(sb.toString());
			stmt = conn.prepareCall(sb.toString());
			for (Integer i : inTypesMap.keySet()) {
				stmt.setObject(i, inTypesMap.get(i));
			}
			for (Integer i : outTypesMap.keySet()) {
				stmt.registerOutParameter(i, outTypesMap.get(i));
			}
			stmt.execute();
			for (Integer i : outTypesMap.keySet()) {
				valuesMap.put(i, stmt.getObject(i));
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}

	final void parseTypes() {
		if (params != null) {
			for (int i = 0; i < params.length; i++) {
				if (params[i] instanceof Out) {
					outTypesMap.put(i + 1, params[i].getTypes());
				} else if (params[i] instanceof InOut) {
					outTypesMap.put(i + 1, params[i].getTypes());
					inTypesMap.put(i + 1, params[i].getValue());
				} else {
					inTypesMap.put(i + 1, params[i].getValue());
				}
			}
		}
	}
}
