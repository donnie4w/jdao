package io.github.donnie4w.jdao.handle;

import java.util.List;

/**
 * author donnie4w <donnie4w@gmail.com>
 * <p>
 * DBhandle is the core interface for all sql operations
 * and provides all Jdao operation functions
 */
public interface DBhandle {

    /**
     * return DB type e.g. MYSQL,  POSTGRESQL,  MARIADB,  SQLITE,  ORACLE,  SQLSERVER,  DB2
     *
     * @return
     */
    DBType getDBType();

    /**
     * return Transaction
     *
     * @return
     * @throws JdaoException
     */
    Transaction getTransaction() throws JdaoException;

    JdbcHandle getJdbcHandle();

//    /**
//     * @param transaction
//     * @param clz
//     * @param sql
//     * @param values
//     * @return
//     * @param <T>
//     * @throws JdaoException
//     */
//    public <T> List<T> executeQueryScanList(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException;
//
//    /**
//     * @param clz
//     * @param sql
//     * @param values
//     * @return
//     * @param <T>
//     * @throws JdaoException
//     */
//    public <T> List<T> executeQueryScanList(Class<T> clz, String sql, Object... values) throws JdaoException;
//
//    /**
//     *
//     * @param transaction
//     * @param clz
//     * @param sql
//     * @param values
//     * @return
//     * @param <T>
//     * @throws JdaoException
//     */
//    public <T> T executeQueryScan(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException;
//
//    /**
//     * @param clz
//     * @param sql
//     * @param values
//     * @return
//     * @param <T>
//     * @throws JdaoException
//     */
//    public <T> T executeQueryScan(Class<T> clz, String sql, Object... values) throws JdaoException;

    /**
     * executeQuery transactional operation and returns DataBean,
     * the first if there are multiple query results
     *
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException;

    /**
     * executeQuery Non-transactional operation and returns DataBean,
     * the first if there are multiple query results
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    DataBean executeQueryBean(String sql, Object... values) throws JdaoException;

    /**
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException;

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException;

    /**
     * executeQuery transactional operation and returns multiple pieces of data,
     * or all of them if there are multiple query results
     *
     * @param transaction
     * @param claz
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws JdaoException
     */
    <T> List<T> executeQueryList(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException;

    /**
     * executeQuery Non-transactional operation and returns multiple pieces of data,
     * or all of them if there are multiple query results
     *
     * @param claz
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws JdaoException
     */
    <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException;

    /**
     * executeQuery Non-transactional operation and returns one piece of data,
     * the first if there are multiple query results
     *
     * @param transaction
     * @param claz
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws JdaoException
     */
    <T> T executeQuery(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException;

    /**
     * executeQuery Non-transactional operation and returns one piece of data,
     * the first if there are multiple query results
     *
     * @param claz
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws JdaoException
     */
    <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException;

    /**
     * executeUpdate transactional operation
     *
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException;

    /**
     * executeUpdate Non-transactional operation
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int executeUpdate(String sql, Object... values) throws JdaoException;

    /**
     * Batch transactional operations
     *
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException;

    /**
     * Batch Non-transactional operations
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int[] executeBatch(String sql, List<Object[]> values) throws JdaoException;
}
