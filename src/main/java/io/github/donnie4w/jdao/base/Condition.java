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

/**
 * Represents a condition for SQL queries.
 */
public class Condition {
    private SqlKV sqlKV = null;
    private String node = "";


    private Condition(SqlKV sqlKV, String node) {
        this.sqlKV = sqlKV;
        this.node = node == null ? "" : node;
    }

    /**
     * Creates a new Condition instance with the specified SQL key-value pair.
     *
     * @param sqlKV the SQL key-value pair.
     * @return a new Condition instance.
     */
    public static Condition newInstance(SqlKV sqlKV) {
        return new Condition(sqlKV, null);
    }

    /**
     * Creates a new Condition instance with the specified SQL key-value pair and node.
     *
     * @param sqlKV the SQL key-value pair.
     * @param node the node.
     * @return a new Condition instance.
     */
    public static Condition newInstance(SqlKV sqlKV, String node) {
        return new Condition(sqlKV, node);
    }

    /**
     * Gets the SQL key-value pair associated with this condition.
     *
     * @return the SQL key-value pair.
     */
    public SqlKV getSqlKV() {
        return sqlKV;
    }

    /**
     * Gets the node associated with this condition.
     *
     * @return the node.
     */
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