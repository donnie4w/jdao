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

import io.github.donnie4w.jdao.dbHandler.DBType;
import io.github.donnie4w.jdao.dbHandler.DBhandle;
import io.github.donnie4w.jdao.dbHandler.JdaoException;
import io.github.donnie4w.jdao.dbHandler.JdaoRuntimeException;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.*;

/**
 * @param <T>
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0.9
 */
public abstract class Table<T extends Table<?>> implements Scanner, Serializable {
    private static final long serialVersionUID = 1L;

    private final transient Log logger = Log.newInstance();
    protected final List<ObjKV<String, Object>> where = new ArrayList<>();
    protected final List<ObjKV<String, Object>> having = new ArrayList();
    protected final Map<Fields, Object>  fieldMap = new HashMap();
    protected final StringBuilder orderStr = new StringBuilder();
    protected final StringBuilder groupStr = new StringBuilder();
    private final Map<Fields, List<Object>> mBatch = new HashMap<Fields, List<Object>>();
    private final String AND = " and ";
    protected int[] limitArg = null;
    protected Fields[] fields = null;
    protected boolean isloggerOn = false;

    private String TABLENAME = null;
    private Class<T> clazz = null;

    private boolean isCache = false;
    private String domain = null;
    private String node = null;
    private String commentLine = null;
    private transient Transaction transaction = null;
    private transient DBhandle dbhandle = null;
    private transient boolean mustMaster = false;

    public Table(String tablename, Class<T> claz) {
        this.TABLENAME = tablename;
        this.clazz = claz;
    }

    private static void getArrayObj(List<Object> list, Array a) {
        for (Object o : a.getArray()) {
            if (o instanceof Array) {
                getArrayObj(list, (Array) o);
            } else {
                list.add(o);
            }
        }
    }

    public void setMustMaster(boolean mustMaster) {
        this.mustMaster = mustMaster;
    }

    public void useTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void useDataSource(DataSource dataSource, DBType dbType) {
        this.dbhandle = Jdao.newDBhandle(dataSource, dbType);
    }

    private DBhandle getDBhandle(boolean qureyType) {
        if (this.dbhandle != null) {
            return this.dbhandle;
        }
        DBhandle dbhandle = Jdao.getDBhandle(this.clazz, this.clazz.getPackageName(), qureyType && !mustMaster);
        if (dbhandle == null) {
            throw new JdaoRuntimeException("DataSource not found");
        }
        return dbhandle;
    }

    public void setFields(Fields... fields) {
        this.fields = fields;
    }

    public List<ObjKV<String, Object>> where(Where... wheres) {
        for (Where w : wheres) {
            where.add(new ObjKV<>(w.getExpression(), w.getValue()));
        }
        return where;
    }

    public void orderBy(Sort... sorts) {
        for (Sort s : sorts) {
            if (orderStr.length() > 0) {
                orderStr.append(",");
            }
            orderStr.append(s.getFieldName());
        }
    }

    public void groupBy(Fields... fields) {
        for (Fields f : fields) {
            if (groupStr.length() > 0) groupStr.append(",");
            groupStr.append(f.getFieldName());
        }
    }

    public void having(Where... wheres) {
        for (Where w : wheres) {
            having.add(new ObjKV<>(w.getExpression(), w.getValue()));
        }
    }

    public void limit(int limit) {
        limitArg = new int[]{limit};
    }

    public void limit(int offset, int limit) {
        limitArg = new int[]{offset, limit};
    }

