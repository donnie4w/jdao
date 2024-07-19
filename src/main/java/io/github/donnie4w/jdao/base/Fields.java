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
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Fields<T> implements Field<T> {

    static final String AND = " and ";
    static final String EQ = "=";
    static final String GT = ">";
    static final String GE = ">=";
    static final String LE = "<=";
    static final String LT = "<";
    static final String NEQ = "<>";

    public String fieldName;

    public Fields(String name) {
        if (name == null)
            throw new JdaoRuntimeException("field name can't be null!");
        fieldName = name;
    }

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
     * = 等于
     */
    public Where<T> EQ(Object value) {
        return parse(value, EQ);
    }

    /**
     * >= 大于等于
     */
    public Where<T> GT(Object value) {
        return parse(value, GT);
    }

    /**
     * <= 大于等于
     */
    public Where<T> GE(Object value) {
        return parse(value, GE);
    }

    /**
     * < 小于等于
     */
    public Where<T> LE(Object value) {
        return parse(value, LE);
    }

    /**
     * > 小于
     */
    public Where<T> LT(Object value) {
        return parse(value, LT);
    }

    /**
     * <> 不等于
     */
    public Where<T> NEQ(Object value) {
        return parse(value, NEQ);
    }

    /**
     * like %value%
     */
    public Where<T> LIKE(Object value) {
        return new Where<T>(AND + fieldName + " like ? ", "%" + value + "%");
    }

    /**
     * like value%
     */
    public Where<T> LLIKE(Object value) {
        return new Where<T>(AND + fieldName + " like ? ", value + "%");
    }

    /**
     * like %value
     */
    public Where<T> RLIKE(Object value) {
        return new Where<T>(AND + fieldName + " like  ? ", "%" + value);
    }

    /**
     * between ? and ?
     */
    public Where<T> BETWEEN(Object from, Object to) {
        return new Where<T>(AND + fieldName + " between ? and ? ", new Array(from, to));
    }

    /**
     * in (?...)
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
     * not in (?...)
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
     * is null
     */
    public Where<T> ISNULL() {
        return new Where<T>(AND + fieldName + " is null ");
    }

    /**
     * is not null
     */
    public Where<T> ISNONULL() {
        return new Where<T>(AND + fieldName + " is not null ");
    }

    /**
     * order by ? asc 升序
     */
    public Sort<T> asc() {
        return new Sort<T>(fieldName + " asc");
    }

    /**
     * order by ? desc 降序
     */
    public Sort<T> desc() {
        return new Sort<T>(fieldName + " desc");
    }

    /**
     * count(?)
     */
    public Func<T> count() {
        return new Func<T>(" count(" + fieldName + ") ");
    }

    /**
     * sum(?)
     */
    public Func<T> sum() {
        return new Func<T>(" sum(" + fieldName + ") ");
    }

    /**
     * distinct
     */
    public Func<T> distinct() {
        return new Func<T>(" distinct " + fieldName + " ");
    }

    /**
     * avg
     */
    public Func<T> avg() {
        return new Func<T>(" avg(" + fieldName + ") ");
    }

    /**
     * max
     */
    public Func<T> max() {
        return new Func<T>(" max(" + fieldName + ") ");
    }

    /**
     * min
     */
    public Func<T> min() {
        return new Func<T>(" min(" + fieldName + ") ");
    }

    /**
     * ucase 转换为大写
     */
    public Func<T> ucase() {
        return new Func<T>(" ucase(" + fieldName + ") ");
    }

    /**
     * lcase 转换为小写
     */
    public Func<T> lcase() {
        return new Func<T>(" lcase(" + fieldName + ") ");
    }

    /**
     * len 长度
     */
    public Func<T> len() {
        return new Func<T>(" len(" + fieldName + ") ");
    }

    /**
     * round(fieldName,0) round函数
     */
    public Func<T> round(int decimals) {
        return new Func<T>(" round(" + fieldName + " ," + decimals + ") ");
    }

    /**
     * Implementation function wrapping
     * e.g.  ID.FnWarp("max")  mean:  max(`id`)
     *
     * @param function
     * @return
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
