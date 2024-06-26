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

import io.github.donnie4w.jdao.legacy.base.JdaoRuntimeException;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 */
public class Fields implements Field {

    private static final long serialVersionUID = 1L;

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
            throw new JdaoRuntimeException("name can't be null!");
        fieldName = name;
    }

    public Where parse(Object value, String _OPER) {
        String f = "?";
        Object v = value;
        if (value instanceof Fields) {
            f = ((Fields) value).getFieldName();
            v = null;
        }
        return new Where(AND + fieldName + _OPER + f, v);
    }

    /**
     * = 等于
     */
    public Where EQ(Object value) {
        return parse(value, EQ);
    }

    /**
     * >= 大于等于
     */
    public Where GT(Object value) {
        return parse(value, GT);
    }

    /**
     * <= 大于等于
     */
    public Where GE(Object value) {
        return parse(value, GE);
    }

    /**
     * < 小于等于
     */
    public Where LE(Object value) {
        return parse(value, LE);
    }

    /**
     * > 小于
     */
    public Where LT(Object value) {
        return parse(value, LT);
    }

    /**
     * <> 不等于
     */
    public Where NEQ(Object value) {
        return parse(value, NEQ);
    }

    /**
     * like %value%
     */
    public Where LIKE(String value) {
        return new Where(AND + fieldName + " like ? ", "%" + value + "%");
    }

    /**
     * like value%
     */
    public Where lLIKE(String value) {
        return new Where(AND + fieldName + " like ? ", value + "%");
    }

    /**
     * like %value
     */
    public Where rLIKE(String value) {
        return new Where(AND + fieldName + " like  ? ", "%" + value);
    }

    /**
     * between ? and ?
     */
    public Where BETWEEN(Object from, Object to) {
        return new Where(AND + fieldName + " between ? and ? ", new Array(from, to));
    }

    /**
     * in (?)
     */
    public Where IN(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            sb.append("?");
            if (i < objects.length - 1)
                sb.append(",");
        }
        return new Where(AND + fieldName + " in(" + sb + ") ", new Array(objects));
    }

    /**
     * not in (?)
     */
    public Where NOTIN(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            sb.append("?");
            if (i < objects.length - 1)
                sb.append(",");
        }
        return new Where(AND + fieldName + " not in(" + sb + ") ", new Array(objects));
    }

    /**
     * is null
     */
    public Where ISNULL() {
        return new Where(AND + fieldName + " is null ");
    }

    /**
     * is not null
     */
    public Where ISNONULL() {
        return new Where(AND + fieldName + " is not null ");
    }

    /**
     * order by ? asc 升序
     */
    public Sort asc() {
        return new Sort(fieldName + " asc");
    }

    /**
     * order by ? desc 降序
     */
    public Sort desc() {
        return new Sort(fieldName + " desc");
    }

    /**
     * count(?)
     */
    public Func count() {
        return new Func(" count(" + fieldName + ") ");
    }

    /**
     * sum(?)
     */
    public Func sum() {
        return new Func(" sum(" + fieldName + ") ");
    }

    /**
     * distinct
     */
    public Func distinct() {
        return new Func(" distinct " + fieldName + " ");
    }

    /**
     * avg
     */
    public Func avg() {
        return new Func(" avg(" + fieldName + ") ");
    }

    /**
     * max
     */
    public Func max() {
        return new Func(" max(" + fieldName + ") ");
    }

    /**
     * min
     */
    public Func min() {
        return new Func(" min(" + fieldName + ") ");
    }

    /**
     * ucase 转换为大写
     */
    public Func ucase() {
        return new Func(" ucase(" + fieldName + ") ");
    }

    /**
     * lcase 转换为小写
     */
    public Func lcase() {
        return new Func(" lcase(" + fieldName + ") ");
    }

    /**
     * len 长度
     */
    public Func len() {
        return new Func(" len(" + fieldName + ") ");
    }

    /**
     * round(fieldName,0) round函数
     */
    public Func round(int decimals) {
        return new Func(" round(" + fieldName + " ," + decimals + ") ");
    }

    /**
     * format(fieldName,'YYYY-MM-DD') 格式化
     */
    public Func format(String format) {
        return new Func(" format(" + fieldName + " ,'" + format + "') ");
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean equals(Object anObject) {
        if (anObject != null) {
            return this.fieldName.equals(((Fields) anObject).getFieldName());
        }
        return false;
    }

    public int hashCode() {
        return this.fieldName.hashCode() * 31;
    }
}