    private SqlKV encodeSqlKV(Field... fields) throws JdaoException {
        final StringBuilder sb = new StringBuilder();
        final List<Object> list = new ArrayList<Object>();
        if (commentLine != null) {
            sb.append("/* ").append(commentLine).append(" */");
        }
        sb.append("select ");

        int length = fields.length;
        int i = 1;
        StringBuilder fieldsSb = new StringBuilder();
        for (Field hf : fields) {
            fieldsSb.append(hf.getFieldName());
            if (i < length) {
                fieldsSb.append(",");
            }
            i++;
        }
        if (fieldsSb.length() > 0) {
            sb.append(fieldsSb);
        } else {
            sb.append("*");
        }
        sb.append(" from ").append(TABLENAME);
        length = where.size();
        if (length > 0) {
            final StringBuilder sbWhere = new StringBuilder();
            for (ObjKV<String, Object> kv : where) {
                if (sbWhere.length() == 0) {
                    sbWhere.append(" where ");
                    String key = kv.getKey();
                    if (key.startsWith(AND)) {
                        sbWhere.append(key.substring(AND.length()));
                    }
                } else {
                    sbWhere.append(kv.getKey());
                }

                Object o = kv.getValue();
                if (o != null) if (o instanceof Array) {
                    getArrayObj(list, (Array) o);
                } else {
                    list.add(o);
                }
            }
            sb.append(sbWhere);
        }
        if (groupStr.length() > 0) sb.append(" group by ").append(groupStr);

        if (having.size() > 0) {
            final StringBuilder sbhaving = new StringBuilder();
            sbhaving.append(" having ");
            for (ObjKV kv : having) {
                sbhaving.append(kv.getKey());
                Object o = kv.getValue();
                if (o instanceof Array) {
                    getArrayObj(list, (Array) o);
                } else {
                    list.add(o);
                }
            }
            sb.append(sbhaving);
        }

        if (orderStr.length() > 0) sb.append(" order by ").append(orderStr);
        if (limitArg != null) {
            if (limitArg.length == 1) {
                sb.append(limitAdapt());
                list.add(limitArg[0]);
            } else if (limitArg.length == 2) {
                sb.append(limit2Adapt());
                list.add(limitArg[0]);
                list.add(limitArg[1]);
            }
        }
        String sql = sb.toString();
        if (list.size() > 0) {
            Object[] args = new Object[list.size()];
            list.toArray(args);
            return new SqlKV(sql, args);
        } else {
            return new SqlKV(sql);
        }
    }

    private String limitAdapt() {
        switch (getDBhandle(true).getDBType()) {
            case SQLSERVER:
                return " OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY ";
            case ORACLE:
            case DB2:
            case DERBY:
                return " FETCH FIRST ? ROWS ONLY ";
            case GREENPLUM:
            case NETEZZA:
            case POSTGRESQL:
                return " LIMIT ? OFFSET 0 ";
            case TERADATA:
            case FIREBIRD:
            case SYBASE:
                limitArg = null;
                return "";
            case INGRES:
            case H2:
            case VERTICA:
            case MYSQL:
            case MARIADB:
            case SQLITE:
            default:
                return " LIMIT ? ";
        }
    }

    private String limit2Adapt() {
        switch (getDBhandle(true).getDBType()) {
            case POSTGRESQL:
            case GREENPLUM:
                return " OFFSET ? LIMIT ? ";
            case ORACLE:
            case SQLSERVER:
                return " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
            case SQLITE:
            case NETEZZA:
            case INGRES:
            case H2:
            case VERTICA:
                int offset = limitArg[0];
                limitArg[0] = limitArg[1];
                limitArg[1] = offset;
                return " LIMIT ? OFFSET ? ";
            case DB2:
            case DERBY:
                offset = limitArg[0];
                limitArg[0] = limitArg[1];
                limitArg[1] = offset;
                return " FETCH FIRST ? ROWS ONLY OFFSET ? ROWS ";
            case SYBASE:
            case TERADATA:
            case FIREBIRD:
                limitArg = null;
                return "";
            case MYSQL:
            case MARIADB:
            default:
                return " LIMIT ?,? ";
        }
    }


    public List<T> selects(Field... fs) throws JdaoException {
        if (fs == null) {
            fs = fields;
        }
        SqlKV skv = encodeSqlKV(fs);
        if (this.isloggerOn) {
            logger.log("[SELETE SQL][" + skv.getSql() + "]" + Arrays.toString(skv.getArgs()));
        }
        Object o = null;
        if (isCache) {
            o = Cache.getCache(domain, clazz, Condition.newInstance(skv, node));
            if (o != null) {
                logger.log("[USE CACHE]:" + skv);
                return (List<T>) o;
            }
        }
        o = getDBhandle(true).executeQueryList(transaction, clazz, skv.getSql(), skv.getArgs());
        if (isCache) {
            Cache.setCache(domain, (Class<Table<?>>) clazz, Condition.newInstance(skv, node), o);
        }
        return (List<T>) o;
    }

