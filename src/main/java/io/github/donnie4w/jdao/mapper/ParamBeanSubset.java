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

package io.github.donnie4w.jdao.mapper;

public class ParamBeanSubset extends ParamBean {
    private final String namespace;
    private final String id;
    private final SqlType sqlType;
    private final String sql;
    private final String inputType;
    private final String outputType;
    private final boolean inputIsClass;
    private final Class<?> inputClass;
    private final Class<?> resultClass;
    private final Object[]  params;

    public ParamBeanSubset(String namespace, String id, SqlType sqlType, String sql, String inputType, String outputType, boolean inputIsClass, Class<?> inputClass, Class<?> resultClass, Object[] params) {
        this.namespace = namespace;
        this.id = id;
        this.sqlType = sqlType;
        this.sql = sql;
        this.inputType = inputType;
        this.outputType = outputType;
        this.inputIsClass = inputIsClass;
        this.inputClass = inputClass;
        this.resultClass = resultClass;
        this.params = params;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public SqlType getSqlType() {
        return sqlType;
    }

    @Override
    public String getSql() {
        return sql;
    }

    public String getInputType() {
        return inputType;
    }

    @Override
    public String getOutputType() {
        return outputType;
    }

    public boolean isInputIsClass() {
        return inputIsClass;
    }

    public Class<?> getInputClass() {
        return inputClass;
    }

    @Override
    public Class<?> getResultClass() {
        return resultClass;
    }

    public Object[] getParams() {
        return params;
    }

}
