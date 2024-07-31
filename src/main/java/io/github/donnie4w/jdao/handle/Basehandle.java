package io.github.donnie4w.jdao.handle;

import io.github.donnie4w.jdao.base.Params;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Basehandle is the base interface for all sql operations
 * and provides all Jdao operation functions
 */
public interface Basehandle {

    /**
     * executeQuery Non-transactional operation and returns DataBean,
     * the first if there are multiple query results
     *
     * @param sql the SQL query statement.
     * @param values the parameter values for the SQL query.
     * @return the DataBean result of the query.
     * @throws JdaoException if a JDAO related exception occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    DataBean executeQueryBean(String sql, Object... values) throws JdaoException, SQLException;

    /**
     * Executes a SQL query and returns a list of DataBean objects.
     *
     * @param sql the SQL query statement.
     * @param values the parameter values for the SQL query.
     * @return a List of DataBean objects.
     * @throws JdaoException if a JDAO related exception occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException, SQLException;

    /**
     * executeQuery Non-transactional operation and returns multiple pieces of data,
     * or all of them if there are multiple query results
     *
     * @param <T> the type of the elements in the returned list.
     * @param claz the Class object that represents the type of the elements.
     * @param sql the SQL query statement.
     * @param values the parameter values for the SQL query.
     * @return a List of objects of the specified type as a result of the query.
     * @throws JdaoException if a JDAO related exception occurs.
     * @throws JdaoClassException if a class related exception occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;

    /**
     * executeQuery Non-transactional operation and returns one piece of data,
     * the first if there are multiple query results
     *
     * @param <T> the type of the result.
     * @param claz the Class object that represents the type of the result.
     * @param sql the SQL query statement.
     * @param values the parameter values for the SQL query.
     * @return the result of the query.
     * @throws JdaoException if a JDAO related exception occurs.
     * @throws JdaoClassException if a class related exception occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;


    /**
     * executeUpdate Non-transactional operation
     *
     * @param sql the SQL update statement.
     * @param values the parameter values for the SQL update.
     * @return the number of rows affected by the update.
     * @throws JdaoException if a JDAO related exception occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    int executeUpdate(String sql, Object... values) throws JdaoException, SQLException;

    /**
     * Batch Non-transactional operations
     *
     * @param sql the SQL update statement.
     * @param values a list of arrays containing parameter values for each batch update.
     * @return an array of update counts, one for each command in the batch.
     * @throws JdaoException if a JDAO related exception occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    int[] executeBatch(String sql, List<Object[]> values) throws JdaoException, SQLException;


    /**
     * Executes a stored procedure and returns the output parameters.
     *
     * @param procedureName the name of the stored procedure to execute.
     * @param params the input and output parameters for the stored procedure.
     * @return a map containing the output parameter values.
     * @throws SQLException if a SQL related exception occurs.
     */
    Map<Integer, Object> executeCall(String procedureName, Params... params) throws SQLException;

}
