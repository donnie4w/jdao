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
import io.github.donnie4w.jdao.util.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
     */
    static <T extends Table<?>> List<T> executeQueryList(Class<T> classType, Connection con, String sql, Object... values) throws JdaoException {
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
                    T object = classType.getConstructor().newInstance();
                    for (int i = 1; i <= columncount; i++) {
                        String columnName = Utils.delUnderline(rs.getMetaData().getColumnLabel(i));
                        object.scan(columnName, rs.getObject(i));
                    }
                    retList.add(object);
                }
            }
            return retList;
        } catch (Exception e) {
            throw new JdaoException(e);
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
     * @throws JdaoException
     */
    static <T extends Table<?>> T executeQuery(Class<T> classType, Connection con, String sql, Object[] values) throws JdaoException {
        ResultSet rs = null;
        T object = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    ps.setObject(i, values[i - 1]);
                }
            }
            rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    object = classType.getConstructor().newInstance();
                    int columncount = rs.getMetaData().getColumnCount();
                    for (int i = 1; i <= columncount; i++) {
                        String columnName = Utils.delUnderline(rs.getMetaData().getColumnLabel(i));
                        object.scan(columnName, rs.getObject(i));
                    }
                }
            }
            return object;
        } catch (Exception e) {
            throw new JdaoException(e);
        } finally {
            Closer.closeAll(rs);
        }
    }


    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    static List<DataBean> executequeryBeans(Connection con, String sql, Object... values) throws JdaoException {
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
        } catch (Exception e) {
            throw new JdaoException(e);
        } finally {
            Closer.closeAll(rs);
        }
    }

    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    static DataBean executequeryBean(Connection con, String sql, Object[] values) throws JdaoException {
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
        } catch (Exception e) {
            throw new JdaoException(e);
        } finally {
            Closer.closeAll(rs);
        }
    }

    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    static int[] executeBatch(Connection con, String sql, List<Object[]> values) throws JdaoException {
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
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    /**
     * @param con
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     */
    static int executeUpdate(Connection con, String sql, Object... values) throws JdaoException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (values != null) {
                for (int i = 1; i <= values.length; i++) {
                    ps.setObject(i, values[i - 1]);
                }
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

}
