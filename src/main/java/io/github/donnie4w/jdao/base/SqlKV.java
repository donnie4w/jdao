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
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlKV sqlKV = (SqlKV) o;
        return Objects.equals(sql, sqlKV.sql) && Objects.deepEquals(args, sqlKV.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sql, Arrays.hashCode(args)) * 31;
    }

    public String toString() {
        if (args == null)
            return "SQL[" + sql + "]ARGS[]";
        else {
            final StringBuilder sb = new StringBuilder();
            for (Object o : args) {
                sb.append(o.toString()).append(",");
            }
            if (sb.length() > 1)
                sb.delete(sb.length() - 1, sb.length());
            return "SQL[" + sql + "]ARGS[" + sb + "]";
        }

    }
}