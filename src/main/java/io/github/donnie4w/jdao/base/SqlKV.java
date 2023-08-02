/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.util.Arrays;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-13
 * @verion 1.0
 */
public class SqlKV {
	String sql = null;
	Object[] args = null;

	public SqlKV(String sql, Object... objects) {
		this.sql = sql;
		this.args = objects;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public boolean equals(Object skv) {
		if (skv == null)
			return false;
		String sql2 = ((SqlKV) skv).getSql();
		Object[] args2 = ((SqlKV) skv).getArgs();
		if (sql != null && sql2 != null && sql.equals(sql2)) {
			if (args == null && args2 == null) {
				return true;
			}
			if (Arrays.equals(args, args2)) {
				return true;
			}
		}
		return false;
	}

	public int hashCode() {
		if (args == null)
			return sql.hashCode();
		else {
			final StringBuilder sb = new StringBuilder();
			for (Object o : args) {
				sb.append(o.toString()).append(",");
			}
			return 31 * sql.hashCode() + sb.toString().hashCode();
		}
	}

	public String toString() {
		if (args == null)
			return "[SQL:" + sql + "]ARGS[]";
		else {
			final StringBuilder sb = new StringBuilder();
			for (Object o : args) {
				sb.append(o.toString()).append(",");
			}
			if (sb.length() > 2)
				sb.delete(sb.length() - 1, sb.length());
			return "SQL[" + sql + "]ARGS[" + sb.toString() + "]";
		}

	}
}