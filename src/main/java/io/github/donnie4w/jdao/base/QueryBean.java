/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import io.github.donnie4w.jdao.util.Utils;

/**
 * Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * date 2013-1-13
 * verion 1.0 用于查询结果集
 */
public class QueryBean implements BaseDao, Serializable {
    private static final long serialVersionUID = 1L;
    private String sql;
    private Object[] args;
    private List<QueryBean> queryDaoList = new ArrayList<QueryBean>();
    private AtomicInteger pos = new AtomicInteger(0);
    private Map<String, Class<?>> typeMap = null;
    private Map<String, Object> valueMap = null;
    private Object[] valueObjects = null;
    private final Object lock = new Object();
    private JdaoHandler jdao;

    private int totalcount;

    private transient Log log = Log.newInstance(true, QueryBean.class);

    public int getTotalcount() {
        return totalcount;
    }

    public QueryBean setTotalcount(int totalcount) {
        this.totalcount = totalcount;
        return this;
    }

    public QueryBean() {
    }

    public QueryBean(JdaoHandler jdao) {
        this.jdao = jdao;
    }

    public DataSource getDataSource() {
        return this.jdao.getDataSource();
    }

    public QueryBean PageTurn(String sql, int start, int rowNumber, Object... objects) throws JException {
        try {
            List<Map<String, Object>> list = queryForMaps(getjh(), "select count(1) c from (" + sql + ") A ", objects);
            this.sql = sql + " limit " + start + " , " + rowNumber;
            return new QueryBean(getjh(), this.sql, objects).setTotalcount(Integer.parseInt(String.valueOf((list.get(0).get("c")))));
        } catch (Exception e) {
            throw new JException(e);
        }
    }

    public QueryBean(JdaoHandler jdao, String sql, Object... objects) throws JException {
        this.sql = sql;
        this.args = objects;
        execute(jdao);
    }

    /**
     * 返回指定class 的集合类
     */
    public static <T> List<T> queryForBeens(JdaoHandler jdao, Class<T> clazz, String sql, Object... objects) throws JException {
        Connection con = null;
        try {
            con = jdao.getConnection();
            return execute4Been(con, clazz, sql, objects);
        } finally {
            jdao.close(con);
        }
    }

    public static <T> List<T> queryForBeens(Class<T> clazz, String sql, Object... objects) throws JException {
        Connection con = null;
        JdaoHandler jdao = DaoFactory.getJdaoHandler(clazz, null);
        try {
            con = jdao.getConnection();
            return execute4Been(con, clazz, sql, objects);
        } finally {
            if (jdao != null)
                jdao.close(con);
        }
    }

    /**
     * 返回Map 的集合类
     *
    */
    public static List<Map<String, Object>> queryForMaps(JdaoHandler jdao, String sql, Object... objects) throws JException {
        Connection con = null;
        try {
            con = jdao.getConnection();
            return execute4Maps(con, sql, objects);
        } finally {
            jdao.close(con);
        }
    }

    public QueryBean(JdaoHandler jdao, String sql) throws JException {
        this.sql = sql;
        execute(jdao);
    }

    public QueryBean(Connection conn, String sql, Object... objects) throws JException {
        this.sql = sql;
        this.args = objects;
        execute_(conn);
    }

    public void setJdaoHandler(JdaoHandler jdaoHandler) {
        this.jdao = jdaoHandler;
    }

    public void setLoggerOn(boolean b) {
        if (b) {
            log.log("[SELETE SQL][" + this.sql + "]" + (this.args == null || this.args.length == 0 ? "" : Arrays.toString(this.args)));
        }
    }

    public QueryBean(Connection conn, String sql) throws JException {
        this.sql = sql;
        execute_(conn);
    }

    public QueryBean(String sql, Object... objects) throws JException {
        this.sql = sql;
        this.args = objects;
        execute(getjh());
    }

    private JdaoHandler getjh() {
        return this.jdao == null ? DaoFactory.getJdaoHandler(QueryBean.class) : this.jdao;
    }

