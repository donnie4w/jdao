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
 * Represents a function applied to a field in a database query.
 *
 * @param <T> the type of the field
 */
public class Func<T> implements Field<T> {
    private static final long serialVersionUID = 1L;
    /**
     * The name of the field.
     */
    public String fieldName;

    /**
     * Constructs a new Func instance.
     *
     * @param field the name of the field
     */
    public Func(String field) {
        this.fieldName = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    /**
     * Compares this field with the given value for equality.
     *
     * @param value the value to compare against
     * @return a Where object representing the comparison
     */
    public Where<T> EQ(Object value) {
        return new Where<T>(fieldName + "=?", value);
    }

    /**
     * Creates a WHERE clause with a greater than condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> GT(Object value) {
        return new Where<T>(fieldName + ">?", value);
    }

    /**
     * Creates a WHERE clause with a greater than or equal to condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> GE(Object value) {
        return new Where<T>(fieldName + ">=?", value);
    }

    /**
     * Creates a WHERE clause with a less than or equal to condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> LE(Object value) {
        return new Where<T>(fieldName + "<=?", value);
    }

    /**
     * Creates a WHERE clause with a less than condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> LT(Object value) {
        return new Where<T>(fieldName + "<?", value);
    }

    /**
     * Creates a WHERE clause with a not equal condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> NEQ(Object value) {
        return new Where<T>(fieldName + "<>?", value);
    }

    /**
     * Creates a WHERE clause with a LIKE condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> LIKE(Object value) {
        return new Where<T>(fieldName + " like %?%", value);
    }

    /**
     * Creates a WHERE clause with a left LIKE condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> lLIKE(Object value) {
        return new Where<T>(fieldName + " like %?", value);
    }

    /**
     * Creates a WHERE clause with a right LIKE condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> rLIKE(Object value) {
        return new Where<T>(fieldName + " like ?%", value);
    }

    /**
     * Creates a WHERE clause with a BETWEEN condition.
     *
     * @param from the lower bound of the range
     * @param to the upper bound of the range
     * @return a new Where object for further conditions
     */
    public Where<T> BETWEEN(Object from, Object to) {
        return new Where<T>(fieldName + " between ? and ? ", new Array(from, to));
    }

    /**
     *  fieldName as alias name
     * @param alias alias name
     * @return a Func object representing the as operation
     */
    public Func<T> AS(Field<T> alias) {
        this.fieldName = fieldName + " as " + alias.getFieldName();
        return this;
    }

}
