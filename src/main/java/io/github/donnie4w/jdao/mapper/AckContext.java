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

import io.github.donnie4w.jdao.handle.JdaoRuntimeException;
import io.github.donnie4w.jdao.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AckContext {
    private StringBuilder sqlbuilder = null;
    List<Object> params = null;

    public AckContext(StringBuilder sqlbuilder, List<Object> params) {
        this.sqlbuilder = sqlbuilder;
        this.params = params;
    }

    public AckContext(String text, ParamContext paramContext) {
        if (text == null || text.length() == 0) {
            return;
        }
        List<String> list = parseSqltext(text);
        if (list.size() > 0) {
            params = new ArrayList<>();
            for (String name : list) {
                Object v = Utils.resolveVariableValue(name, paramContext);
                if (v != null) {
                    params.add(v);
                } else {
                    throw new JdaoRuntimeException("Parse text[" + text + "] failed! The parameter value for [" + name + "] could not be found");
                }
            }
            if (params.size() == 0 && paramContext.getArray() != null && paramContext.getArray().length >= list.size()) {
                for (int i = 0; i < list.size(); i++) {
                    params.add(paramContext.getArray()[i]);
                }
            }
        }
    }

    public List<String> parseSqltext(String sql) {
        Pattern pattern = Pattern.compile("#\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(sql);
        StringBuffer modifiedSql = new StringBuffer();
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String parameterName = matcher.group(1);
            list.add(parameterName);
            matcher.appendReplacement(modifiedSql, "?");
        }
        matcher.appendTail(modifiedSql);
        sqlbuilder = new StringBuilder();
        sqlbuilder.append(modifiedSql);
        return list;
    }

    public void append(AckContext ackContext) {
        if (ackContext == null) {
            return;
        }
        if (sqlbuilder != null && sqlbuilder.length() > 0) {
            if (ackContext.sqlbuilder != null && ackContext.sqlbuilder.length() > 0) {
                sqlbuilder.append(' ').append(ackContext.sqlbuilder);
            }
        } else {
            sqlbuilder = ackContext.sqlbuilder;
        }
        if (params != null) {
            if (ackContext.params != null) {
                params.addAll(ackContext.params);
            }
        } else {
            params = ackContext.params;
        }
    }

    public StringBuilder getSqlbuilder() {
        return sqlbuilder;
    }

    public String getSql() {
        if (sqlbuilder == null) {
            return null;
        }
        return sqlbuilder.toString();
    }

    public List<Object> getParams() {
        return params;
    }

}
