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

import io.github.donnie4w.jdao.handle.*;
import io.github.donnie4w.jdao.util.Logger;
import io.github.donnie4w.jdao.util.Utils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

public abstract class Table<T extends Table<?>> implements JStruct<T> {
    private static final long serialVersionUID = 6118092300004961000L;

    private static final String AND = " and ";
    private transient List<ObjKV<String, Object>> where;
    private transient List<ObjKV<String, Object>> having;
    private transient Map<Fields<T>, Object> fieldMap;
    private transient StringBuilder orderSb;
    private transient StringBuilder groupSb;
    private transient Map<Fields<T>, List<Object>> batchMap;
    private transient int[] limitArg;
    private transient Fields<T>[] fields;
    private transient byte isCache = 0;
    private transient String commentLine = null;
    private transient Transaction transaction = null;
    private transient DBhandle dbhandle = null;
    private transient boolean mustMaster = false;
    private transient boolean isinit = false;
    private transient Class<T> clazz = null;
    private String TABLENAME = null;

    protected Table(String tablename, Class<T> claz) {
        this.TABLENAME = tablename;
        init(claz);
    }

    protected void fieldPut(Fields<T> field, Object value) {
        if (fieldMap != null) fieldMap.put(field, value);
    }

    protected void init(Class<T> claz) {
        this.where = new ArrayList<>();
        this.having = new ArrayList();
        this.fieldMap = new HashMap();
        this.orderSb = new StringBuilder();
        this.groupSb = new StringBuilder();
        this.batchMap = new HashMap<Fields<T>, List<Object>>();
        this.clazz = claz;
        this.isinit = true;
    }

    public T useMaster(boolean useMaster) {
        this.mustMaster = useMaster;
        return (T) this;
    }

    /**
     * Set up transactions and enable transactions
     */
    public T useTransaction(Transaction transaction) {
        this.transaction = transaction;
        return (T) this;
    }

    /**
     * Set the dataSource for this operation
     */
    public T useDataSource(DataSource dataSource, DBType dbType) {
        this.dbhandle = Jdao.newDBhandle(dataSource, dbType);
        return (T) this;
    }


    public T useDBhandle(DBhandle dbhandle) {
        this.dbhandle = dbhandle;
        return (T) this;
    }

    private DBhandle getDBhandle(boolean qureyType) {
        if (this.dbhandle != null) {
            return this.dbhandle;
        }
        DBhandle dbhandle = getDBhandle(this.clazz, Utils.getPackageName(this.clazz), qureyType && !mustMaster);
        if (dbhandle == null) {
            throw new JdaoRuntimeException("DataSource not found");
        }
        return dbhandle;
    }

    static DBhandle getDBhandle(Class<? extends Table<?>> clz, String packageName, boolean queryType) {
        DBhandle dbhandle = null;

        if (JdaoSlave.size() > 0 && queryType) {
            dbhandle = JdaoSlave.get(clz, packageName);
            if (dbhandle != null) {
                return dbhandle;
            }
        }

        dbhandle = Jdao.getDBhandle(clz);
        if (dbhandle == null) {
            dbhandle = Jdao.getDBhandle(packageName);
        }
        if (dbhandle == null) {
            dbhandle = Jdao.getDefaultDBhandle();
        }
        return dbhandle;
    }

    protected void initFields(Fields<T>... fields) {
        this.fields = fields;
    }

    /**
     * Adds one or more WHERE conditions to the query builder.
     *
     * @param wheres One or more Where objects representing conditions to be added to the WHERE clause.
     * @return The current query builder object to allow method chaining.
     * <p>
     * Description:
     * This method allows you to add one or more WHERE conditions to the query builder. Each condition is represented by a Where object,
     * which can be combined using logical operators (AND, OR) to form complex conditions. The method returns the current query builder
     * object to support method chaining for building more complex queries.
     * <p>
     * Example:
     * <p> Assuming Hstest is a table class and ID is a column in that table
     * <blockquote><pre>
     *   Hstest hs = new Hstest().where(Hstest.ID.LE(3), Hstest.ID.GE(0).OR(Hstest.ID.EQ(10))).groupBy(Hstest.ID).having(Hstest.ID.count().LT(2)).orderBy(Hstest.ID.asc()).limit(0, 5);
     *   </pre></blockquote>
     */
    public T where(Where<T>... wheres) {
        isinit();
        for (Where<T> w : wheres) {
            where.add(new ObjKV<>(w.getExpression(), w.getValue()));
        }
        return (T) this;
    }

