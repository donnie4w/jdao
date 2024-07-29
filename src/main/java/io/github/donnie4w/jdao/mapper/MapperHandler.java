
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
 *  github.com/donnie4w/jdao
 */

package io.github.donnie4w.jdao.mapper;

import io.github.donnie4w.jdao.base.Condition;
import io.github.donnie4w.jdao.base.SqlKV;
import io.github.donnie4w.jdao.base.Table;
import io.github.donnie4w.jdao.handle.*;
import io.github.donnie4w.jdao.util.Logger;
import io.github.donnie4w.jdao.util.Utils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapperHandler extends JdaoMapper {

    private final static Table mapperTable = new MapperTable("");
    private Transaction transaction;
    private DBhandle dBhandle;

    @Override
    public boolean isAutocommit() {
        return transaction == null;
    }

    public void setAutocommit(boolean on) throws SQLException {
        if (on) {
            if (this.transaction == null) {
                DBhandle dBhandle = getDBhandle("", "", false);
                if (dBhandle != null) {
                    this.transaction = dBhandle.getTransaction();
                } else {
                    throw new SQLException("No data source was found");
                }
            }
        } else {
            this.transaction = null;
        }
    }

    public void useTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void rollback() throws SQLException {
        if (transaction != null) {
            transaction.rollback();
            transaction = null;
        }
    }

    public void commit() throws SQLException {
        if (transaction != null) {
            transaction.commit();
            transaction = null;
        }
    }

    private DBhandle getDBhandle(String namespce, String id, boolean queryType) {
        if (this.dBhandle != null) {
            return this.dBhandle;
        }
        DBhandle dbhandle = null;
        if (Utils.stringValid(namespce) && Utils.stringValid(id) && JdaoSlave.size() > 0 && queryType) {
            dbhandle = JdaoSlave.getMapper(namespce, id);
            if (dbhandle != null) {
                return dbhandle;
            }
        }
        if (Utils.stringValid(namespce) && Utils.stringValid(id)) {
            dbhandle = Jdao.getMapperDBhandle(namespce, id);
        }
        if (dbhandle == null) {
            dbhandle = Jdao.getDefaultDBhandle();
        }
        if (dbhandle == null) {
            throw new JdaoRuntimeException("DataSource not found");
        }
        return dbhandle;
    }

    MapperHandler() {
        if (!MapperParser.hasMapper()) {
            throw new JdaoRuntimeException("The mapping file is not parsed.call JdaoMapper.build(.xml) first");
        }
    }

    public JdaoMapper useDBhandle(DBhandle dBhandle) {
        this.dBhandle = dBhandle;
        return this;
    }

    public JdaoMapper useDBhandle(DataSource dataSource, DBType dbType) {
        this.dBhandle = Jdao.newDBhandle(dataSource, dbType);
        return this;
    }

    public <T> T selectOne(String mapperId, Object... args) throws JdaoException, JdaoClassException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nSELECTONE SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return _selectOne(mapperId, pb, args);
    }

    public <T> T selectOne(String mapperId, Object param) throws JdaoException, JdaoClassException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        Object[] args = pb.setParameter(param);

        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nSELECTONE SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return _selectOne(mapperId, pb, args);
    }


    private <T> T _selectOne(String mapperId, ParamBean pb, Object... args) throws JdaoException, JdaoClassException, SQLException {
        String domain = JdaoCache.getDomain(pb.getNamespace(),pb.getId());
        SqlKV skv = null;
        Object result = null;
        Class resultclass = pb.getResultClass();
        String outputType = pb.getOutputType();
        String sql = pb.getSql();

        if (domain != null) {
            skv = new SqlKV(sql, args);
            result = JdaoCache.getCache(domain, mapperTable.getClass(), Condition.newInstance(skv, mapperId));
            if (result != null) {
                if (Logger.isVaild())
                    Logger.info("[GET CACHE]:" + skv);
                return (T) result;
            }
        }

        if (resultclass != null) {
            result = getDBhandle(pb.getNamespace(), pb.getId(), true).executeQuery(transaction, (Class<T>) resultclass, sql, args);
        } else {
            result = toT(getDBhandle(pb.getNamespace(), pb.getId(), true).executeQueryBean(transaction, sql, args), outputType);
        }

        if (domain != null) {
            if (skv == null) {
                skv = new SqlKV(sql, args);
            }
            if (Logger.isVaild())
                Logger.info("[SET CACHE]:" + skv);

            JdaoCache.setCache(domain, (Class<Table<?>>) mapperTable.getClass(), Condition.newInstance(skv, mapperId), result);
        }
        return (T) result;
    }

    public <T> T[] selectArray(String mapperId, Object... args) throws JdaoException, JdaoClassException, SQLException {
        List<T> list = selectList(mapperId, args);
        if (list != null && !list.isEmpty()) {
            Class<?> elementType = list.get(0).getClass();
            T[] array = (T[]) java.lang.reflect.Array.newInstance(elementType, list.size());
            return list.toArray(array);
        }
        return null;
    }

    public <T> T[] selectArray(String mapperId, Object param) throws JdaoException, JdaoClassException, SQLException {
        List<T> list = selectList(mapperId, param);
        if (list != null && !list.isEmpty()) {
            Class<?> elementType = list.get(0).getClass();
            T[] array = (T[]) java.lang.reflect.Array.newInstance(elementType, list.size());
            return list.toArray(array);
        }
        return null;
    }

    public <T> List<T> selectList(String mapperId, Object... args) throws JdaoException, JdaoClassException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null)
            throw new JdaoException("Mapper Id: " + mapperId + " not found");

        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nSELECTLIST SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return _selectList(mapperId, pb, args);
    }

    public <T> List<T> selectList(String mapperId, Object param) throws JdaoException, JdaoClassException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        Object[] args = pb.setParameter(param);
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nSELECTLIST SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return _selectList(mapperId, pb, args);
    }

    private <T> List<T> _selectList(String mapperId, ParamBean pb, Object... args) throws JdaoException, JdaoClassException, SQLException {
        String domain = JdaoCache.getDomain(pb.getNamespace(),pb.getId());
        Object result = null;
        SqlKV skv = null;
        if (domain != null) {
            skv = new SqlKV(pb.getSql(), args);
            result = JdaoCache.getCache(domain, mapperTable.getClass(), Condition.newInstance(skv, mapperId));
            if (result != null) {
                if (Logger.isVaild())
                    Logger.info("[GET CACHE]:" + skv);

                return (List<T>) result;
            }
        }

        if (pb.getResultClass() != null) {
            result = getDBhandle(pb.getNamespace(), pb.getId(), true).executeQueryList(transaction, (Class<T>) pb.getResultClass(), pb.getSql(), args);
        } else {
            List<DataBean> list = getDBhandle(pb.getNamespace(), pb.getId(), true).executeQueryBeans(transaction, pb.getSql(), args);
            if (list != null && list.size() > 0) {
                List<T> rlist = new ArrayList<>();
                for (DataBean db : list) {
                    rlist.add(toT(db, pb.getOutputType()));
                }
                result = rlist;
            }
        }

        if (domain != null) {
            if (skv == null) {
                skv = new SqlKV(pb.getSql(), args);
            }
            if (Logger.isVaild())
                Logger.info("[SET CACHE]:" + skv);

            JdaoCache.setCache(domain, (Class<Table<?>>) mapperTable.getClass(), Condition.newInstance(skv, mapperId), result);
        }

        return (List<T>) result;
    }


    public int insert(String mapperId, Object... args) throws JdaoException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nINSERT SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return getDBhandle(pb.getNamespace(), pb.getId(), false).executeUpdate(transaction, pb.getSql(), args);
    }

    public int insert(String mapperId, Object param) throws JdaoException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        Object[] args = pb.setParameter(param);
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nINSERT SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return getDBhandle(pb.getNamespace(), pb.getId(), false).executeUpdate(transaction, pb.getSql(), args);
    }

    public int update(String mapperId, Object... args) throws JdaoException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nUPDATE SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return getDBhandle(pb.getNamespace(), pb.getId(), false).executeUpdate(transaction, pb.getSql(), args);
    }

    public int update(String mapperId, Object param) throws JdaoException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        Object[] args = pb.setParameter(param);
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nUPDATE SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return getDBhandle(pb.getNamespace(), pb.getId(), false).executeUpdate(transaction, pb.getSql(), args);
    }

    public int delete(String mapperId, Object... args) throws JdaoException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nDELETE SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return getDBhandle(pb.getNamespace(), pb.getId(), false).executeUpdate(transaction, pb.getSql(), args);
    }

    public int delete(String mapperId, Object param) throws JdaoException, SQLException {
        ParamBean pb = MapperParser.getParamBean(mapperId);
        if (pb == null) {
            throw new JdaoException("Mapper Id: " + mapperId + " not found");
        }
        Object[] args = pb.setParameter(param);
        if (Logger.isVaild())
            Logger.info("[Mapper Id] " + mapperId + " \nDELETE SQL[" + pb.getSql() + "]ARGS" + (args == null ? "[]" : Arrays.toString(args)));

        return getDBhandle(pb.getNamespace(), pb.getId(), false).executeUpdate(transaction, pb.getSql(), args);
    }

    public <T> T toT(DataBean db, String rsType) throws JdaoException {
        if (db == null)
            return null;
        try {
            switch (rsType.toLowerCase()) {
                case "int":
                case "integer":
                    return (T) Integer.valueOf(db.findField(0).valueInt());
                case "long":
                    return (T) Long.valueOf(db.findField(0).valueLong());
                case "short":
                    return (T) Short.valueOf(db.findField(0).valueShort());
                case "float":
                    return (T) Float.valueOf(db.findField(0).valueFloat());
                case "double":
                    return (T) Double.valueOf(db.findField(0).valueDouble());
                case "boolean":
                    return (T) Boolean.valueOf(db.findField(0).valueBoolean());
                case "byte":
                    return (T) Byte.valueOf(db.findField(0).valueByte());
                case "byte[]":
                    return (T) db.findField(0).valueBytes();
                case "char":
                    return (T) Character.valueOf(db.findField(0).valueChar());
                case "string":
                    return (T) db.findField(0).valueString();
                case "bigdecimal":
                    return (T) db.findField(0).valueBigDecimal();
                case "biginteger":
                    return (T) db.findField(0).valueBigInteger();
                case "date":
                    return (T) db.findField(0).valueDate();
                case "localtime":
                    return (T) db.findField(0).valueLocalTime();
                case "localdate":
                    return (T) db.findField(0).valueLocalDate();
                case "localdatetime":
                    return (T) db.findField(0).valueLocalDateTime();
                case "map":
                    return (T) db.toMap();
                case "list":
                    return (T) db.toList();
                case "set":
                    return (T) db.toSet();
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMapper(Class<T> clazz) {
        checkMapper(clazz);
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new MapperInvoke(this)
        );
    }

    /**
     * @param clazz
     */
    private void checkMapper(Class<?> clazz) {
        for (Method m : clazz.getMethods()) {
            String mapperId = clazz.getName() + "." + m.getName();
            if (!MapperParser.containsMapperId(mapperId)) {
                throw new JdaoRuntimeException("[Method not configured in the .xml]" + m.getName() + "[namespace]" + clazz.getName());
            }
        }
    }
}
