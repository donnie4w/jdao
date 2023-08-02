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
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-3-7
 * @verion 1.0.3
 */
public class JdaoHandlerImplSingleTon2 implements JdaoHandler {
    private static final long serialVersionUID = 1L;
    private DataSource dataSource;
    private boolean transaction = false;
    private Connection conn;

    public JdaoHandlerImplSingleTon2(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }


    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void close() throws JException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new JException(e);
            }
            conn = null;
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
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new JException(e);
            }
        }
    }

    @Override
    public int[] executeBatch(String sql, List<Object[]> list) throws JException {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            ps = getConnection().prepareStatement(sql);
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
            Utils.close(ps);
            if (!transaction)
                close(conn);
        }
    }

    @Override
    public QueryBean executeQuery(String sql) throws JException {
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            return new QueryBean(conn, sql);
        } finally {
            close(conn);
        }
    }

    @Override
    public QueryBean executeQuery(String sql, Object... values) throws JException {
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            return new QueryBean(conn, sql, values);
        } finally {
            if (!transaction)
                close(conn);
        }
    }

    @Override
    public <T extends Table<?>> List<T> executeQuery(String sql, Class<T> claz) throws JException {
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            return JdaoUtil.selectDaos(conn, sql, claz, null);
        } finally {
            if (!transaction)
                close(conn);
        }
    }

    @Override
    public <T extends Table<?>> List<T> executeQuery(Class<T> claz, String sql, Object... values) throws JException {
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            return JdaoUtil.selectDaos(conn, sql, claz, values);
        } finally {
            if (!transaction)
                close(conn);
        }
    }

    @Override
    public int executeUpdate(String sql, Object... values) throws JException {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= values.length; i++) {
                ps.setObject(i, values[i - 1]);
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(ps);
            if (!transaction)
                close(conn);
        }
    }

    @Override
    public int executeUpdate(String sql) throws JException {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            ps = conn.prepareStatement(sql);
            return ps.executeUpdate();
        }  catch (Exception e) {
            throw new JException(e);
        } finally {
            Utils.close(ps);
            if (!transaction)
                close(conn);
        }
    }

    @Override
    public void rollBack() throws JException {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                throw new JException(e);
            }
        }
    }

    @Override
    public boolean setAutoCommit(boolean auto) throws JException {
        if (auto) {
            transaction = false;
        } else {
            conn = conn == null ? getConnection() : conn;
            transaction = true;
        }
        return true;
    }

    @Override
    public <T extends Table<?>> T executeQueryById(Class<T> claz, String sql, Object... values) throws JException {
        Connection conn = null;
        try {
            conn = conn == null ? getConnection() : conn;
            return JdaoUtil.selectDao(conn, sql, claz, values);
        } finally {
            if (!transaction)
                close(conn);
        }
    }
}