    /**
     * order by
     * The function is the same as order by in sql
     */
    public T orderBy(Sort<T>... sorts) {
        for (Sort s : sorts) {
            if (orderSb.length() > 0) {
                orderSb.append(",");
            }
            orderSb.append(s.getFieldName());
        }
        return (T) this;
    }

    /**
     * group by
     * The function is the same as group by in sql
     */
    public T groupBy(Fields<T>... fields) {
        for (Fields<T> f : fields) {
            if (groupSb.length() > 0) groupSb.append(",");
            groupSb.append(f.getFieldName());
        }
        return (T) this;
    }

    /**
     * having
     * The function is the same as having in sql
     */
    public T having(Where<T>... wheres) {
        for (Where<T> w : wheres) {
            having.add(new ObjKV<>(w.getExpression(), w.getValue()));
        }
        return (T) this;
    }

    /**
     * The LIMIT clause in SQL is used to constrain the number of rows returned by the query.
     * However, not all databases use the same exact command for this purpose.
     * Jdao, as an ORM (Object-Relational Mapping) tool, adapts to different database types (DBTypes)
     * to ensure that the correct syntax is used for each database,
     * guaranteeing that the SQL queries execute properly across various database systems.
     * This means that while the concept and functionality remain consistent (i.e., limiting the result set),
     * the specific command or syntax might vary depending on the underlying database system being used.
     * <p>
     * limit ?
     * <p>
     * The function is the same as limit in sql
     */
    public T limit(int limit) {
        limitArg = new int[]{limit};
        return (T) this;
    }

    /**
     * The LIMIT clause in SQL is used to constrain the number of rows returned by the query.
     * However, not all databases use the same exact command for this purpose.
     * Jdao, as an ORM (Object-Relational Mapping) tool, adapts to different database types (DBTypes)
     * to ensure that the correct syntax is used for each database,
     * guaranteeing that the SQL queries execute properly across various database systems.
     * This means that while the concept and functionality remain consistent (i.e., limiting the result set),
     * the specific command or syntax might vary depending on the underlying database system being used.
     * <p>
     * limit ?,?
     * <p>
     * The function is the same as limit in sql
     */
    public T limit(int offset, int limit) {
        limitArg = new int[]{offset, limit};
        return (T) this;
    }