    private QueryBean(Map<String, Class<?>> typeMap, Map<String, Object> valueMap) {
        this.typeMap = typeMap;
        this.valueMap = valueMap;
    }

    private void execute(JdaoHandler jdao) throws JException {
        Connection con = null;
        try {
            con = jdao.getConnection();
            execute_(con);
        } finally {
            jdao.close(con);
        }
    }

    private void execute_(Connection conn) throws JException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            if (args != null) {
                for (int i = 1; i <= args.length; i++) {
                    ps.setObject(i, args[i - 1]);
                }
            }
            rs = ps.executeQuery();
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int count = meta.getColumnCount();
                typeMap = new HashMap<String, Class<?>>();
                for (int i = 1; i <= count; i++) {
                    typeMap.put(fieldFormat(meta.getColumnLabel(i)), type2javaObj(meta.getColumnType(i)));
                }
                boolean isInit = false;
                while (rs.next()) {
                    if (!isInit) {
                        valueMap = new LinkedHashMap<String, Object>();
                        for (int i = 1; i <= count; i++) {
                            valueMap.put(fieldFormat(meta.getColumnLabel(i)), rs.getObject(i));
                            isInit = true;
                        }
                        queryDaoList.add(this);
                    } else {
                        Map<String, Object> vm = new LinkedHashMap<String, Object>();
                        for (int i = 1; i <= count; i++) {
                            vm.put(fieldFormat(meta.getColumnLabel(i)), rs.getObject(i));
                        }
                        queryDaoList.add(new QueryBean(typeMap, vm));
                    }
                }
            }
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(rs, ps);
        }
    }

    private static <T> List<T> execute4Been(Connection conn, Class<T> clazz, String sql, Object... objects) throws JException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<T>();
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= objects.length; i++) {
                ps.setObject(i, objects[i - 1]);
            }
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int columncount = rs.getMetaData().getColumnCount();
                    T object = clazz.getConstructor().newInstance();
                    for (int i = 1; i <= columncount; i++) {
                        String columnName = rs.getMetaData().getColumnLabel(i);
                        String firstLetter = columnName.substring(0, 1).toUpperCase();
                        String setMethodName = "set" + firstLetter + columnName.substring(1);
                        Method setMethod = clazz.getMethod(setMethodName, new Class[]{clazz.getDeclaredField(columnName).getType()});
                        if (rs.getObject(i) != null) {
                            setMethod.invoke(object, rs.getObject(i));
                        }
                    }
                    list.add(object);
                }
            }
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(rs, ps);
        }
        return list;
    }

    private static List<Map<String, Object>> execute4Maps(Connection conn, String sql, Object... objects) throws JException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= objects.length; i++) {
                ps.setObject(i, objects[i - 1]);
            }
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int columncount = rs.getMetaData().getColumnCount();
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (int i = 1; i <= columncount; i++) {
                        String columnName = rs.getMetaData().getColumnLabel(i);
                        map.put(columnName, rs.getObject(i));
                    }
                    list.add(map);
                }
            }
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(rs, ps);
        }
        return list;
    }



    private String fieldFormat(String filedName) {
        if (filedName != null)
            filedName = filedName.toLowerCase();
        return filedName;
    }

    /**
     * 返回字段类型 return the field's java type
     */
    public Class<?> fieldType(String field) {
        return typeMap != null ? typeMap.get(fieldFormat(field)) : null;
    }

    /**
     * 根据字段名field 返回字段的值 return the value of field
     */
    public Object fieldValue(String field) {
        return valueMap != null ? valueMap.get(fieldFormat(field)) : null;
    }

    /**
     * 根据字段查询先后的索引值index返回字段的值 return the value of field
     */
    public Object fieldValue(int index) {
        if (valueMap == null || index > valueMap.size()) {
            return null;
        }
        if (valueObjects == null)
            initValueObjects();
        return valueObjects[index - 1];
    }

    private void initValueObjects() {
        synchronized (lock) {
            if (valueObjects == null && valueMap != null) {
                valueObjects = new Object[valueMap.size()];
                int i = 0;
                for (String s : valueMap.keySet()) {
                    valueObjects[i] = valueMap.get(s);
                    ++i;
                }
            }
        }
    }

    /**
     * 将字段值转换为整型 field's value to int
     */
    public int field2Int(String field) {
        if (valueMap != null) {
            return Integer.parseInt(String.valueOf(valueMap.get(fieldFormat(field))));
        } else {
            throw new JRuntimeException("field2Int error");
        }
    }

    /**
     * 将字段值转换为字符串 field's value to String
     */
    public String field2String(String field) {
        if (valueMap != null) {
            return String.valueOf(valueMap.get(fieldFormat(field)));
        } else {
            throw new JRuntimeException("field2String error");
        }
    }

    /**
     * 将字段值转换为BigDecimal类型 field's value to BigDecimal
     */
    public BigDecimal field2BigDecimal(String field) {
        if (valueMap != null)
            return new BigDecimal(String.valueOf(valueMap.get(fieldFormat(field))));
        else
            throw new JRuntimeException("field2BigDecimal error");
    }

    /**
     * 将字段值转换为BigDecimal类型 field's value to BigDecimal
     */
    public BigDecimal field2BigDecimal(int idx) {
        if (valueMap != null)
            return new BigDecimal(String.valueOf(fieldValue(idx)));
        else
            throw new JRuntimeException("field2BigDecimal error");
    }

    /**
     *  查询字段 查询日期类型字段值，格式化后返回Date类型 format the Date value and return Date type
     */
    public Date field2Date(String field, String format) throws ParseException {
        if (valueMap != null)
            return Util.dateFormat((Date) (valueMap.get(fieldFormat(field))), format);
        else
            throw new JRuntimeException("field2Date error");
    }

    /**
     * 查询字段 查询日期类型字段值，格式化后返回String类型 format the Date value and return String type
     */
    public String field2DateString(String field, String format) throws ParseException {
        if (valueMap != null)
            return Util.date2String((Date) (valueMap.get(fieldFormat(field))), format);
        else
            throw new JRuntimeException("field2DateString error");
    }

    /**
     *
     * 返回值转换为整型
     */
    public int toInt() {
        return Integer.parseInt(String.valueOf(valueMap.get(valueMap.keySet().iterator().next())));
    }

    /**
     * returns the number of elements in this result
     */
    public int size() {
        return queryDaoList.size();
    }

    /**
     * Returns true if has more elements.
     */
    public boolean hasNext() {
        if (size() - pos.get() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the next element.
     */
    public QueryBean next() {
        if (pos.get() < queryDaoList.size()) {
            return queryDaoList.get(pos.getAndIncrement());
        } else {
            return null;
        }
    }

    /**
     * Flips this QueryDao. The limit is set to the current position and then the position is set to zero.
     */
    public void flip() {
        pos.set(0);
    }

    private static Class<?> type2javaObj(int javaType) {
        switch (javaType) {
            case Types.DECIMAL:
                return java.math.BigDecimal.class;
            case Types.NUMERIC:
                return java.math.BigDecimal.class;
            case Types.SMALLINT:
                return int.class;
            case Types.TINYINT:
                return int.class;
            case Types.INTEGER:
                return int.class;
            case Types.DOUBLE:
                return java.math.BigDecimal.class;
            case Types.FLOAT:
                return java.math.BigDecimal.class;
            case Types.REAL:
                return java.math.BigDecimal.class;
            case Types.TIMESTAMP:
                return Date.class;
            case Types.BIGINT:
                return java.math.BigDecimal.class;
            case Types.DATE:
                return Date.class;
            case Types.TIME:
                return Date.class;
            case Types.CHAR:
                return String.class;
            case Types.LONGVARBINARY:
                return byte[].class;
            case Types.VARCHAR:
                return String.class;
            case Types.BINARY:
                return byte[].class;
            case Types.VARBINARY:
                return byte[].class;
            case Types.LONGNVARCHAR:
                return String.class;
            case Types.BIT:
                return boolean.class;
            default:
                return String.class;
        }
    }

    public List<QueryBean> queryDaoList() {
        return queryDaoList;
    }

}
