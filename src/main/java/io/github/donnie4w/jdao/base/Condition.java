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

import java.util.Objects;

public class Condition {
    private SqlKV sqlKV = null;
    private String node = "";

    private Condition(SqlKV sqlKV, String node) {
        this.sqlKV = sqlKV;
        this.node = node == null ? "" : node;
    }

    public static Condition newInstance(SqlKV sqlKV) {
        return new Condition(sqlKV, null);
    }

    public static Condition newInstance(SqlKV sqlKV, String node) {
        return new Condition(sqlKV, node);
    }

    public SqlKV getSqlKV() {
        return sqlKV;
    }

    public String getNode() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition condition = (Condition) o;
        return Objects.equals(sqlKV, condition.sqlKV) && Objects.equals(node, condition.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sqlKV, node);
    }

    public String toString() {
        return "[node][" + node + "]|" + sqlKV.toString();
    }

}