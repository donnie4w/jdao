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

import io.github.donnie4w.jdao.util.Logger;
import io.github.donnie4w.jdao.util.RPNCalculator;
import io.github.donnie4w.jdao.util.Utils;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class SqlBuildHandler extends SqlBuilder{
    private StringBuilder sql = new StringBuilder();
    private final List<Object> parameters = new ArrayList<>();

    private DBhandle dBhandle;

    public void useDBhandle(DBhandle dBhandle) {
        this.dBhandle = dBhandle;
    }

    public SqlBuildHandler append(String text) {
        sql.append(" ").append(text).append(" ");
        return this;
    }

    public SqlBuildHandler append(String text, Object... params) {
        sql.append(" ").append(text).append(" ");
        addParameters(params);
        return this;
    }

    public SqlBuildHandler appendIf(String expression, Object context, String text, Object... params) {
        if (RPNCalculator.evaluate(expression, context)) {
            sql.append(" ").append(text);
            addParameters(params);
        }
        return this;
    }

    public SqlBuildHandler appendChoose(Object object, Consumer<ChooseBuilder> chooseBuilderConsumer) {
        ChooseBuilder chooseBuilder = new ChooseBuilder(this, object);
        chooseBuilderConsumer.accept(chooseBuilder);
        return this;
    }

    public SqlBuildHandler appendForeach(String collectionName, Object context, String item, String separator, String open, String close, Consumer<ForeachBuilder> foreachConsumer) {
        Object collectionObj = null;
        if (Utils.stringValid(collectionName) && !"list".equals(collectionName) && !"array".equals(collectionName)) {
            if (context instanceof Map) {
                collectionObj = ((Map) context).get(collectionName);
            }
        } else {
            collectionObj = context;
        }
        if (collectionObj == null) {
            if (Logger.isVaild()) {
                Logger.severe("AppendForeach unable to find collection data");
            }
            return this;
        }
        Collection<?> collection = null;
        if (collectionObj.getClass().isArray()) {
            int length = Array.getLength(collectionObj);
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                list.add(Array.get(collectionObj, i));
            }
            collection = list;
        } else if (collectionObj instanceof Collection<?>) {
            collection = (Collection<?>) collectionObj;
        }
        if (collection != null && !collection.isEmpty()) {
            if (open != null) {
                sql.append(open);
            }
            ForeachBuilder foreachBuilder = new ForeachBuilder(this, separator);
            foreachConsumer.accept(foreachBuilder);

            Iterator<?> iterator = collection.iterator();
            while (iterator.hasNext()) {
                Object currentItem = iterator.next();
                String replacedBody = foreachBuilder.getBody().replace("#{" + item + "}", "?");
                sql.append(replacedBody);
                addParameter(currentItem);
                if (separator != null && iterator.hasNext()) {
                    sql.append(separator);
                }
            }
            if (close != null) {
                sql.append(close);
            }
        }

        return this;
    }


    public SqlBuildHandler appendTrim(String prefix, String suffix, String prefixOverrides, String suffixOverrides, Consumer<SqlBuilder> contentBuilder) {
        StringBuilder tempSql = new StringBuilder();
        SqlBuildHandler tempBuilder = new SqlBuildHandler(tempSql);
        contentBuilder.accept(tempBuilder);
        while (tempSql.length() > 0 && Character.isWhitespace(tempSql.charAt(0))) {
            tempSql.deleteCharAt(0);
        }
        while (tempSql.length() > 0 && Character.isWhitespace(tempSql.charAt(tempSql.length() - 1))) {
            tempSql.deleteCharAt(tempSql.length() - 1);
        }
        if (prefixOverrides != null) {
            String[] prefixes = prefixOverrides.split("\\|");
            for (String override : prefixes) {
                if (tempSql.indexOf(override) == 0) {
                    tempSql.delete(0, override.length());
                    break;
                }
            }
        }
        if (suffixOverrides != null) {
            String[] suffixes = suffixOverrides.split("\\|");
            for (String override : suffixes) {
                if (tempSql.lastIndexOf(override) == tempSql.length() - override.length()) {
                    tempSql.delete(tempSql.length() - override.length(), tempSql.length());
                }
            }
        }

        if (tempSql.length() > 0) {
            if (Utils.stringValid(prefix)) {
                tempSql.insert(0, prefix);
            }
            if (Utils.stringValid(suffix)) {
                tempSql.append(suffix);
            }
            tempSql.append(' ');
            sql.append(tempSql);
        }
        parameters.addAll(tempBuilder.parameters);
        return this;
    }


    public String getSql() {
        return sql.toString();
    }

    public Object[] getParameters() {
        return parameters.toArray();
    }

     void addParameter(Object param) {
        parameters.add(param);
    }

     protected void addParameters(Object... params) {
        parameters.addAll(Arrays.asList(params));
    }

    public SqlBuildHandler appendSet(Consumer<SqlBuilder> contentBuilder) {
        StringBuilder tempSql = new StringBuilder();
        SqlBuildHandler tempBuilder = new SqlBuildHandler(tempSql);
        contentBuilder.accept(tempBuilder);
        int length = tempSql.length();
        while (length > 0 && (tempSql.charAt(length - 1) == ',' || Character.isWhitespace(tempSql.charAt(length - 1)))) {
            tempSql.deleteCharAt(length - 1);
            length--;
        }
        if (length > 0) {
            sql.append("SET ").append(tempSql).append(" ");
        }
        this.parameters.addAll(tempBuilder.parameters);
        return this;
    }


    private SqlBuildHandler(StringBuilder tempSql) {
        this.sql = tempSql;
    }

    protected SqlBuildHandler() {
    }

    public DataBean selectOne() throws JdaoException, SQLException {
        if (Logger.isVaild()) {
            Logger.info("[SqlBuilder SQL]", this.getSql(), "[ARGS]", Arrays.asList(this.getParameters()));
        }
        return this.getDBhandle().executeQueryBean(this.getSql(), this.getParameters());
    }

    public List<DataBean> selectList() throws JdaoException, SQLException {
        if (Logger.isVaild()) {
            Logger.info("[SqlBuilder SQL]", this.getSql(), "[ARGS]", Arrays.asList(this.getParameters()));
        }
        return this.getDBhandle().executeQueryBeans(this.getSql(), this.getParameters());
    }

    public int exec() throws JdaoException, SQLException {
        if (Logger.isVaild()) {
            Logger.info("[SqlBuilder SQL]", this.getSql(), "[ARGS]", Arrays.asList(this.getParameters()));
        }
        return this.getDBhandle().executeUpdate(this.getSql(), this.getParameters());
    }

    public DBhandle getDBhandle() {
        if (this.dBhandle != null) {
            return this.dBhandle;
        }
        if (Jdao.getDefaultDBhandle() != null) {
            return Jdao.getDefaultDBhandle();
        }
        throw new JdaoRuntimeException("No DataSource DBhandle found");
    }
}
