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

import io.github.donnie4w.jdao.base.JStruct;
import io.github.donnie4w.jdao.base.Params;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the DBhandle interface for performing database operations.
 */
public class DBhandler implements DBhandle {
    private final DBType dbType;
    private final JdbcHandle jdbcHandle;

    /**
     * Constructs a new DBhandler instance.
     *
     * @param dataSource the DataSource used to obtain connections
     * @param dbType     the type of the database
     */
    public DBhandler(DataSource dataSource, DBType dbType) {
        this.dbType = dbType;
        this.jdbcHandle = JdbcHandler.getInstance(dataSource);
    }

    public DBType getDBType() {
        return dbType;
    }

    @Override
    public Transaction getTransaction() throws SQLException {
        return this.jdbcHandle.newTransaction();
    }

    public JdbcHandle getJdbcHandle() {
        return jdbcHandle;
    }

    public DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeQueryBean(sql, values);
        }
        return jdbcHandle.executeQueryBean(sql, values);
    }

    @Override
    public DataBean executeQueryBean(String sql, Object... values) throws JdaoException, SQLException {
        return executeQueryBean(null, sql, values);
    }

    public List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeQueryBeans(sql, values);
        }
        return jdbcHandle.executeQueryBeans(sql, values);
    }

    @Override
    public List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException, SQLException {
        return executeQueryBeans(null, sql, values);
    }

    public <T> List<T> executeQueryList(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        if (transaction != null) {
            return transaction.executeQueryList(claz, sql, values);
        }
        return jdbcHandle.executeQueryList(claz, sql, values);
    }

    @Override
    public <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        return executeQueryList(null, claz, sql, values);
    }

    public <T> T executeQuery(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        if (transaction != null) {
            return transaction.executeQuery(claz, sql, values);
        }
        return jdbcHandle.executeQuery(claz, sql, values);
    }

    @Override
    public <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        return jdbcHandle.executeQuery(claz, sql, values);
    }

    public int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeUpdate(sql, values);
        }
        return jdbcHandle.executeUpdate(sql, values);
    }

    @Override
    public int executeUpdate(String sql, Object... values) throws JdaoException, SQLException {
        return jdbcHandle.executeUpdate(sql, values);
    }

    public int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeBatch(sql, values);
        }
        return jdbcHandle.executeBatch(sql, values);
    }

    @Override
    public int[] executeBatch(String sql, List<Object[]> values) throws JdaoException, SQLException {
        return jdbcHandle.executeBatch(sql, values);
    }

    public Map<Integer, Object> executeCall(String procedureCallMethod, Params... params) throws SQLException {
        return jdbcHandle.executeCall(procedureCallMethod, params);
    }

    /**
     * Store Procedure operations
     */
    public Map<Integer, Object> executeCall(Transaction transaction, String procedureName, Params... params) throws SQLException {
        if (transaction != null) {
            return transaction.executeCall(procedureName, params);
        }
        return jdbcHandle.executeCall(procedureName, params);
    }


    public <T extends JStruct<T>> T newStruct(Class<T> clazz) {
        try {
            T t = clazz.getConstructor().newInstance();
            t.useDBhandle(this);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public <T extends JStruct<T>> T newStruct(Class<T> clazz, String tablename) {
        try {
            Constructor<T> constructor = findConstructorWithSingleStringParameter(clazz);
            if (constructor == null) {
                throw new NoSuchMethodException("No constructor with a single String parameter found in " + clazz.getName());
            }
            T t = constructor.newInstance(tablename);
            t.useDBhandle(this);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> Constructor<T> findConstructorWithSingleStringParameter(Class<T> clazz) {
        try {
            return clazz.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

}
