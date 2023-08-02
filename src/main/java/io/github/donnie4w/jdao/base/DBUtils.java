/**
 * https://github.com/donnie4w/jdao
 * Copyright jdao Author. All Rights Reserved.
 * Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

/**
 * @File:jdao: com.jdao.base :DBUtils.java
 * @Date:2017年10月23日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: dong
 * @Desc: CRUD 超级类
 */
public class DBUtils<T extends DBUtils<T>> implements Cloneable {

    QueryBean qd;
    List<T> list;
    int totalCount;
    Transaction transaction;

    public List<T> rsList() {
        return list;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public DBUtils() {}

    public Transaction setTransaction(Transaction transaction) {
        this.transaction = transaction;
        return transaction;
    }

    public void removeTransaction() {
        this.transaction = null;
    }

    public void select(String sql, Object... objects) throws JException {
        qd = new QueryBean(getjh(), sql, objects);
    }

    public DataSource getDataSource() {
        return qd.getDataSource();
    }

    @SuppressWarnings("unchecked")
    public T selectList(String sql, Object... objects) throws JException {
        try {
            QueryBean qd = new QueryBean(getjh(), sql, objects);
            if (qd != null) {
                List<T> list = new ArrayList<T>();
                for (QueryBean qdao : qd.queryDaoList()) {
                    T t = (T) clone();
                    t.qd = qdao;
                    list.add(t);
                }
                this.totalCount = qd.getTotalcount();
                this.qd = qd;
                this.list = list;
            }
            return (T) this;
        } catch (Exception e) {
            throw new JException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public T selectListPage(int rowStart, int scanRows, String sql, Object... objects) throws JException {
        try {
            QueryBean qd = new QueryBean(getjh()).PageTurn(sql, rowStart, scanRows, objects);
            if (qd != null) {
                List<T> list = new ArrayList<T>();
                for (QueryBean qdao : qd.queryDaoList()) {
                    T t = (T) clone();
                    t.qd = qdao;
                    list.add(t);
                }
                this.qd = qd;
                this.list = list;
                this.totalCount = qd.getTotalcount();
            }
            return (T) this;
        } catch (Exception e) {
            throw new JException(e);
        }
    }

    public int getTotalCount() throws JException {
        return qd.getTotalcount();
    }

    public String getString(String name) throws JException {
        return qd.field2String(name);
    }

    public String getString(int idx) throws JException {
        return String.valueOf(qd.fieldValue(idx));
    }

    public long getLong(String name) throws JException {
        return Long.parseLong(getString(name));
    }

    public long getLong(int idx) throws JException {
        return Long.parseLong(getString(idx));
    }

    public double getDouble(String name) throws JException {
        return Double.parseDouble(getString(name));
    }

    public double getDouble(int idx) throws JException {
        return Double.parseDouble(getString(idx));
    }

    public Date getDateFormat(String name, String format) throws JException {
        try {
            return qd.field2Date(name, format);
        } catch (Exception e) {
            throw new JException(e);
        }
    }

    public BigDecimal getBigDecimal(String name) throws JException {
        return qd.field2BigDecimal(name);
    }

    public BigDecimal getBigDecimal(int idx) throws JException {
        return qd.field2BigDecimal(idx);
    }

    public Object getObject(String name) throws JException {
        return qd.fieldValue(name);
    }

    public Object getObject(int idx) throws JException {
        return qd.fieldValue(idx);
    }

    public JdaoHandler getJdaoHandler() {
        return getjh();
    }

    private JdaoHandler getjh() {
        JdaoHandler jh = null;
        if (transaction == null) {
            jh = DaoFactory.getJdaoHandler(this.getClass(), this.getClass().getPackage().getName());
        } else {
            jh = transaction.getJdaoHandler();
        }
        return jh;
    }

    public int execute(String sql, Object... objects) throws JException {
        if (objects == null) {
            return getjh().executeUpdate(sql);
        } else {
            return getjh().executeUpdate(sql, objects);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
