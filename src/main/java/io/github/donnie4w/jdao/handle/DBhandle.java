package io.github.donnie4w.jdao.handle;

import io.github.donnie4w.jdao.base.JStruct;
import io.github.donnie4w.jdao.base.Params;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * DBhandle is the core interface for all sql operations
 * and provides all Jdao operation functions
 */
public interface DBhandle extends Basehandle {

    /**
     * Gets the database type.
     *
     * @return the database type  e.g. MYSQL,  POSTGRESQL,  MARIADB,  SQLITE,  ORACLE,  SQLSERVER,  DB2
     */
    DBType getDBType();

    /**
     * Gets the current transaction.
     *
     * @return the transaction
     * @throws SQLException if there is an issue obtaining the transaction
     */
    Transaction getTransaction() throws SQLException;

    /**
     * Retrieves the JDBC handle associated with this handler.
     *
     * @return the JDBC handle
     */
    JdbcHandle getJdbcHandle();

    /**
     * executeQuery transactional operation and returns DataBean,
     * the first if there are multiple query results
     *
     * @param transaction the transaction to use
     * @param sql         the SQL query to execute
     * @param values      the values to bind to the query
     * @return the DataBean result
     * @throws JdaoException if there is an error executing the query
     * @throws SQLException  if there is an error obtaining the transaction
     */
    DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException;

    /**
     * executeQuery transactional operation
     *
     * @param transaction the transaction to use
     * @param sql         the SQL query to execute
     * @param values      the values to bind to the query
     * @return the DataBean list result
     * @throws JdaoException if there is an error executing the query
     * @throws SQLException  if there is an error obtaining the transaction
     */
    List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException;

    /**
     * executeQuery transactional operation and returns multiple pieces of data,
     * or all of them if there are multiple query results
     *
     * @param transaction the transaction to use
     * @param claz        the class of the objects to be created
     * @param sql         the SQL query to execute
     * @param values      the values to bind to the query
     * @param <T>         the type of the objects to be created
     * @return a list of results
     * @throws JdaoException      if there is an error executing the query
     * @throws JdaoClassException if there is an error with the class
     * @throws SQLException       if there is an error obtaining the transaction
     */
    <T> List<T> executeQueryList(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;


    /**
     * executeQuery Non-transactional operation and returns one piece of data,
     * the first if there are multiple query results
     *
     * @param <T>         the type of the object to be created
     * @param transaction the transaction to use
     * @param claz        the class of the object to be created
     * @param sql         the SQL query to execute
     * @param values      the values to bind to the query
     * @return the result
     * @throws JdaoException      if there is an error executing the query
     * @throws JdaoClassException if there is an error with the class
     * @throws SQLException       if there is an error obtaining the transaction
     */
    <T> T executeQuery(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;

    /**
     * executeUpdate transactional operation
     *
     * @param transaction the transaction to use
     * @param sql         the SQL update statement to execute
     * @param values      the values to bind to the update statement
     * @return the number of rows affected
     * @throws JdaoException if there is an error executing the update
     * @throws SQLException  if there is an error obtaining the transaction
     */
    int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException;

    /**
     * Executes a batch of update statements.
     *
     * @param transaction the transaction to use
     * @param sql         the SQL update statement to execute
     * @param values      a list of value arrays for each update statement
     * @return an array of update counts
     * @throws JdaoException if there is an error executing the updates
     * @throws SQLException  if there is an error obtaining the transaction
     */
    int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException, SQLException;


    /**
     * Executes a stored procedure call.
     *
     * @param transaction   the transaction to use
     * @param procedureName the name of the stored procedure to call
     * @param params        the parameters for the stored procedure
     * @return a map of result set columns to their values
     * @throws SQLException if there is an error executing the procedure call
     */
    Map<Integer, Object> executeCall(Transaction transaction, String procedureName, Params... params) throws SQLException;

    <T extends JStruct<T>> T newStruct(Class<T> clazz);

    <T extends JStruct<T>> T newStruct(Class<T> clazz,String tablename);
}