    public T select(Fields... fs) throws JdaoException {
        if (fs == null) {
            fs = fields;
        }
        SqlKV skv = encodeSqlKV(fs);
        if (this.isloggerOn) {
            logger.log("[SELETE SQL][" + skv.getSql() + "]" + Arrays.toString(skv.getArgs()));
        }
        Object o = null;
        if (isCache) {
            o = Cache.getCache(domain, clazz, Condition.newInstance(skv, node));
            if (o != null) {
                logger.log("[USE CACHE]:" + skv);
                return (T) o;
            }
        }
        o = getDBhandle(true).executeQuery(transaction, clazz, skv.getSql(), skv.getArgs());
        if (isCache) {
            Cache.setCache(domain, (Class<Table<?>>) clazz, Condition.newInstance(skv, node), o);
        }
        return (T) o;
    }

    public int insert() throws JdaoException {
        SqlKV kv = save_();
        return getDBhandle(false).executeUpdate(transaction, kv.getSql(), kv.getArgs());
    }

    private SqlKV save_() {
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (commentLine != null) {
            sb1.append("/* ").append(commentLine).append(" */");
        }
        sb1.append("insert into " + TABLENAME + "(");
        sb2.append(")values(");
        int i = 1;
        int length = fieldMap.size();
        Object[] values = new Object[length];
        for (Fields hf : fieldMap.keySet()) {
            values[i - 1] = fieldMap.get(hf);
            sb1.append(hf.getFieldName());
            sb2.append("?");
            if (i < length) {
                sb1.append(",");
                sb2.append(",");
            }
            i++;
        }
        sb1.append(sb2).append(")");
        String sql = sb1.toString();
        if (this.isloggerOn) {
            StringBuilder sb = new StringBuilder();
            for (Object o : values) {
                sb.append(o).append(",");
            }
            logger.log("[INSERT SQL][" + sql + "][" + sb.substring(0, sb.length() - 1) + "]");
        }
        SqlKV skv = new SqlKV(sql, values);
        return skv;
    }

    public void addBatch() {
        if (mBatch.size() == 0) {
            for (Fields f : fieldMap.keySet()) {
                List<Object> list = new ArrayList<Object>();
                list.add(fieldMap.get(f));
                mBatch.put(f, list);
            }
        } else {
            for (Fields f : mBatch.keySet()) {
                mBatch.get(f).add(fieldMap.get(f));
            }
        }
        fieldMap.clear();
    }

    public int[] executeBatch() throws JdaoException {
        if (fieldMap.size()>0){
            addBatch();
        }
        if (mBatch.size() == 0) {
            return new int[]{};
        }
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (commentLine != null) {
            sb1.append("/* ").append(commentLine).append(" */");
        }
        sb1.append("insert into " + TABLENAME + "(");
        sb2.append(")values(");
        int length = mBatch.get(mBatch.keySet().iterator().next()).size();
        int lengthfield = mBatch.size();
        int i = 1;
        List<Fields> listfields = new ArrayList<Fields>();
        for (Fields f : mBatch.keySet()) {
            listfields.add(f);
            sb1.append(f.getFieldName());
            sb2.append("?");
            if (i < lengthfield) {
                sb1.append(",");
                sb2.append(",");
            }
            i++;
        }
        sb1.append(sb2).append(")");
        List<Object[]> list = new ArrayList<Object[]>();
        for (int k = 0; k < length; k++) {
            Object[] o = new Object[listfields.size()];
            for (int j = 0; j < lengthfield; j++) {
                o[j] = mBatch.get(listfields.get(j)).get(k);
            }
            list.add(o);
        }
        if (isloggerOn) {
            logger.log("[BATCH SQL][" + sb1 + "]" + Arrays.toString(list.toArray()));
        }
        return getDBhandle(false).executeBatch(transaction, sb1.toString(), list);
    }

