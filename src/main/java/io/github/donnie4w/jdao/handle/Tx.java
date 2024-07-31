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
import io.github.donnie4w.jdao.util.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Tx implements Transaction {

    Connection connection;
    DataSource dataSource;

    public Tx(Connection connection) throws SQLException {
        this.connection = connection;
        this.connection.setAutoCommit(false);
    }

    public Tx(DataSource dataSource) throws SQLException {
        this.dataSource = dataSource;
        this.connection = dataSource.getConnection();
        this.connection.setAutoCommit(false);
    }

    /**
     * The transaction commits and closes the transaction connection
     */
    public void commit() throws SQLException {
        this.connection.commit();
        this.close();
        if (Logger.isVaild()) {
            Logger.info("[Transaction commit and connection close successfully]");
        }
    }

    /**
     * The transaction rollbacks and closes the transaction connection
     */
    public void rollback() throws SQLException {
        this.connection.rollback();
        this.close();
        if (Logger.isVaild()) {
            Logger.info("[Transaction rollback and connection close successfully]");
        }
    }

    void close() throws SQLException {
        this.connection.close();
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public DataBean executeQueryBean(String sql, Object... values) throws SQLException {
        return DBexec.executequeryBean(connection, sql, values);
    }

    @Override
    public List<DataBean> executeQueryBeans(String sql, Object... values) throws SQLException {
        return DBexec.executequeryBeans(connection, sql, values);
    }

    @Override
    public <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        return DBexec.executeQueryList(claz, connection, sql, values);
    }

    @Override
    public <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        return DBexec.executeQuery(claz, connection, sql, values);
    }

    @Override
    public int executeUpdate(String sql, Object... values) throws SQLException {
        return DBexec.executeUpdate(connection, sql, values);
    }

    @Override
    public int[] executeBatch(String sql, List<Object[]> values) throws SQLException {
        return DBexec.executeBatch(connection, sql, values);
    }

    @Override
    public Map<Integer, Object> executeCall(String procedureCallMethod, Params... params) throws SQLException {
        return DBexec.executeCall(connection, procedureCallMethod, params);
    }
}
