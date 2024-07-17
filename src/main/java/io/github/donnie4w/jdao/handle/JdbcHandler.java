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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcHandler implements JdbcHandle {

    private static volatile Map<DataSource,JdbcHandler> instanceMap = new ConcurrentHashMap<>();

    private DataSource dataSource;

    private JdbcHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static JdbcHandler getInstance(DataSource dataSource) {
        if (instanceMap.get(dataSource) == null) {
            synchronized (JdbcHandler.class) {
                if (instanceMap.get(dataSource) == null) {
                    instanceMap.put(dataSource,new JdbcHandler(dataSource));
                }
            }
        }
        return instanceMap.get(dataSource);
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
    public Tx newTransaction() throws JdaoException{
        return new Tx(dataSource);
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public DataBean executeQueryBean(String sql, Object... values) throws JdaoException {
        try (Connection conn = dataSource.getConnection()) {
            return DBexec.executequeryBean(conn,sql,values);
        } catch (SQLException ex) {
           throw new JdaoException(ex);
        }
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException {
        try (Connection conn = dataSource.getConnection()) {
            return DBexec.executequeryBeans(conn,sql,values);
        } catch (SQLException ex) {
          throw new JdaoException(ex);
        }
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
    public <T> List<T> executeQueryList(Class<T> claz, String sql, Object... values) throws JdaoException {
        try (Connection conn = dataSource.getConnection()) {
            return DBexec.executeQueryList(claz,conn,sql,values);
        } catch (SQLException ex) {
            throw new JdaoException(ex);
        }
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
    public <T> T executeQuery(Class<T> claz, String sql, Object... values) throws JdaoException {
        try (Connection conn = dataSource.getConnection()) {
            return DBexec.executeQuery(claz,conn,sql,values);
        } catch (SQLException ex) {
            throw new JdaoException(ex);
        }
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public int executeUpdate(String sql, Object... values) throws JdaoException {
        try (Connection conn = dataSource.getConnection()) {
            return DBexec.executeUpdate(conn,sql,values);
        } catch (SQLException ex) {
            throw new JdaoException(ex);
        }
    }

    /**
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    @Override
    public int[] executeBatch(String sql, List<Object[]> values) throws JdaoException {
        try (Connection conn = dataSource.getConnection()) {
            return DBexec.executeBatch(conn,sql,values);
        } catch (SQLException ex) {
            throw new JdaoException(ex);
        }
    }


}
