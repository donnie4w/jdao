package io.github.donnie4w.jdao.handle;

import io.github.donnie4w.jdao.base.Params;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * author donnie4w <donnie4w@gmail.com>
 * <p>
 * Basehandle is the base interface for all sql operations
 * and provides all Jdao operation functions
 */
public interface Basehandle {

    /**
     * executeQuery Non-transactional operation and returns DataBean,
     * the first if there are multiple query results
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    DataBean executeQueryBean(String sql, Object... values) throws JdaoException, SQLException;

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException, SQLException;

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
    <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;

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
    <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;


    /**
     * executeUpdate Non-transactional operation
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int executeUpdate(String sql, Object... values) throws JdaoException, SQLException;

    /**
     * Batch Non-transactional operations
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    int[] executeBatch(String sql, List<Object[]> values) throws JdaoException, SQLException;


    /**
     * Store Procedure operations
     * @param procedureName  StoreProcedure Name
     * @param params
     * @return
     * @throws SQLException
     */
    Map<Integer, Object> executeCall(String procedureName, Params... params) throws SQLException;

}
