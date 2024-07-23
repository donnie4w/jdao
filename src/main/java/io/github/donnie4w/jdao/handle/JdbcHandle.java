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
import java.util.List;
import java.util.Map;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public interface JdbcHandle {

    DataSource getDataSource();

    Transaction newTransaction() throws SQLException;

    /**
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    DataBean executeQueryBean(String sql, Object... values) throws SQLException;

    /**
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     * @throws SQLException
     */
    List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException, SQLException;


    /**
     * @param claz
     * @param sql
     * @param values
     * @return
     * @param <T>
     * @throws JdaoException
     * @throws JdaoClassException
     * @throws SQLException
     */
    <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;

    /**
     * @param claz
     * @param sql
     * @param values
     * @return
     * @param <T>
     * @throws JdaoException
     * @throws JdaoClassException
     * @throws SQLException
     */
    <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException;

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     * @throws SQLException
     */
    int executeUpdate(String sql, Object... values) throws JdaoException, SQLException;


    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     * @throws SQLException
     */
    int[] executeBatch(String sql, List<Object[]> values) throws JdaoException, SQLException;


    /**
     * @param procedureName
     * @param params
     * @return
     * @throws SQLException
     */
    Map<Integer, Object> executeCall(String procedureCallMethod, Params... params) throws SQLException;

}