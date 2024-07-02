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

import io.github.donnie4w.jdao.dbHandler.DBexec;
import io.github.donnie4w.jdao.dbHandler.DataBean;
import io.github.donnie4w.jdao.dbHandler.JdaoException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @File:jdao: com.jdao.base :Transaction.java
 * @Date:2017年11月4日
 * @Copyright (c) 2017, donnie4w@gmail.com All Rights Reserved.
 * @Author: donnie4w
 */
public class Tx implements Transaction {

    Connection connection;
    DataSource dataSource;

    public Tx(Connection connection) throws JdaoException {
        try {
            this.connection = connection;
            this.connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    public Tx(DataSource dataSource) throws JdaoException {
        try {
            this.dataSource = dataSource;
            this.connection = dataSource.getConnection();
            this.connection.setAutoCommit(false);
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    public void commit() throws JdaoException {
        try {
            this.connection.commit();
            this.close();
        } catch (SQLException e) {
            throw new JdaoException(e);
        }
    }

    public void rollback() throws JdaoException {
        try {
            this.connection.rollback();
            this.close();
        } catch (SQLException e) {
            throw new JdaoException(e);
        }
    }

    void close() throws JdaoException {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new JdaoException(e);
        }
    }


    /**
     * @return
     */
    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * @return
     */
    @Override
    public Tx getTransaction() throws JdaoException {
        return this;
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public DataBean executeQueryBean(String sql, Object... values) throws JdaoException {
        return DBexec.executequeryBean(connection, sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException {
        return DBexec.executequeryBeans(connection, sql, values);
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
    public <T extends Table<?>> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException {
        return DBexec.executeQueryList(claz, connection, sql, values);
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
    public <T extends Table<?>> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException {
        return DBexec.executeQuery(claz, connection, sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public int executeUpdate(String sql, Object... values) throws JdaoException {
        return DBexec.executeUpdate(connection, sql, values);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public int[] executeBatch(String sql, List<Object[]> values) throws JdaoException {
        return DBexec.executeBatch(connection, sql, values);
    }
}
