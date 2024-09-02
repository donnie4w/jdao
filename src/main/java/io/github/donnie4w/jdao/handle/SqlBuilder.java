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

import io.github.donnie4w.jdao.util.RPNCalculator;
import io.github.donnie4w.jdao.util.Utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class SqlBuilder {
    private StringBuilder sql = new StringBuilder();
    private final List<Object> parameters = new ArrayList<>();

    public SqlBuilder append(String text) {
        sql.append(" ").append(text).append(" ");
        return this;
    }

    public SqlBuilder append(String text, Object... params) {
        sql.append(" ").append(text).append(" ");
        addParameters(params);
        return this;
    }

    public SqlBuilder appendIf(String expression, Object context, String text, Object... params) {
        if (RPNCalculator.evaluate(expression, Utils.toMap(context))) {
            sql.append(" ").append(text);
            addParameters(params);
        }
        return this;
    }

    public SqlBuilder appendIf(String expression, Map<String, Object> context, String text, Object... params) {
        if (RPNCalculator.evaluate(expression, context)) {
            sql.append(" ").append(text);
            addParameters(params);
        }
        return this;
    }

    public SqlBuilder appendChoose(Object object, Consumer<ChooseBuilder> chooseBuilderConsumer) {
        ChooseBuilder chooseBuilder = new ChooseBuilder(this, Utils.toMap(object));
        chooseBuilderConsumer.accept(chooseBuilder);
        return this;
    }

    public SqlBuilder appendChoose(Map<String, Object> context, Consumer<ChooseBuilder> chooseBuilderConsumer) {
        ChooseBuilder chooseBuilder = new ChooseBuilder(this, context);
        chooseBuilderConsumer.accept(chooseBuilder);
        return this;
    }

    public SqlBuilder appendForeach(String collectionName, Object context, String item, String separator, String open, String close, Consumer<ForeachBuilder> foreachConsumer) {
        return appendForeach(collectionName, Utils.toMap(context), item, separator, open, close, foreachConsumer);
    }

    public SqlBuilder appendForeach(String collectionName, Map<String, Object> context, String item, String separator, String open, String close, Consumer<ForeachBuilder> foreachConsumer) {
        Object collectionObj = context.get(collectionName);
        if (collectionObj == null) {
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


    public SqlBuilder appendTrim(String prefix, String suffix, String prefixOverrides, String suffixOverrides, Consumer<SqlBuilder> contentBuilder) {
        StringBuilder tempSql = new StringBuilder();
        SqlBuilder tempBuilder = new SqlBuilder(tempSql);
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

    private void addParameter(Object param) {
        parameters.add(param);
    }

    private void addParameters(Object... params) {
        parameters.addAll(Arrays.asList(params));
    }

    public static class ChooseBuilder {
        private final SqlBuilder parentBuilder;
        private final Map<String, Object> context;
        private boolean conditionMet = false;

        public ChooseBuilder(SqlBuilder parentBuilder, Map<String, Object> context) {
            this.parentBuilder = parentBuilder;
            this.context = context;
        }

        public ChooseBuilder when(String expression, String sql, Object... params) {
            if (!conditionMet && RPNCalculator.evaluate(expression, context)) {
                parentBuilder.append(sql).addParameters(params);
                conditionMet = true;
            }
            return this;
        }

        public ChooseBuilder otherwise(String sql, Object... params) {
            if (!conditionMet) {
                parentBuilder.append(sql).addParameters(params);
            }
            return this;
        }
    }

    public static class ForeachBuilder {
        private final SqlBuilder parentBuilder;
        private final String separator;
        private String body;

        public ForeachBuilder(SqlBuilder parentBuilder, String separator) {
            this.parentBuilder = parentBuilder;
            this.separator = separator;
        }

        public ForeachBuilder body(String body) {
            this.body = body;
            return this;
        }

        public String getBody() {
            return body;
        }
    }

    public SqlBuilder appendSet(Consumer<SqlBuilder> contentBuilder) {
        StringBuilder tempSql = new StringBuilder();
        SqlBuilder tempBuilder = new SqlBuilder(tempSql);
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


    private SqlBuilder(StringBuilder tempSql) {
        this.sql = tempSql;
    }

    public SqlBuilder() {
    }

    public static void main(String[] args) {
        SqlBuilder builder = new SqlBuilder();
        Map<String, Object> context = new HashMap<>();
        context.put("username", "John Doe");
        context.put("age", 30);
        context.put("emails", Arrays.asList("john@example.com", "doe@example.com", "john.doe@example.com"));

        builder.append("SELECT * FROM users")
                .appendTrim("WHERE", "", "AND", "", trimBuilder -> {
                    trimBuilder.appendIf("username != null", context, "AND username = ?", context.get("username"))
                            .appendIf("age > 18", context, "AND age = ?", context.get("age"))
                            .appendChoose(context, choose -> choose
                                    .when(" age > 10", "AND age = ?", context.get("age"))
                                    .when("username!=null", "AND username = ?", context.get("username"))
                                    .otherwise("email IS NULL")
                            );
                })
                .append("AND email in")
                .appendForeach("emails", context, "email", ",", "(", ")", foreachBuilder -> foreachBuilder
                        .body("#{email}")
                )
                .append("ORDER BY id ASC");
        String sql = builder.getSql();
        System.out.println(sql);
        System.out.println(builder.parameters);
        System.out.println("-------------------------------------------------------------------");
        builder = new SqlBuilder()
                .append("UPDATE users ")
                .appendSet(set -> set
                        .appendIf("username != null", context, "username = ?,", context.get("username"))
                        .appendIf("age == 30", context, "age = ?,", context.get("age"))
                )
                .append("WHERE id = ?", 10);

        System.out.println(sql);
        System.out.println(builder.parameters);
    }

}