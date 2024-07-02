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
package io.github.donnie4w.jdao.base;

import io.github.donnie4w.jdao.dbHandler.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Jdao {
    public static final String VERTION = "2.0.1";

    private Jdao(){
    }

    private static final Map<Object, DBhandle> dbhandleMap = new ConcurrentHashMap<>();

    private static DBhandle defaultDBhandle;

    public static DBhandle newDBhandle(DataSource dataSource, DBType dbtype) {
        return new DBhandler(dataSource, dbtype);
    }

    public static void initDataSource(DataSource dataSource, DBType dbtype) {
        defaultDBhandle = newDBhandle(dataSource, dbtype);
    }

    public static void setDataSource(Class<?> clz, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(clz, newDBhandle(dataSource, dbtype));
    }

    public static void removeDataSource(Class<?> clz) {
        dbhandleMap.remove(clz);
    }

    public static void setDataSource(String packageName, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(packageName, newDBhandle(dataSource, dbtype));
    }

    public static void removeDataSource(String packageName) {
        dbhandleMap.remove(packageName);
    }

    public static void addSlaveDataSource(Class<?> clz, DataSource dataSource, DBType dbtype) {
        SlaveSource.add(clz, dataSource, dbtype);
    }

    public static void removeSlaveDataSource(Class<?> clz) {
        SlaveSource.remove(clz);
    }

    public static void addSlaveDataSource(String packageName, DataSource dataSource, DBType dbtype) {
        SlaveSource.add(packageName
                , dataSource, dbtype);
    }

    public static void removeSlaveDataSource(String packageName) {
        SlaveSource.remove(packageName);
    }

    public static Transaction getTransaction() throws JdaoException {
        return defaultDBhandle.getJdbcHandle().getTransaction();
    }

    public static DBhandle getDefaultDBhandle() {
        return defaultDBhandle;
    }

    static DBhandle getDBhandle(Class<?> clz, String packageName, boolean queryType) {
        DBhandle dbhandle = null;

        if (SlaveSource.size() > 0 && queryType) {
            dbhandle = SlaveSource.get(clz, packageName);
            if (dbhandle != null) {
                return dbhandle;
            }
        }

        dbhandle = dbhandleMap.get(clz);
        if (dbhandle == null) {
            dbhandle = dbhandleMap.get(packageName);
        }
        if (dbhandle == null) {
            dbhandle = defaultDBhandle;
        }
        return dbhandle;
    }

    public static Cache getCache() {
        return CacheBeen.newInstance();
    }

    public static <T> T executeQueryScan(Class<T> clz, String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryScan(null, clz, sql, values);
    }

    public static <T> T executeQueryScan(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryScan(transaction, clz, sql, values);
    }

    public static <T> List<T> executeQueryScanList(Class<T> clz, String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryScanList(null, clz, sql, values);
    }

    public static <T> List<T> executeQueryScanList(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryScanList(transaction, clz, sql, values);
    }

    public static DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryBean(transaction, sql, values);
    }

    public static DataBean executeQueryBean(String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryBean(null, sql, values);
    }

    public static List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryBeans(transaction, sql, values);
    }

    public static List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeQueryBeans(null, sql, values);
    }

    public static int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeUpdate(transaction, sql, values);
    }

    public static int executeUpdate(String sql, Object... values) throws JdaoException {
        return defaultDBhandle.executeUpdate(null, sql, values);
    }

    public static int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException {
        return defaultDBhandle.executeBatch(transaction, sql, values);
    }

    public static int[] executeBatch(String sql, List<Object[]> values) throws JdaoException {
        return defaultDBhandle.executeBatch(null, sql, values);
    }

}