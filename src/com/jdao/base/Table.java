package com.jdao.base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jdao.dbHandler.JdaoHandler;

/**
 * @param <T>
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0.9
 */
public class Table<T extends Table<?>> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final String JdaoVersion = "1.1.4";

	static final String AND = " and ";
	private transient Log logger = Log.newInstance();
	protected Map<String, Object> whereMap = new LinkedHashMap<String, Object>();
	protected Map<String, Object> havingMap = new LinkedHashMap<String, Object>();
	protected StringBuilder sortStr = new StringBuilder();
	protected StringBuilder groupStr = new StringBuilder();
	protected int[] limitStr = null;
	protected Fields[] fields = null;
	private String TABLENAME = null;
	private Class<T> clazz = null;
	private Map<Fields, List<Object>> mBatch = new HashMap<Fields, List<Object>>();
	protected boolean isloggerOn = false;
	private transient JdaoHandler jdao = null;
	protected Map<Fields, Object> fieldValueMap = null;
	private transient FieldFilter fieldFilter;
	private boolean isCache = false;
	private String domain = null;
	private String node = null;
	private String commentLine = null;

	public void setJdaoHandler(JdaoHandler jdaoHandler) {
		this.jdao = jdaoHandler;
	}

	public Table(String tablename, Class<T> claz) {
		this.TABLENAME = tablename;
		this.clazz = claz;
		fieldValueMap = new MyMap<Fields, Object>(clazz);
		String pkgn = claz.getPackage().getName();
		if (DaoFactory.jdaoMap.containsKey(claz)) {
			this.jdao = DaoFactory.jdaoMap.get(claz);
		} else if (pkgn!=null && DaoFactory.jdaoPackageMap.containsKey(pkgn)) {
			this.jdao = DaoFactory.jdaoPackageMap.get(pkgn);
		} else {
			this.jdao = DaoFactory.getJaoHandler();
		}
	}

	public void setFields(Fields... fields) {
		this.fields = fields;
	}

	protected void setTableName(String tableName) {
		this.TABLENAME = tableName;
	}

	/**
	 * 时间函数 ，仅用于mysql get time;mysql now() function;
	 */
	public static String Now() {
		return " now() ";
	}

	/**
	 * 时间函数 ，仅用于Sql Server get time;sql Server getdate() function
	 */
	public static String getdate() {
		return " getdate() ";
	}

	/**
	 * 时间函数 ，仅用于oracle get time;oracle sysdate system variable
	 */
	public static String sysdate() {
		return " sysdate ";
	}

	/**
	 * get time; DB2 current system variable
	 */
	public static String current() {
		return " current ";
	}

	/**
	 * 条件 同sql 中where后的条件
	 * 
	 * @param wheres
	 */
	public void where(Where... wheres) {
		for (Where w : wheres) {
			whereMap.put(w.getExpression(), w.getValue());
		}
	}

	/**
	 * 排序 同 sql 中order by
	 * 
	 * @param strings
	 * 
	 */
	public void sort(Sort... sorts) {
		for (Sort s : sorts) {
			if (sortStr.length() > 0) {
				sortStr.append(",");
			}
			sortStr.append(s.getFieldName());
		}
	}

	/**
	 * 分组 同sql中group by
	 * 
	 * @param fields
	 * 
	 */
	public void group(Fields... fields) {
		for (Fields f : fields) {
			if (groupStr.length() > 0)
				groupStr.append(",");
			groupStr.append(f.getFieldName());
		}

	}

	/**
	 * 分组条件，同sql中having
	 * 
	 * @param wheres
	 */
	public void having(Where... wheres) {
		for (Where w : wheres) {
			havingMap.put(w.getExpression(), w.getValue());
		}
	}

	/**
	 * 结果集翻页函数limit仅适用于部分数据库，如mysql
	 * 
	 * @param f
	 * @param t
	 */
	public void limit(int f, int t) {
		limitStr = new int[2];
		limitStr[0] = f;
		limitStr[1] = t;
	}

	private SqlKV query_(Field... fields) throws SQLException {
		final StringBuilder sb = new StringBuilder();
		final List<Object> list = new ArrayList<Object>();
		if (commentLine != null) {
			sb.append("/* ").append(commentLine).append(" */");
		}
		sb.append("select ");
		int length = fields.length;
		int i = 1;
		for (Field hf : fields) {
			sb.append(hf.getFieldName());
			if (i < length) {
				sb.append(",");
			}
			i++;
		}
		sb.append(" from ").append(TABLENAME);
		length = whereMap.size();
		if (length > 0) {
			final StringBuilder sbWhere = new StringBuilder();
			for (String string : whereMap.keySet()) {
				sbWhere.append(string);
				Object o = whereMap.get(string);
				if (o != null)
					if (o instanceof Array) {
						getArrayObj(list, (Array) o);
					} else {
						list.add(o);
					}
			}
			sb.append(sbWhere.toString().replaceFirst(AND, " where "));
		}
		if (groupStr.length() > 0)
			sb.append(" group by ").append(groupStr.toString());

		if (havingMap.size() > 0) {
			final StringBuilder sbhaving = new StringBuilder();
			sbhaving.append(" having");
			for (String string : havingMap.keySet()) {
				sbhaving.append(string);
				Object o = havingMap.get(string);
				if (o instanceof Array) {
					getArrayObj(list, (Array) o);
				} else {
					list.add(havingMap.get(string));
				}
			}
			sb.append(sbhaving.toString());
		}

		if (sortStr.length() > 0)
			sb.append(" order by ").append(sortStr);
		if (limitStr != null) {
			sb.append(" limit ?,?");
			list.add(limitStr[0]);
			list.add(limitStr[1]);
		}
		Object[] args = null;
		String sql = sb.toString();
		if (list.size() > 0) {
			args = new Object[list.size()];
			list.toArray(args);
			if (this.isloggerOn) {
				StringBuilder s = new StringBuilder();
				for (Object o : args) {
					s.append(o).append(",");
				}
				logger.log("[SELETE SQL][" + sql + "][" + s.toString().substring(0, s.length() - 1) + "]");
			}
			return new SqlKV(sql, args);
		} else {
			logger.log("[SELETE SQL][" + sql + "]");
			return new SqlKV(sql);
		}
	}

	/**
	 * 查询所有字段
	 * 
	 * @return List<T>
	 * @throws Exception
	 */
	public List<T> query() throws Exception {
		return query(fields);
	}

	/**
	 * 返回结果只有一个整数时，直接转换为int
	 * 
	 * @param field
	 * @return int
	 * @throws Exception
	 */
	public int queryForInt(Field field) throws Exception {
		return query(field).toInt();
	}

	/**
	 * 
	 * @param fields
	 *            查询字段
	 * @return List<T>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(Fields... fields) throws Exception {
		SqlKV skv = query_(fields);
		Object o = null;
		if (isCache) {
			o = Cache.getCache(domain, clazz, Condition.newInstance(skv, node));
			if (o != null) {
				logger.log("[USE CACHE]:" + skv.toString() + "");
				return (List<T>) o;
			}
		}
		if (skv.getArgs() == null) {
			o = jdao.executeQuery(skv.getSql(), clazz);
		} else {
			o = jdao.executeQuery(clazz, skv.getSql(), skv.getArgs());
		}
		if (isCache) {
			Cache.setCache(domain, (Class<Table<?>>) clazz, Condition.newInstance(skv, node), o);
		}
		return (List<T>) o;
	}

	/**
	 * 
	 * @param fields
	 *            查询字段
	 * @return List<T>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public T queryById(Fields... fields) throws Exception {
		SqlKV skv = query_(fields);
		Object o = null;
		if (isCache) {
			o = Cache.getCache(domain, clazz, Condition.newInstance(skv, node));
			if (o != null) {
				logger.log("[USE CACHE]:" + skv.toString() + "");
				return (T) o;
			}
		}

		if (skv.getArgs() == null) {
			o = jdao.executeQueryById(clazz, skv.getSql());
		} else {
			o = jdao.executeQueryById(clazz, skv.getSql(), skv.getArgs());
		}

		if (isCache) {
			Cache.setCache(domain, (Class<Table<?>>) clazz, Condition.newInstance(skv, node), o);
		}
		return (T) o;
	}

	/**
	 * 
	 * @param fields
	 *            查询字段
	 * @return List<T>
	 * @throws Exception
	 */
	public T queryById() throws Exception {
		return queryById(fields);
	}

	/**
	 * 
	 * @param fields
	 *            查询字段，一般包含函数操作，如 count,sum等
	 * @return QueryDao对象
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public QueryDao query(Field... fields) throws SQLException {
		SqlKV skv = query_(fields);
		Object o = null;
		if (isCache) {
			o = Cache.getCache(domain, clazz, Condition.newInstance(skv, node));
			if (o != null) {
				logger.log("[USE CACHE]:" + skv.toString() + "");
				return (QueryDao) o;
			}
		}
		if (skv.getArgs() == null) {
			o = jdao.executeQuery(skv.getSql());
		} else {
			o = jdao.executeQuery(skv.getSql(), skv.getArgs());
		}
		if (isCache) {
			Cache.setCache(domain, (Class<Table<?>>) clazz, Condition.newInstance(skv, node), o);
		}
		return (QueryDao) o;
	}

	private static void getArrayObj(List<Object> list, Array a) {
		for (Object o : a.getArray()) {
			if (o instanceof Array) {
				getArrayObj(list, (Array) o);
			} else {
				list.add(o);
			}
		}
	}

	/**
	 * 数据插入操作 同sql中insert
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int save() throws SQLException {
		SqlKV kv = save_();
		return jdao.executeUpdate(kv.getSql(), kv.getArgs());
	}

	/**
	 * 仅适用于mysql ，插入并返回主鍵ID,调用的是mysql LAST_INSERT_ID() 函数；
	 * 注意：该方法调用了jdaoHandler的close()方法关闭了连接
	 * 
	 * @return int
	 * @throws SQLException
	 */
	public int saveAndGetLastInsertId4MYSQL() throws SQLException {
		Connection conn = jdao.getConnection();
		try {
			return saveAndGetLastInsertId(conn);
		} finally {
			jdao.close(conn);
		}
	}

	/**
	 * 仅适用于mysql ，插入并返回主鍵ID,调用的是mysql LAST_INSERT_ID() 函数；
	 * 注意：该方法沒有调用了jdaoHandler的close()方法。 仅适用于jdaoHandler实例中使用的是同一个connnect的情况。
	 * 
	 * @return int
	 * @throws SQLException
	 */
	public int saveAndGetLastInsertIdNotCloseConnect4MYSQL() throws SQLException {
		return saveAndGetLastInsertId(jdao.getConnection());
	}

	private int saveAndGetLastInsertId(Connection conn) throws SQLException {
		SqlKV kv = save_();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(kv.getSql());
			Object[] values = kv.getArgs();
			for (int i = 1; i <= values.length; i++) {
				ps.setObject(i, values[i - 1]);
			}
			ps.executeUpdate();
			return new QueryDao(conn, "select LAST_INSERT_ID() id").field2Int("id");
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	private SqlKV save_() {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		if (commentLine != null) {
			sb1.append("/* ").append(commentLine).append(" */");
		}
		sb1.append("insert into " + TABLENAME + "(");
		sb2.append(")values(");
		int i = 1;
		int length = fieldValueMap.size();
		Object[] values = new Object[length];
		for (Fields hf : fieldValueMap.keySet()) {
			values[i - 1] = fieldValueMap.get(hf);
			sb1.append(hf.getFieldName());
			sb2.append("?");
			if (i < length) {
				sb1.append(",");
				sb2.append(",");
			}
			i++;
		}
		sb1.append(sb2.toString()).append(")");
		String sql = sb1.toString();
		if (this.isloggerOn) {
			StringBuilder s = new StringBuilder();
			for (Object o : values) {
				s.append(o).append(",");
			}
			logger.log("[INSERT SQL][" + sql + "][" + s.toString().substring(0, s.length() - 1) + "]");
		}
		SqlKV skv = new SqlKV(sql, values);
		return skv;
	}

	/***
	 * 加入一组参数到批处理对象中
	 * 
	 */
	public void addBatch() {
		if (mBatch.size() == 0) {
			for (Fields f : fieldValueMap.keySet()) {
				List<Object> list = new ArrayList<Object>();
				list.add(fieldValueMap.get(f));
				mBatch.put(f, list);
			}
		} else {
			for (Fields f : mBatch.keySet()) {
				mBatch.get(f).add(fieldValueMap.get(f));
			}
		}
	}

	/**
	 * 批量插入
	 * 
	 * @return 插入批处理影响的条数数组
	 * @throws SQLException
	 */
	public int[] batchForSave() throws SQLException {
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		if (commentLine != null) {
			sb1.append("/* ").append(commentLine).append(" */");
		}
		sb1.append("insert into " + TABLENAME + "(");
		sb2.append(")values(");
		int length = mBatch.get(mBatch.keySet().iterator().next()).size();
		int lengthfield = mBatch.size();
		int i = 1;
		List<Fields> listfields = new ArrayList<Fields>();
		for (Fields f : mBatch.keySet()) {
			listfields.add(f);
			sb1.append(f.getFieldName());
			sb2.append("?");
			if (i < lengthfield) {
				sb1.append(",");
				sb2.append(",");
			}
			i++;
		}
		sb1.append(sb2.toString()).append(")");
		List<Object[]> list = new ArrayList<Object[]>();
		for (int k = 0; k < length; k++) {
			Object[] o = new Object[listfields.size()];
			for (int j = 0; j < lengthfield; j++) {
				o[j] = mBatch.get(listfields.get(j)).get(k);
			}
			list.add(o);
		}

		return jdao.executeBatch(sb1.toString(), list);
	}

	/**
	 * 更新操作 同sql中update
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int update() throws SQLException {
		StringBuilder sb = new StringBuilder();
		if (commentLine != null) {
			sb.append("/* ").append(commentLine).append(" */");
		}
		sb.append("update " + TABLENAME + " set ");
		int i = 1;
		List<Object> list = new ArrayList<Object>();
		for (Fields hf : fieldValueMap.keySet()) {
			list.add(fieldValueMap.get(hf));
			sb.append(hf.getFieldName()).append("=?");
			if (i < fieldValueMap.size()) {
				sb.append(",");
			}
			i++;
		}
		if (whereMap.size() > 0) {
			StringBuilder sbWhere = new StringBuilder();
			for (String str : whereMap.keySet()) {
				sbWhere.append(str);
				Object o = whereMap.get(str);
				if (o != null)
					if (o instanceof Array) {
						getArrayObj(list, (Array) o);
					} else {
						list.add(o);
					}
			}
			sb.append(sbWhere.toString().replaceFirst(AND, " where "));
		}
		String sql = sb.toString();
		Object[] values = new Object[list.size()];
		list.toArray(values);
		if (this.isloggerOn) {
			StringBuilder s = new StringBuilder();
			for (Object o : values) {
				s.append(o).append(",");
			}
			logger.log("[UPDATE SQL][" + sql + "][" + s.toString().substring(0, s.length() - 1) + "]");
		}
		return jdao.executeUpdate(sql, list.toArray(values));
	}

	/**
	 * 删除操作 同sql中delete
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int delete() throws SQLException {
		StringBuilder sb = new StringBuilder();
		if (commentLine != null) {
			sb.append("/* ").append(commentLine).append(" */");
		}
		sb.append("delete from " + TABLENAME + "");
		int length = whereMap.size();
		if (length > 0) {
			StringBuilder sbWhere = new StringBuilder();
			// int i = 1;
			List<Object> list = new ArrayList<Object>();
			for (String str : whereMap.keySet()) {
				sbWhere.append(str);
				Object o = whereMap.get(str);
				if (o != null)
					if (o instanceof Array) {
						getArrayObj(list, (Array) o);
					} else {
						list.add(o);
					}
				// if (i < length) {
				// sbWhere.append(",");
				// }
				// i++;
			}
			sb.append(sbWhere.toString().replaceFirst(AND, " where "));
			String sql = sb.toString();
			Object[] values = new Object[list.size()];
			list.toArray(values);
			if (this.isloggerOn) {
				StringBuilder s = new StringBuilder();
				for (Object o : values) {
					s.append(o).append(",");
				}
				logger.log("qurey sql[" + sql + "][" + s.toString().substring(0, s.length() - 1) + "]");
			}

			return jdao.executeUpdate(sb.toString(), list.toArray(values));
		} else {
			logger.log("[DELETE SQL][" + sb.toString() + "]");
			return jdao.executeUpdate(sb.toString());
		}
	}

	/**
	 * 开启或关闭日志记录
	 * 
	 * @param on
	 */
	public void setLoggerOn(boolean on) {
		this.isloggerOn = on;
		if (on) {
			logger.isLog(on, clazz);
		}
	}

	public FieldFilter getFieldFilter() {
		return fieldFilter;
	}

	public void setFieldFilter(FieldFilter fieldFilter) {
		this.fieldFilter = fieldFilter;
		// this.fieldValueMap.setFieldFilter(fieldFilter);
	}

	/**
	 * 对象SQL操作缓存初始化
	 */
	public void clear() {
		whereMap.clear();
		havingMap.clear();
		sortStr = new StringBuilder();
		groupStr = new StringBuilder();
		limitStr = null;
		mBatch = new HashMap<Fields, List<Object>>();
		fieldValueMap = new MyMap<Fields, Object>(clazz);
		fieldFilter = null;
	}

	class MyMap<K, V> extends HashMap<K, V> {
		private Class<?> clazz;

		public MyMap(Class<?> t) {
			clazz = t;
		}

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		public V put(K key, V value) {

			if (value == null)
				return null;

			if (fieldFilter != null) {
				Object o = fieldFilter.process(((Fields) key), ((Fields) key).getFieldName(), value);
				return o == null ? null : super.put(key, (V) o);
			}

			if (DaoFactory.fieldMap.containsKey(clazz)) {
				FieldFilter ff = DaoFactory.fieldMap.get(clazz);
				Object o = ff.process(((Fields) key), ((Fields) key).getFieldName(), value);
				return o == null ? null : super.put(key, (V) o);
			}

			if (DaoFactory.getField() != null) {
				Object o = DaoFactory.getField().process(((Fields) key), ((Fields) key).getFieldName(), value);
				return o == null ? null : super.put(key, (V) o);
			}

			return super.put(key, value);
		}
	}

	public T useCache(String domain) {
		return useCache(true, domain, null);
	}

	public T useCache(String domain, String node) {
		return useCache(true, domain, node);
	}

	@SuppressWarnings("unchecked")
	private T useCache(boolean isCache, String domain, String node) {
		this.isCache = isCache;
		this.domain = domain;
		this.node = node;
		return (T) this;
	}

	/**
	 * SQL注释行内容设置
	 * 
	 * @param note
	 */
	public void setCommentLine(String commentLine) {
		this.commentLine = commentLine.matches(".{0,}\\*/.{0,}") ? null : commentLine;
	}
}
