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


import io.github.donnie4w.jdao.handle.JdaoRuntimeException;

import java.util.Objects;

/**
 * Represents a field in a database query.
 *
 * @param <T> the type of the field
 */
public class Fields<T> implements Field<T> {

    static final String AND = " and ";
    static final String EQ = "=";
    static final String GT = ">";
    static final String GE = ">=";
    static final String LE = "<=";
    static final String LT = "<";
    static final String NEQ = "<>";

    /**
     * The name of the field.
     */
    public String fieldName;

    /**
     * Constructs a new Fields instance.
     *
     * @param name the name of the field
     */
    public Fields(String name) {
        if (name == null)
            throw new JdaoRuntimeException("field name can't be null!");
        fieldName = name;
    }

    /**
     * Parses the given value into a Where clause based on the specified operator.
     *
     * @param value the value to parse
     * @param _OPER the operator to use
     * @return a Where object representing the parsed condition
     */
    public Where<T> parse(Object value, String _OPER) {
        String f = "?";
        Object v = value;
        if (value instanceof Fields) {
            f = ((Fields) value).getFieldName();
            v = null;
        }
        return new Where<T>(AND + fieldName + _OPER + f, v);
    }

    /**
     * Creates a WHERE clause with an equality condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> EQ(Object value) {
        return parse(value, EQ);
    }

    /**
     * Creates a WHERE clause with a greater than condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> GT(Object value) {
        return parse(value, GT);
    }

    /**
     * Creates a WHERE clause with a greater than or equal to condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> GE(Object value) {
        return parse(value, GE);
    }

    /**
     * Creates a WHERE clause with a less than or equal to condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> LE(Object value) {
        return parse(value, LE);
    }

    /**
     * Creates a WHERE clause with a less than condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> LT(Object value) {
        return parse(value, LT);
    }

    /**
     * Creates a WHERE clause with a not equal condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> NEQ(Object value) {
        return parse(value, NEQ);
    }

    /**
     * Creates a WHERE clause with a LIKE condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> LIKE(Object value) {
        return new Where<T>(AND + fieldName + " like ? ", "%" + value + "%");
    }

    /**
     * Creates a WHERE clause with a left LIKE condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> LLIKE(Object value) {
        return new Where<T>(AND + fieldName + " like ? ", value + "%");
    }

    /**
     * Creates a WHERE clause with a right LIKE condition.
     *
     * @param value the value to compare the field against
     * @return a new Where object for further conditions
     */
    public Where<T> RLIKE(Object value) {
        return new Where<T>(AND + fieldName + " like  ? ", "%" + value);
    }

    /**
     * Creates a WHERE clause with a BETWEEN condition.
     *
     * @param from the lower bound of the range
     * @param to the upper bound of the range
     * @return a new Where object for further conditions
     */
    public Where<T> BETWEEN(Object from, Object to) {
        return new Where<T>(AND + fieldName + " between ? and ? ", new Array(from, to));
    }

    /**
     * Creates a WHERE clause with an IN condition.
     *
     * @param objects the values to include in the IN condition
     * @return a new Where object for further conditions
     */
    public Where<T> IN(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            sb.append("?");
            if (i < objects.length - 1)
                sb.append(",");
        }
        return new Where<T>(AND + fieldName + " in(" + sb + ") ", new Array(objects));
    }

    /**
     * Creates a WHERE clause with a NOT IN condition.
     *
     * @param objects the values to exclude in the NOT IN condition
     * @return a new Where object for further conditions
     */
    public Where<T> NOTIN(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            sb.append("?");
            if (i < objects.length - 1)
                sb.append(",");
        }
        return new Where<T>(AND + fieldName + " not in(" + sb + ") ", new Array(objects));
    }

    /**
     * Creates a WHERE clause with an IS NULL condition.
     *
     * @return a new Where object for further conditions
     */
    public Where<T> ISNULL() {
        return new Where<T>(AND + fieldName + " is null ");
    }

    /**
     * Creates a WHERE clause with an IS NOT NULL condition.
     *
     * @return a new Where object for further conditions
     */
    public Where<T> ISNONULL() {
        return new Where<T>(AND + fieldName + " is not null ");
    }

    /**
     * Creates a SORT clause with an ascending order.
     * order by ? asc
     *
     * @return a new Sort object for further sorting
     */
    public Sort<T> asc() {
        return new Sort<T>(fieldName + " asc");
    }

    /**
     * Creates a SORT clause with a descending order.
     * order by ? desc
     *
     * @return a new Sort object for further sorting
     */
    public Sort<T> desc() {
        return new Sort<T>(fieldName + " desc");
    }

    /**
     * Creates a functional aggregation with a COUNT operation.
     * count(?)
     *
     * @return a new Func object for further operations
     */
    public Func<T> count() {
        return new Func<T>(" count(" + fieldName + ") ");
    }

    /**
     * Creates a functional aggregation with a SUM operation.
     * sum(?)
     *
     * @return a new Func object for further operations
     */
    public Func<T> sum() {
        return new Func<T>(" sum(" + fieldName + ") ");
    }

    /**
     * Creates a functional aggregation with a DISTINCT operation.
     *
     * @return a new Func object for further operations
     */
    public Func<T> distinct() {
        return new Func<T>(" distinct " + fieldName + " ");
    }

    /**
     * Creates a functional aggregation with an AVG operation.
     *
     * @return a new Func object for further operations
     */
    public Func<T> avg() {
        return new Func<T>(" avg(" + fieldName + ") ");
    }

    /**
     * Creates a functional aggregation with a MAX operation.
     *
     * @return a new Func object for further operations
     */
    public Func<T> max() {
        return new Func<T>(" max(" + fieldName + ") ");
    }

    /**
     * Creates a functional aggregation with a MIN operation.
     *
     * @return a new Func object for further operations
     */
    public Func<T> min() {
        return new Func<T>(" min(" + fieldName + ") ");
    }

    /**
     * Creates a functional aggregation with an UCASE (uppercase) operation.
     *
     * @return a new Func object for further operations
     */
    public Func<T> ucase() {
        return new Func<T>(" ucase(" + fieldName + ") ");
    }

    /**
     * Creates a functional aggregation with an LCASE (uppercase) operation.
     *
     * @return a Func object representing the lowercase transformation
     */
    public Func<T> lcase() {
        return new Func<T>(" lcase(" + fieldName + ") ");
    }

    /**
     * Creates a functional aggregation with an LEN (uppercase) operation.
     *
     * @return a Func object representing the len transformation
     */
    public Func<T> len() {
        return new Func<T>(" len(" + fieldName + ") ");
    }

    /**
     * Rounds the field value to the specified number of decimal places.
     * round(fieldName,0)
     * @param decimals the number of decimal places
     * @return a Func object representing the rounding operation
     */
    public Func<T> round(int decimals) {
        return new Func<T>(" round(" + fieldName + " ," + decimals + ") ");
    }

    /**
     * Implementation function wrapping
     * e.g.  ID.FnWarp("max")  mean:  max(`id`)
     *
     * @param function the function to apply
     * @return the result of applying the function
     */
    public Func<T> FnWarp(String function) {
        return new Func<T>(" " + function + "(" + fieldName + ") ");
    }

    public String getFieldName() {
        return fieldName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fields fields = (Fields) o;
        return Objects.equals(fieldName, fields.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldName) * 31;
    }
}
