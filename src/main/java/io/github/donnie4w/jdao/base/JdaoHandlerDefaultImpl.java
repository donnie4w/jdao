/**
 *  https://github.com/donnie4w/jdao
 *  Copyright jdao Author. All Rights Reserved.
 *  Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import io.github.donnie4w.jdao.util.Utils;

/**
 * @File:jdao: com.jdao.dbHandlerImpl :JdaoHandlerDefaultImpl.java
 * @Date:2017年11月4日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: donnie4w
 * @Desc:
 */
public class JdaoHandlerDefaultImpl implements JdaoHandler {
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;
    private boolean transaction = false;
    private Connection _conn;

    public JdaoHandlerDefaultImpl(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        // conn = dataSource.getConnection();
        // conn.setAutoCommit(true);
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public Connection getConnection() {
        try {
            return getConn();
        } catch (JException e) {
            throw new JRuntimeException(e);
        }
    }

    @Override
    public void close() throws JException {
        if (_conn != null) {
            try {
                _conn.close();
                _conn = null;
            } catch (Exception e) {
                throw new JException(e);
            }
        }
    }

    @Override
    public void close(Connection con) throws JException {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                throw new JException(e);
            }
        }
    }

    @Override
    public void commit() throws JException {
        if (_conn != null) {
            try {
                _conn.commit();
            } catch (Exception e) {
                throw new JException(e);
            }
        }
    }

    synchronized Connection getConn() throws JException {
        if (this._conn != null) {
            return _conn;
        } else {
            try {
                Connection c = dataSource.getConnection();
                c.setAutoCommit(true);
                return c;
            } catch (Exception e) {
                throw new JException(e);
            }
        }
    }

    @Override
    public int[] executeBatch(String sql, List<Object[]> list) throws JException {
        PreparedStatement ps = null;
        Connection c = getConn();
        try {
            ps = c.prepareStatement(sql);
            for (Object[] args : list) {
                for (int i = 1; i <= args.length; i++) {
                    ps.setObject(i, args[i - 1]);
                }
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            close(ps);
            if (!transaction)
                close(c);
        }
    }

    @Override
    public QueryBean executeQuery(String sql) throws JException {
        Connection c = getConn();
        try {
            return new QueryBean(c, sql);
        } finally {
            if (!transaction)
                close(c);
        }
    }

    @Override
    public QueryBean executeQuery(String sql, Object... values) throws JException {
        Connection c = null;
        try {
            c = getConn();
            return new QueryBean(c, sql, values);
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            if (!transaction) {
                if (c != null)
                    close(c);
            }
        }
    }

    @Override
    public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws JException {
        Connection c = getConn();
        try {
            return JdaoUtil.selectDaos(c, sql, claz, null);
        } finally {
            if (!transaction)
                close(c);
        }
    }

    @Override
    public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws JException {
        Connection c = getConn();
        try {
            return JdaoUtil.selectDaos(c, sql, claz, values);
        } finally {
            if (!transaction)
                close(c);
        }
    }

    @Override
    public int executeUpdate(String sql, Object... values) throws JException {
        PreparedStatement ps = null;
        Connection c = getConn();
        try {
            ps = c.prepareStatement(sql);
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(ps);
            if (!transaction)
                close(c);
        }
    }

    @Override
    public int executeUpdate(String sql) throws JException {
        PreparedStatement ps = null;
        Connection c = getConn();
        try {
            ps = c.prepareStatement(sql);
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(ps);
            if (!transaction)
                close(c);
        }
    }

    @Override
    public void rollBack() throws JException {
        if (_conn != null)
            try {
                _conn.rollback();
            } catch (SQLException e) {
                throw new JException(e);
            }
    }

    @Override
    public synchronized boolean setAutoCommit(boolean auto) throws JException {
        try {
            if (auto) {
                transaction = false;
                close();
            } else {
                this._conn = getConnection();
                this._conn.setAutoCommit(false);
                transaction = true;
            }
            return true;
        } catch (Exception e) {
            throw new JException(e);
        }
    }

    @Override
    public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws JException {
        Connection c = null;
        try {
            c = getConn();
            return JdaoUtil.selectDao(c, sql, claz, values);
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            if (!transaction) {
                close(c);
            }
        }
    }

    private void close(AutoCloseable io) {
        try {
            if (io != null)
                io.close();
        } catch (Exception e) {
        }
    }
}
