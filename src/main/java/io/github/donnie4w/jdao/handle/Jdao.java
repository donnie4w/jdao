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

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Jdao {
    private final static String VERTION = "2.0.1";

    private final static String err_noinit = "the jdao DataSource was not initialized(Hint: jdao.InitDataSource(DataSource dataSource, DBType dbtype)) ";

    private Jdao() {}

    private static final Map<Object, DBhandle> dbhandleMap = new ConcurrentHashMap<>();

    private static DBhandle defaultDBhandle;

    /**
     * @param dataSource
     * @param dbtype
     * @return
     */
    public static DBhandle newDBhandle(DataSource dataSource, DBType dbtype) {
        return new DBhandler(dataSource, dbtype);
    }

    /**
     * jdao init DataSource
     * @param dataSource
     * @param dbtype
     */
    public static void initDataSource(DataSource dataSource, DBType dbtype) {
        defaultDBhandle = newDBhandle(dataSource, dbtype);
    }

    /**
     * @param clz
     * @param dataSource
     * @param dbtype
     */
    public static void setDataSource(Class<?> clz, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(clz, newDBhandle(dataSource, dbtype));
    }

    public static DBhandle getDBhandle(Class<?> clz) {
        return  dbhandleMap.get(clz);
    }

    /**
     * remove DataSource by class
     * @param clz
     */
    public static void removeDataSource(Class<?> clz) {
        dbhandleMap.remove(clz);
    }

    /**
     * @param packageName
     * @param dataSource
     * @param dbtype
     */
    public static void setDataSource(String packageName, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(packageName, newDBhandle(dataSource, dbtype));
    }

    public static DBhandle getDBhandle(String packageName) {
        return  dbhandleMap.get(packageName);
    }

    /**
     * remove DataSource by packageName
     * @param packageName
     */
    public static void removeDataSource(String packageName) {
        dbhandleMap.remove(packageName);
    }

    /**
     * add slave DataSource
     * @param clz
     * @param dataSource
     * @param dbtype
     */
    public static void addSlaveDataSource(Class<?> clz, DataSource dataSource, DBType dbtype) {
        SlaveSource.add(clz, dataSource, dbtype);
    }

    /**
     * remove slave DataSource by class
     * @param clz
     */
    public static void removeSlaveDataSource(Class<?> clz) {
        SlaveSource.remove(clz);
    }

    /**
     * @param packageName
     * @param dataSource
     * @param dbtype
     */
    public static void addSlaveDataSource(String packageName, DataSource dataSource, DBType dbtype) {
        SlaveSource.add(packageName
                , dataSource, dbtype);
    }

    /**
     * remove slave DataSource by packageName
     * @param packageName
     */
    public static void removeSlaveDataSource(String packageName) {
        SlaveSource.remove(packageName);
    }

    public static Transaction getTransaction() throws JdaoException {
        return defaultDBhandle.getJdbcHandle().getTransaction();
    }

    public static DBhandle getDefaultDBhandle() {
        return defaultDBhandle;
    }


    /**
     * select * from table
     * @param clz
     * @param sql
     * @param values
     * @return
     * @param <T>
     * @throws JdaoException
     */
    public static <T> T executeQueryScan(Class<T> clz, String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryScan(null, clz, sql, values);
    }

    /**
     * select * from table
     * @param transaction
     * @param clz
     * @param sql
     * @param values
     * @return
     * @param <T>
     * @throws JdaoException
     */
    public static <T> T executeQueryScan(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryScan(transaction, clz, sql, values);
    }

    /**
     * select * from table
     * @param clz
     * @param sql
     * @param values
     * @return
     * @param <T>
     * @throws JdaoException
     */
    public static <T> List<T> executeQueryScanList(Class<T> clz, String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryScanList(null, clz, sql, values);
    }

    /**
     * select * from table
     * @param transaction
     * @param clz
     * @param sql
     * @param values
     * @return
     * @param <T>
     * @throws JdaoException
     */
    public static <T> List<T> executeQueryScanList(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryScanList(transaction, clz, sql, values);
    }

    /**
     * select * from table
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBean(transaction, sql, values);
    }

    /**
     * select * from table
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static DataBean executeQueryBean(String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBean(null, sql, values);
    }

    /**
     * select * from table
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBeans(transaction, sql, values);
    }

    /**
     * select * from table
     * @param sql
     * @param values
     * @return List<DataBean>
     * @throws JdaoException
     */
    public static List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBeans(null, sql, values);
    }

    /**
     * update  insert into  delete
     * @param transaction
     * @param sql
     * @param values
     * @return int
     * @throws JdaoException
     */
    public static int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeUpdate(transaction, sql, values);
    }

    /**
     * update  insert into  delete
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static int executeUpdate(String sql, Object... values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeUpdate(null, sql, values);
    }

    /**
     * insert into
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeBatch(transaction, sql, values);
    }

    /**
     * insert into
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    public static int[] executeBatch(String sql, List<Object[]> values) throws JdaoException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeBatch(null, sql, values);
    }

    private static void notnull(Object obj, String message) {
        if (obj == null) {
            throw new JdaoRuntimeException(message);
        }
    }

}