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

import io.github.donnie4w.jdao.base.Params;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBhandler implements DBhandle {
    private final DBType dbType;
    private final JdbcHandle jdbcHandle;

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

    public <T> List<T> executeQueryScanList(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException, SQLException {
        List<DataBean> list = executeQueryBeans(transaction, sql, values);
        if (list != null) {
            List<T> results = new ArrayList<>();
            for (DataBean dataBean : list) {
                results.add(dataBean.scan(clz));
            }
            return results;
        }
        return null;
    }

    /**
     * @param sql
     * @param values
     * @return
     */
    public DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeQueryBean(sql, values);
        }
        return jdbcHandle.executeQueryBean(sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public DataBean executeQueryBean(String sql, Object... values) throws JdaoException, SQLException {
        return executeQueryBean(null, sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     */
    public List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeQueryBeans(sql, values);
        }
        return jdbcHandle.executeQueryBeans(sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException, SQLException {
        return executeQueryBeans(null, sql, values);
    }


    /**
     * auther donnie wu
     *
     * @param <T>
     * @param claz
     * @param sql
     * @param values
     */
    public <T> List<T> executeQueryList(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        if (transaction != null) {
            return transaction.executeQueryList(claz, sql, values);
        }
        return jdbcHandle.executeQueryList(claz, sql, values);
    }

    /**
     * @param claz
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws JdaoException
     */
    @Override
    public <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        return executeQueryList(null, claz, sql, values);
    }

    /**
     * auther donnie wu
     *
     * @param <T>
     * @param claz
     * @param sql
     * @param values
     */
    public <T> T executeQuery(Transaction transaction, Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        if (transaction != null) {
            return transaction.executeQuery(claz, sql, values);
        }
        return jdbcHandle.executeQuery(claz, sql, values);
    }

    /**
     * @param claz
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws JdaoException
     */
    @Override
    public <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        return jdbcHandle.executeQuery(claz, sql, values);
    }

    /**
     * auther donnie wu
     *
     * @param sql
     * @param values
     */
    public int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeUpdate(sql, values);
        }
        return jdbcHandle.executeUpdate(sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public int executeUpdate(String sql, Object... values) throws JdaoException, SQLException {
        return jdbcHandle.executeUpdate(sql, values);
    }

    /**
     * auther donnie wu
     *
     * @param sql
     * @param values
     */
    public int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException, SQLException {
        if (transaction != null) {
            return transaction.executeBatch(sql, values);
        }
        return jdbcHandle.executeBatch(sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public int[] executeBatch(String sql, List<Object[]> values) throws JdaoException, SQLException {
        return jdbcHandle.executeBatch(sql, values);
    }


    /**
     * @param procedureName
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<Integer, Object> executeCall(String procedureCallMethod, Params... params) throws SQLException {
        return jdbcHandle.executeCall(procedureCallMethod, params);
    }

    /**
     * Store Procedure operations
     *
     * @param procedureName StoreProcedure Name
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<Integer, Object> executeCall(Transaction transaction, String procedureName, Params... params) throws SQLException {
        if (transaction != null) {
            return transaction.executeCall(procedureName, params);
        }
        return jdbcHandle.executeCall(procedureName, params);
    }

}
