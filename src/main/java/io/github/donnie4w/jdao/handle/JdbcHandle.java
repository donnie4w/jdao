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

import io.github.donnie4w.jdao.base.Table;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public interface JdbcHandle {

    DataSource getDataSource();

    Transaction getTransaction() throws JdaoException;

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    DataBean executeQueryBean(String sql, Object... values) throws JdaoException;

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException;


    /**
     * auther donnie wu
     *
     * @param <T>
     * @param claz
     * @param sql
     * @param values
     * @throws JdaoException
     */
    <T extends Table<?>> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException;

    /**
     * auther donnie wu
     *
     * @param <T>
     * @param claz
     * @param sql
     * @param values
     * @throws JdaoException
     */
    <T extends Table<?>> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException;

    /**
     * auther donnie wu
     *
     * @param sql
     * @param values
     * @throws JdaoException
     */
    int executeUpdate(String sql, Object... values) throws JdaoException;


    /**
     * auther donnie wu
     *
     * @param sql
     * @param values
     * @throws JdaoException
     */
    int[] executeBatch(String sql, List<Object[]> values) throws JdaoException;

}