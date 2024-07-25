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

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Func<T> implements Field<T> {
    private static final long serialVersionUID = 1L;
    public String fieldName;

    public Func(String field) {
        this.fieldName = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    /**
     * = 等于
     */
    public Where<T> EQ(Object value) {
        return new Where<T>(fieldName + "=?", value);
    }

    /**
     * > 大于
     */
    public Where<T> GT(Object value) {
        return new Where<T>(fieldName + ">?", value);
    }

    /**
     * >= 大于等于
     */
    public Where<T> GE(Object value) {
        return new Where<T>(fieldName + ">=?", value);
    }

    /**
     * <= 小于等于
     */
    public Where<T> LE(Object value) {
        return new Where<T>(fieldName + "<=?", value);
    }

    /**
     * < 小于
     */
    public Where<T> LT(Object value) {
        return new Where<T>(fieldName + "<?", value);
    }

    /**
     * <> 不等于
     */
    public Where<T> NEQ(Object value) {
        return new Where<T>(fieldName + "<>?", value);
    }

    /**
     * like %value%
     */
    public Where<T> LIKE(Object value) {
        return new Where<T>(fieldName + " like %?%", value);
    }

    /**
     * like value%
     */
    public Where<T> lLIKE(Object value) {
        return new Where<T>(fieldName + " like %?", value);
    }

    /**
     * like %value
     */
    public Where<T> rLIKE(Object value) {
        return new Where<T>(fieldName + " like ?%", value);
    }

    /**
     * between ? and ?
     */
    public Where<T> BETWEEN(Object from, Object to) {
        return new Where<T>(fieldName + " between ? and ? ", new Array(from, to));
    }

    /**
     * fieldName as alias name
     */
    public Func<T> AS(Field<T> alias) {
        this.fieldName = fieldName + " as " + alias.getFieldName();
        return this;
    }

}
