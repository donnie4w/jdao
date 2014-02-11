package com.jdao.dbHandler;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.jdao.base.Table;
import com.jdao.util.Utils;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class JdaoUtil {

	public static <T extends Table<?>> List<T> selectDaos(Connection con, String sql, Class<T> classType, Object[] args) throws Exception {
		List<T> retList = new ArrayList<T>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			if (args == null || args.length == 0) {
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
			} else {
				ps = con.prepareStatement(sql);
				for (int i = 1; i <= args.length; i++) {
					ps.setObject(i, args[i - 1]);
				}
				rs = ps.executeQuery();
			}
			if (rs != null) {
				while (rs.next()) {
					int columncount = rs.getMetaData().getColumnCount();
					T object = classType.getConstructor().newInstance();
					for (int i = 1; i <= columncount; i++) {
						String columnName = Utils.delUnderline(rs.getMetaData().getColumnLabel(i));
						if (!Utils.isContainsLowerCase(columnName)) {
							columnName = columnName.toLowerCase();
						}
						String setMethodName = "set" + Utils.upperFirstChar(columnName);
						Method setMethod = classType.getMethod(setMethodName, new Class[] { classType.getDeclaredField(columnName).getType() });
						if (setMethod.getGenericParameterTypes()[0].toString().equals(BigDecimal.class.toString())) {
							if (rs.getString(i) != null)
								setMethod.invoke(object, new BigDecimal(rs.getString(i)));
						} else {
							if (rs.getObject(i) != null) {
								setMethod.invoke(object, rs.getObject(i));
							}
						}
					}
					retList.add(object);
				}
			}
			return retList;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} finally {
				if (ps != null)
					ps.close();
			}
		}
	}

	public static <T extends Table<?>> T selectDao(Connection con, String sql, Class<T> classType, Object[] args) throws Exception {
		ResultSet rs = null;
		PreparedStatement ps = null;
		T object = null;
		try {
			if (args == null || args.length == 0) {
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
			} else {
				ps = con.prepareStatement(sql);
				for (int i = 1; i <= args.length; i++) {
					ps.setObject(i, args[i - 1]);
				}
				rs = ps.executeQuery();
			}
			if (rs != null) {
				if (rs.next()) {
					object = classType.getConstructor().newInstance();
					int columncount = rs.getMetaData().getColumnCount();
					for (int i = 1; i <= columncount; i++) {
						String columnName = Utils.delUnderline(rs.getMetaData().getColumnLabel(i));
						if (!Utils.isContainsLowerCase(columnName)) {
							columnName = columnName.toLowerCase();
						}
						String setMethodName = "set" + Utils.upperFirstChar(columnName);
						Method setMethod = classType.getMethod(setMethodName, new Class[] { classType.getDeclaredField(columnName).getType() });
						if (setMethod.getGenericParameterTypes()[0].toString().equals(BigDecimal.class.toString())) {
							if (rs.getString(i) != null)
								setMethod.invoke(object, new BigDecimal(rs.getString(i)));
						} else {
							if (rs.getObject(i) != null) {
								setMethod.invoke(object, rs.getObject(i));
							}
						}
					}
				}
			}
			return object;

		} finally {
			try {
				if (rs != null)
					rs.close();
			} finally {
				if (ps != null)
					ps.close();
			}
		}
	}
}
