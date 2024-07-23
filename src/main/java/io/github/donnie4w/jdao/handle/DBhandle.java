package io.github.donnie4w.jdao.handle;

import io.github.donnie4w.jdao.base.Params;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * author donnie4w <donnie4w@gmail.com>
 * <p>
 * DBhandle is the core interface for all sql operations
 * and provides all Jdao operation functions
 */
public interface DBhandle extends Basehandle {

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
    Transaction getTransaction() throws SQLException;

    JdbcHandle getJdbcHandle();

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
    DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException;

    /**
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException;

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
    <T> List<T> executeQueryList(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;


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
    <T> T executeQuery(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;

    /**
     * executeUpdate transactional operation
     *
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException;

    /**
     * Batch transactional operations
     *
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException, SQLException;


    /**
     * Store Procedure operations
     *
     * @param procedureName StoreProcedure Name
     * @param params
     * @return
     * @throws SQLException
     */
    Map<Integer, Object> executeCall(Transaction transaction, String procedureName, Params... params) throws SQLException;

}
