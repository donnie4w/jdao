/*
 * Copyright (c) 2024, donnie <donnie4w@gmail.com> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * github.com/donnie4w/jdao
 */

package io.github.donnie4w.jdao.handle;

import io.github.donnie4w.jdao.util.Logger;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: donnie4w <donnie4w@gmail.com>
 * <p>
 * This is the core class of Jdao, providing jdao data CRUD operation functions,
 * transaction, batch processing and other operation functions,
 * data source object DBhandle creation and acquisition,
 * multi-data source add and delete functions
 */
public class Jdao {

    final static String VERTION = "2.0.1";

    private final static String err_noinit = "the jdao DataSource was not initialized(Hint: jdao.init(dataSource, dbtype)) ";

    private Jdao() {
    }

    private static final Map<Object, DBhandle> dbhandleMap = new ConcurrentHashMap<>();

    private static DBhandle defaultDBhandle;

    /**
     * new DBhandle  by data source
     *
     * @param dataSource
     * @param dbtype
     * @return
     */
    public static DBhandle newDBhandle(DataSource dataSource, DBType dbtype) {
        return new DBhandler(dataSource, dbtype);
    }

    /**
     * jdao init DataSource and return DBhandle
     *
     * @param dataSource
     * @param dbtype
     */
    public static void init(DataSource dataSource, DBType dbtype) {
        defaultDBhandle = newDBhandle(dataSource, dbtype);
    }

    /**
     * Sets the data source of the class that will be used by the class operation
     *
     * @param clz
     * @param dataSource
     * @param dbtype
     */
    public static void setDataSource(Class<?> clz, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(clz, newDBhandle(dataSource, dbtype));
    }

    /**
     * Check whether the class has a set data source and return the data source
     *
     * @param clz
     * @return
     */
    public static DBhandle getDBhandle(Class<?> clz) {
        return dbhandleMap.get(clz);
    }

    /**
     * Check whether the package has a set data source and return the data source
     *
     * @param packageName
     * @return DBhandle
     */
    public static DBhandle getDBhandle(String packageName) {
        return dbhandleMap.get(packageName);
    }


    /**
     * remove DataSource by class
     *
     * @param clz
     */
    public static void removeDataSource(Class<?> clz) {
        dbhandleMap.remove(clz);
    }

    /**
     * For the package where the dao resides, set the data source,
     * and use the data source for the operations corresponding to the dao in the package
     *
     * @param packageName
     * @param dataSource
     * @param dbtype
     */
    public static void setDataSource(String packageName, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(packageName, newDBhandle(dataSource, dbtype));
    }


    /**
     * remove DataSource by packageName
     *
     * @param packageName
     */
    public static void removeDataSource(String packageName) {
        dbhandleMap.remove(packageName);
    }

    /**
     * Gets the new transaction operation object
     *
     * @return
     * @throws JdaoException
     */
    public static Transaction newTransaction() throws SQLException {
        return defaultDBhandle.getJdbcHandle().newTransaction();
    }

    /**
     * reture the default DBhandle , which set with function initDataSource
     *
     * @return
     */
    public static DBhandle getDefaultDBhandle() {
        return defaultDBhandle;
    }


    /**
     * select * from table
     *
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     */
    public static <T> T executeQuery(Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQuery(clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     */
    public static <T> T executeQuery(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQuery(transaction, clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     */
    public static <T> List<T> executeQueryList(Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryList(clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     */
    public static <T> List<T> executeQueryList(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryList(transaction, clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param sql
     * @param values
     * @return DataBean
     * @throws JdaoException
     */
    public static DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBean(transaction, sql, values);
    }

    /**
     * select * from table
     *
     * @param sql
     * @param values
     * @return DataBean
     * @throws JdaoException
     */
    public static DataBean executeQueryBean(String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBean(null, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param sql
     * @param values
     * @return List<DataBean>
     * @throws JdaoException
     */
    public static List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBeans(transaction, sql, values);
    }

    /**
     * select * from table
     *
     * @param sql
     * @param values
     * @return List<DataBean>
     * @throws JdaoException
     */
    public static List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBeans(null, sql, values);
    }

    /**
     * update  insert into  delete
     *
     * @param transaction
     * @param sql
     * @param values
     * @return int
     * @throws JdaoException
     */
    public static int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeUpdate(transaction, sql, values);
    }

    /**
     * update  insert into  delete
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static int executeUpdate(String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeUpdate(null, sql, values);
    }

    /**
     * insert into
     *
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeBatch(transaction, sql, values);
    }

    /**
     * insert into
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static int[] executeBatch(String sql, List<Object[]> values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeBatch(null, sql, values);
    }

    private static void notnull(Object obj, String message) {
        if (obj == null) {
            throw new JdaoRuntimeException(message);
        }
    }

    public static void setLogger(boolean on) {
        Logger.setLogger(on);
    }

}