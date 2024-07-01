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

public class Condition {
    private SqlKV sqlKV = null;
    private String node = "";

    private Condition(SqlKV sqlKV, String node) {
        this.sqlKV = sqlKV;
        this.node = node == null ? "" : node;
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

    public boolean equals(Object obj) {
        Condition cd = (Condition) obj;
        if (sqlKV == null || cd.getSqlKV() == null || !sqlKV.equals(cd.getSqlKV()) || !node.equals(cd.getNode())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (sqlKV == null)
            return node.hashCode();
        return 31 * sqlKV.hashCode() + node.hashCode();
    }

    public String toString() {
        return "[node][" + node + "]|" + sqlKV.toString();
    }

}