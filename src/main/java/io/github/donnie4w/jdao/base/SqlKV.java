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

import java.util.Arrays;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-13
 * @verion 1.0
 */
public class SqlKV {
    String sql = null;
    Object[] args = null;

    public SqlKV(String sql, Object... objects) {
        this.sql = sql;
        this.args = objects;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public boolean equals(Object skv) {
        if (skv == null)
            return false;
        String sql2 = ((SqlKV) skv).getSql();
        Object[] args2 = ((SqlKV) skv).getArgs();
        if (sql != null && sql2 != null && sql.equals(sql2)) {
            if (args == null && args2 == null) {
                return true;
            }
            if (Arrays.equals(args, args2)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        if (args == null)
            return sql.hashCode();
        else {
            final StringBuilder sb = new StringBuilder();
            for (Object o : args) {
                sb.append(o.toString()).append(",");
            }
            return 31 * sql.hashCode() + sb.toString().hashCode();
        }
    }

    public String toString() {
        if (args == null)
            return "[SQL:" + sql + "]ARGS[]";
        else {
            final StringBuilder sb = new StringBuilder();
            for (Object o : args) {
                sb.append(o.toString()).append(",");
            }
            if (sb.length() > 2)
                sb.delete(sb.length() - 1, sb.length());
            return "SQL[" + sql + "]ARGS[" + sb.toString() + "]";
        }

    }
}