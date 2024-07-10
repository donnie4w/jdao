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
public class Where<T> {
    private String expression;
    private Object value;

    public Where(String expression, Object value) {
        this.expression = expression;
        this.value = value;
    }

    public Where(String expression, Object... value) {
        this.expression = expression;
        if (value != null && value.length > 0)
            this.value = value;
    }

    public OR<T> OR(Where<T>... wheres) {
        if (wheres != null && wheres.length == 1) {
            expression = expression + wheres[0].getExpression().replaceFirst(" and ", " or ");
            value = value != null ? new Array(value, wheres[0].getValue()) : null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (Where<T> w : wheres) {
                sb.append(w.getExpression());
                value = value != null ? new Array(value, w.getValue()) : null;
            }
            sb.append(") ");
            expression = expression + sb.toString().replaceFirst(" and ", " or (");
        }
        return new OR<T>(expression, value);
    }

    public Where<T> AND(OR<T> or) {
        expression = expression + or.getExpression().replaceFirst(" and ", " and (") + ")";
        value = value != null ? new Array(value, or.getValue()) : null;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public Object getValue() {
        return value;
    }
}
