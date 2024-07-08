package io.github.donnie4w.jdao.handle;

import io.github.donnie4w.jdao.base.Table;

import java.util.List;

public interface DBhandle {
    public DBType getDBType();

    public Transaction  getTransaction() throws JdaoException;;

    public JdbcHandle getJdbcHandle();

    public <T> List<T> executeQueryScanList(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException;

    public <T> List<T> executeQueryScanList(Class<T> clz, String sql, Object... values) throws JdaoException;

    public <T> T executeQueryScan(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException;

    public <T> T executeQueryScan(Class<T> clz, String sql, Object... values) throws JdaoException;

    public DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException;

    public DataBean executeQueryBean(String sql, Object... values) throws JdaoException;

    public List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException;

    public List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException;

    public <T extends Table<?>> List<T> executeQueryList(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException;

    public <T extends Table<?>> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException;

    public <T extends Table<?>> T executeQuery(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException;

    public <T extends Table<?>> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException;

    public int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException;

    public int executeUpdate(String sql, Object... values) throws JdaoException;

    public int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException;

    public int[] executeBatch(String sql, List<Object[]> values) throws JdaoException;
}
