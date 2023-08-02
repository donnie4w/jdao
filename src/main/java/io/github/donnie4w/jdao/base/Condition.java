/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

public class Condition {
	private SqlKV sqlKV = null;
	private String node = "";

	private Condition(SqlKV sqlKV, String node) {
		this.sqlKV = sqlKV;
		this.node = node == null ? "" : node;
	}

	public static Condition newInstance(SqlKV sqlKV, String node) {
		return new Condition(sqlKV, node);
	}

	public SqlKV getSqlKV() {
		return sqlKV;
	}

	public String getNode() {
		return node;
	}

	public boolean equals(Object obj) {
		Condition cd = (Condition) obj;
		if (sqlKV == null || cd.getSqlKV() == null || !sqlKV.equals(cd.getSqlKV()) || !node.equals(cd.getNode())) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		if (sqlKV == null)
			return node.hashCode();
		return 31 * sqlKV.hashCode() + node.hashCode();
	}

	public String toString() {
		return "[node][" + node + "]|" + sqlKV.toString();
	}

}