    private SqlKV encodeSqlKV(Field<T>... fields) throws JdaoException {
        final StringBuilder sb = new StringBuilder();
        final List<Object> list = new ArrayList<Object>();
        if (commentLine != null) {
            sb.append("/* ").append(commentLine).append(" */");
        }
        sb.append("select ");

        int length = fields.length;
        int i = 1;
        StringBuilder fieldsSb = new StringBuilder();
        for (Field<T> hf : fields) {
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
                    parseArray(list, (Array) o);
                } else {
                    list.add(o);
                }
            }
            sb.append(sbWhere);
        }
        if (groupSb.length() > 0) sb.append(" group by ").append(groupSb);

        if (having.size() > 0) {
            final StringBuilder sbhaving = new StringBuilder();
            sbhaving.append(" having ");
            for (ObjKV kv : having) {
                sbhaving.append(kv.getKey());
                Object o = kv.getValue();
                if (o instanceof Array) {
                    parseArray(list, (Array) o);
                } else {
                    list.add(o);
                }
            }
            sb.append(sbhaving);
        }

        if (orderSb.length() > 0) sb.append(" order by ").append(orderSb);
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
            case INFORMIX:
                return " FETCH FIRST ? ROWS ONLY ";
            case GREENPLUM:
            case NETEZZA:
            case POSTGRESQL:
            case OPENGAUSS:
            case ENTERPRISEDB:
            case COCKROACHDB:
                return " LIMIT ? OFFSET 0 ";
            case TERADATA:
            case FIREBIRD:
            case SYBASE:
            case SAPHANA:
                limitArg = null;
                return "";
            case INGRES:
            case H2:
            case VERTICA:
            case MYSQL:
            case MARIADB:
            case SQLITE:
            case TIDB:
            case OCEANBASE:
            case HSQLDB:
            default:
                return " LIMIT ? ";
        }
    }

    private String limit2Adapt() {
        switch (getDBhandle(true).getDBType()) {
            case POSTGRESQL:
            case GREENPLUM:
            case OPENGAUSS:
                return " OFFSET ? LIMIT ? ";
            case ORACLE:
            case SQLSERVER:
            case INFORMIX:
                return " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";
            case SQLITE:
            case NETEZZA:
            case INGRES:
            case H2:
            case VERTICA:
            case HSQLDB:
            case ENTERPRISEDB:
            case COCKROACHDB:
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
            case SAPHANA:
                limitArg = null;
                return "";
            case MYSQL:
            case MARIADB:
            case TIDB:
            case OCEANBASE:
            default:
                return " LIMIT ?,? ";
        }
    }


    /**
     * Query and return list, used in the case of returning multiple database data
     */
    public List<T> selects(Field<T>... fs) throws JdaoException, JdaoClassException, SQLException {
        isinit();
        if (fs == null) {
            fs = fields;
        }
        SqlKV skv = encodeSqlKV(fs);
        if (Logger.isVaild())
            Logger.info("[SELETE SQL][" + skv.getSql() + "]" + Arrays.toString(skv.getArgs()));

        String domain = JdaoCache.getDomain(Utils.getPackageName(clazz), clazz);
        boolean iscache = (isCache == 1 || domain != null) && isCache != 2;
        Object o = null;
        Condition condition = null;
        if (iscache) {
            condition = Condition.newInstance(skv, "List" + clazz.getSimpleName());
            o = JdaoCache.getCache(domain, clazz, condition);
            if (o != null) {
                if (Logger.isVaild())
                    Logger.info("[GET CACHE]:" + skv);
                return (List<T>) o;
            }
        }
        o = getDBhandle(true).executeQueryList(transaction, clazz, skv.getSql(), skv.getArgs());
        if (iscache) {
            JdaoCache.setCache(domain, (Class<Table<?>>) clazz, condition, o);
            if (Logger.isVaild())
                Logger.info("[SET CACHE]:" + skv);
        }
        return (List<T>) o;
    }

    /**
     * Query and return data objects, generally used in the case of returning a database data,
     * if there are multiple query results, return the first
     */
    public T select(Fields<T>... fs) throws JdaoException, JdaoClassException, SQLException {
        isinit();
        if (fs == null) {
            fs = fields;
        }
        SqlKV skv = encodeSqlKV(fs);
        if (Logger.isVaild())
            Logger.info("[SELETE SQL][" + skv.getSql() + "]" + Arrays.toString(skv.getArgs()));
        Object o = null;
        String domain = JdaoCache.getDomain(Utils.getPackageName(clazz), clazz);
        boolean iscache = (isCache == 1 || domain != null) && isCache != 2;
        Condition condition = null;
        if (iscache) {
            condition = Condition.newInstance(skv, clazz.getSimpleName());
            o = JdaoCache.getCache(domain, clazz, condition);
            if (o != null) {
                if (Logger.isVaild())
                    Logger.info("[GET CACHE]:" + skv);
                return (T) o;
            }
        }
        o = getDBhandle(true).executeQuery(transaction, clazz, skv.getSql(), skv.getArgs());
        if (iscache) {
            JdaoCache.setCache(domain, (Class<Table<?>>) clazz, condition, o);
            if (Logger.isVaild())
                Logger.info("[SET CACHE]:" + skv);
        }
        return (T) o;
    }

    /**
     * Execute insert
     */
    public int insert() throws JdaoException, SQLException {
        isinit();
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
        for (Fields<T> hf : fieldMap.keySet()) {
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
        if (Logger.isVaild()) {
            StringBuilder sb = new StringBuilder();
            for (Object o : values) {
                sb.append(o).append(",");
            }
            sb.setLength(sb.length() - 1);
            Logger.info("[INSERT SQL][" + sql + "][" + sb + "]");
        }
        SqlKV skv = new SqlKV(sql, values);
        return skv;
    }

    /**
     * Add batch data
     */
    public void addBatch() {
        isinit();
        if (batchMap.size() == 0) {
            for (Fields f : fieldMap.keySet()) {
                List<Object> list = new ArrayList<Object>();
                list.add(fieldMap.get(f));
                batchMap.put(f, list);
            }
        } else {
            for (Fields f : batchMap.keySet()) {
                batchMap.get(f).add(fieldMap.get(f));
            }
        }
    }

    /**
     * Execute batch processing
     */
    public int[] executeBatch() throws JdaoException, SQLException {
        isinit();
        if (batchMap.size() == 0) {
            return new int[]{};
        }
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        if (commentLine != null) {
            sb1.append("/* ").append(commentLine).append(" */");
        }
        sb1.append("insert into " + TABLENAME + "(");
        sb2.append(")values(");
        int length = batchMap.get(batchMap.keySet().iterator().next()).size();
        int lengthfield = batchMap.size();
        int i = 1;
        List<Fields> listfields = new ArrayList<Fields>();
        for (Fields<T> f : batchMap.keySet()) {
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
                o[j] = batchMap.get(listfields.get(j)).get(k);
            }
            list.add(o);
        }
        if (Logger.isVaild()) {
            List<String> plist = new ArrayList();
            for (Object[] objects : list) {
                plist.add(Arrays.toString(objects));
            }
            Logger.info("[BATCH SQL][" + sb1 + "]" + Arrays.toString(plist.toArray()));
        }
        return getDBhandle(false).executeBatch(transaction, sb1.toString(), list);
    }

    /**
     * Execute update
     */
    public int update() throws JdaoException, SQLException {
        isinit();
        StringBuilder sb = new StringBuilder();
        if (commentLine != null) {
            sb.append("/* ").append(commentLine).append(" */");
        }
        sb.append("update " + TABLENAME + " set ");
        int i = 1;
        List<Object> list = new ArrayList<Object>();
        for (Fields<T> hf : fieldMap.keySet()) {
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
                    parseArray(list, (Array) o);
                } else {
                    list.add(o);
                }

            }
            sb.append(sbWhere);
        }
        String sql = sb.toString();
        Object[] values = new Object[list.size()];
        list.toArray(values);
        if (Logger.isVaild()) {
            StringBuilder s = new StringBuilder();
            for (Object o : values) {
                s.append(o).append(",");
            }
            s.setLength(s.length() - 1);
            Logger.info("[UPDATE SQL][" + sql + "][" + s + "]");
        }
        return getDBhandle(false).executeUpdate(transaction, sql, list.toArray(values));
    }

    /**
     * Execute delete
     */
    public int delete() throws JdaoException, SQLException {
        isinit();
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
                    parseArray(list, (Array) o);
                } else {
                    list.add(o);
                }
            }
            sb.append(sbWhere);
            values = new Object[list.size()];
            list.toArray(values);
        }
        if (Logger.isVaild()) {
            if (values != null && values.length > 0) {
                StringBuilder s = new StringBuilder();
                for (Object o : values) {
                    s.append(o).append(",");
                }
                s.setLength(s.length() - 1);
                Logger.info("[DELETE SQL][" + sb + "][" + s + "]");
            } else {
                Logger.info("[DELETE SQL][" + sb + "]");
            }
        }
        return getDBhandle(false).executeUpdate(transaction, sb.toString(), values);
    }

    public void reset() {
        isinit();
        where.clear();
        having.clear();
        orderSb.setLength(0);
        groupSb.setLength(0);
        limitArg = null;
        batchMap.clear();
        fieldMap.clear();
        commentLine = null;
    }

    /**
     * This operation enables caching
     */
    public T useCache() {
        return useCache(true);
    }


    /**
     * Set whether this table object uses cache
     */
    public T useCache(boolean use) {
        isinit();
        if (use) {
            this.isCache = 1;
        } else {
            this.isCache = 2;
        }
        return (T) this;
    }

    /**
     * Set annotations for Sql
     */
    public T useCommentLine(String commentLine) {
        isinit();
        this.commentLine = commentLine.matches(".{0,}\\*/.{0,}") ? null : commentLine;
        return (T) this;
    }

    private static void parseArray(List<Object> list, Array a) {
        for (Object o : a.getArray()) {
            if (o instanceof Array) {
                parseArray(list, (Array) o);
            } else {
                list.add(o);
            }
        }
    }

    private void isinit() {
        if (!this.isinit) {
            throw new JdaoRuntimeException("the object is not a Jdao object and should be converted to a Jdao object by calling the toJdao() function");
        }
    }

    public String TABLENAME() {
        return TABLENAME;
    }
}

