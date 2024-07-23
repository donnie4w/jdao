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

import io.github.donnie4w.jdao.base.InOut;
import io.github.donnie4w.jdao.base.Out;
import io.github.donnie4w.jdao.base.Params;
import io.github.donnie4w.jdao.base.Table;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBexec {

    private DBexec() {
    }

    /**
     * @param classType
     * @param con
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws JdaoException
     * @throws SQLException
     * @throws JdaoClassException
     */
    static <T> List<T> executeQueryList(Class<T> classType, Connection con, String sql, Object... values) throws JdaoException, SQLException, JdaoClassException {
        List<T> retList = new ArrayList<T>();
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    ps.setObject(i, values[i - 1]);
                }
            }
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int columncount = rs.getMetaData().getColumnCount();
                    T targetBean = classType.getDeclaredConstructor().newInstance();
                    for (int i = 1; i <= columncount; i++) {
                        Object value = rs.getObject(i);
                        if (value != null) {
                            if (targetBean instanceof Table) {
                                ((Table) targetBean).scan(rs.getMetaData().getColumnLabel(i), value);
                            } else {
                                DataBean.scanToClass(classType, targetBean, rs.getMetaData().getColumnLabel(i), value);
                            }
                        }
                    }
                    retList.add(targetBean);
                }
            }
            return retList;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new JdaoClassException(e);
        } finally {
            Closer.closeAll(rs);
        }
    }

    /**
     * @param classType
     * @param con
     * @param sql
     * @param values
     * @param <T>
     * @return
     * @throws SQLException
     * @throws JdaoClassException
     * @throws JdaoException
     */
    static <T> T executeQuery(Class<T> classType, Connection con, String sql, Object[] values) throws SQLException, JdaoClassException, JdaoException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            T targetBean = classType.getDeclaredConstructor().newInstance();
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    ps.setObject(i, values[i - 1]);
                }
            }
            rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    int columncount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= columncount; i++) {
                        Object value = rs.getObject(i);
                        if (value != null) {
                            if (targetBean instanceof Table) {
                                ((Table) targetBean).scan(rs.getMetaData().getColumnLabel(i), value);
                            } else {
                                DataBean.scanToClass(classType, targetBean, rs.getMetaData().getColumnLabel(i), value);
                            }
                        }
                    }
                }
            }
            return targetBean;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new JdaoClassException(e);
        } finally {
            Closer.closeAll(rs);
        }
    }


    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    static List<DataBean> executequeryBeans(Connection con, String sql, Object... values) throws SQLException {
        List<DataBean> retList = new ArrayList<DataBean>();
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    ps.setObject(i, values[i - 1]);
                }
            }
            rs = ps.executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    int columncount = rs.getMetaData().getColumnCount();
                    DataBean bean = new DataBean();
                    for (int i = 0; i < columncount; i++) {
                        String columnName = rs.getMetaData().getColumnLabel(i + 1);
                        bean.put(columnName, i, rs.getObject(i + 1));
                    }
                    retList.add(bean);
                }
            }
            return retList;
        } finally {
            Closer.closeAll(rs);
        }
    }

    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    static DataBean executequeryBean(Connection con, String sql, Object[] values) throws SQLException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    ps.setObject(i, values[i - 1]);
                }
            }
            rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    int columncount = rs.getMetaData().getColumnCount();
                    DataBean bean = new DataBean();
                    for (int i = 0; i < columncount; i++) {
                        String columnName = rs.getMetaData().getColumnLabel(i + 1);
                        bean.put(columnName, i, rs.getObject(i + 1));
                    }
                    return bean;
                }
            }
            return null;
        } finally {
            Closer.closeAll(rs);
        }
    }

    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    static int[] executeBatch(Connection con, String sql, List<Object[]> values) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (values != null) {
                for (Object[] args : values) {
                    for (int i = 1; i <= args.length; i++) {
                        ps.setObject(i, args[i - 1]);
                    }
                    ps.addBatch();
                }
            }
            return ps.executeBatch();
        }
    }

    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    static int executeUpdate(Connection con, String sql, Object... values) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    ps.setObject(i, values[i - 1]);
                }
            }
            return ps.executeUpdate();
        }
    }

    static Map<Integer, Object> executeCall(Connection con, String procedureCallMethod, Params... params) throws SQLException {
        try (CallableStatement stmt = con.prepareCall(procedureCallMethod)) {
            List<Integer> indexList = new ArrayList();
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Out) {
                    indexList.add(i + 1);
                    stmt.registerOutParameter(i + 1, params[i].getTypes());
                } else if (params[i] instanceof InOut) {
                    indexList.add(i + 1);
                    stmt.registerOutParameter(i + 1, params[i].getTypes());
                    stmt.setObject(i + 1, params[i].getValue());
                } else {
                    stmt.setObject(i + 1, params[i].getValue());
                }
            }
            stmt.execute();
            Map<Integer, Object> valuesMap = new HashMap();
            for (Integer i : indexList) {
                valuesMap.put(i, stmt.getObject(i));
            }
            return valuesMap;
        }
    }
}