    public int update() throws JdaoException {
        StringBuilder sb = new StringBuilder();
        if (commentLine != null) {
            sb.append("/* ").append(commentLine).append(" */");
        }
        sb.append("update " + TABLENAME + " set ");
        int i = 1;
        List<Object> list = new ArrayList<Object>();
        for (Fields hf : fieldMap.keySet()) {
            list.add(fieldMap.get(hf));
            sb.append(hf.getFieldName()).append("=?");
            if (i < fieldMap.size()) {
                sb.append(",");
            }
            i++;
        }
        if (where.size() > 0) {
            StringBuilder sbWhere = new StringBuilder();
            for (ObjKV<String, Object> kv : where) {
                if (sbWhere.length() == 0) {
                    sbWhere.append(" where ");
                    String key = kv.getKey();
                    if (key.startsWith(AND)) {
                        sbWhere.append(key.substring(AND.length()));
                    }
                } else {
                    sbWhere.append(kv.getKey());
                }
                Object o = kv.getValue();
                if (o != null) if (o instanceof Array) {
                    getArrayObj(list, (Array) o);
                } else {
                    list.add(o);
                }
            }
            sb.append(sbWhere);
        }
        String sql = sb.toString();
        Object[] values = new Object[list.size()];
        list.toArray(values);
        if (this.isloggerOn) {
            StringBuilder s = new StringBuilder();
            for (Object o : values) {
                s.append(o).append(",");
            }
            logger.log("[UPDATE SQL][" + sql + "][" + s.substring(0, s.length() - 1) + "]");
        }
        return getDBhandle(false).executeUpdate(transaction, sql, list.toArray(values));
    }

    public int delete() throws JdaoException {
        StringBuilder sb = new StringBuilder();
        if (commentLine != null) {
            sb.append("/* ").append(commentLine).append(" */");
        }
        sb.append("delete from " + TABLENAME);
        int length = where.size();
        Object[] values = null;
        if (length > 0) {
            StringBuilder sbWhere = new StringBuilder();
            List<Object> list = new ArrayList<Object>();
            for (ObjKV<String, Object> kv : where) {
                if (sbWhere.length() == 0) {
                    sbWhere.append(" where ");
                    String key = kv.getKey();
                    if (key.startsWith(AND)) {
                        sbWhere.append(key.substring(AND.length()));
                    }
                } else {
                    sbWhere.append(kv.getKey());
                }

                Object o = kv.getValue();
                if (o != null) if (o instanceof Array) {
                    getArrayObj(list, (Array) o);
                } else {
                    list.add(o);
                }
            }
            sb.append(sbWhere);
            values = new Object[list.size()];
            list.toArray(values);
        }
        if (this.isloggerOn) {
            if (values != null && values.length > 0) {
                StringBuilder s = new StringBuilder();
                for (Object o : values) {
                    s.append(o).append(",");
                }
                logger.log("[DELETE SQL][" + sb + "][" + s.substring(0, s.length() - 1) + "]");
            } else {
                logger.log("[DELETE SQL][" + sb + "]");
            }
        }
        return getDBhandle(false).executeUpdate(transaction, sb.toString(), values);

    }

    public void logger(boolean on) {
        this.isloggerOn = on;
        if (on) {
            logger.logOn(on);
        }
    }

    public void reset() {
        where.clear();
        having.clear();
        orderStr.setLength(0);
        groupStr.setLength(0);
        limitArg = null;
        mBatch.clear();
        fieldMap.clear();
        commentLine = null;
    }

    public <T> T useCache(String domain) {
        return useCache(true, domain, null);
    }

    public <T> T useCache(String domain, String node) {
        return useCache(true, domain, node);
    }

    private <T> T useCache(boolean isCache, String domain, String node) {
        this.isCache = isCache;
        this.domain = domain;
        this.node = node;
        return (T) this;
    }

    /**
     * @param commentLine
     */
    public void setCommentLine(String commentLine) {
        this.commentLine = commentLine.matches(".{0,}\\*/.{0,}") ? null : commentLine;
    }
}

