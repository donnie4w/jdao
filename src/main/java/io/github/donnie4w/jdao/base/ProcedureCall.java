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

import io.github.donnie4w.jdao.dbHandler.DBType;
import io.github.donnie4w.jdao.dbHandler.DBhandle;
import io.github.donnie4w.jdao.dbHandler.JdaoException;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-2-2
 * @verion 1.0.1 存储过程调用
 */
public class ProcedureCall {

    private  String procedureName;
    private  Params[] params;

    private final Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
    private final Map<Integer, Object> inTypesMap = new HashMap<Integer, Object>();
    private final Map<Integer, Integer> outTypesMap = new HashMap<Integer, Integer>();
    private String placeholderStr = "";

    public ProcedureCall(DataSource dataSource, DBType dbType, String procedureName, Params... params) throws JdaoException {
        this.procedureName = procedureName;
        this.params = params;
        parseTypes();
        call(Jdao.newDBhandle(dataSource,dbType));
    }

    public ProcedureCall(String sql, Params... params) throws JdaoException {
        this.procedureName = sql;
        this.params = params;
        parseTypes();
        call(Jdao.getDefaultDBhandle());
    }

    public Object value(int index) {
        return valuesMap.get(index);
    }

    final void call(DBhandle dBhandle) throws JdaoException {
        try (Connection con = dBhandle.getJdbcHandle().getDataSource().getConnection()) {
            parseParams(dBhandle.getDBType());
            call_(con);
        } catch (SQLException e) {
            throw new JdaoException(e);
        }
    }

    private void parseParams(DBType dbType){
        int i = 1;
        int length = params.length;
        StringBuilder sb = new StringBuilder();
        while (i <= length) {
            if (dbType==DBType.POSTGRESQL){
                sb.append("$").append(i);
            }else{
                sb.append("?");
            }
            if (i < length) {
                sb.append(",");
            }
            i+=1;
        }
        placeholderStr = sb.toString();
    }

    final void call_(Connection conn) throws JdaoException {
        StringBuilder sb = new StringBuilder();
        sb.append("{call ").append(procedureName).append("(");
        if (params != null) {
            sb.append(placeholderStr);
        }
        sb.append(")}");
        try (CallableStatement stmt = conn.prepareCall(sb.toString())) {
            for (Integer i : inTypesMap.keySet()) {
                stmt.setObject(i, inTypesMap.get(i));
            }
            for (Integer i : outTypesMap.keySet()) {
                stmt.registerOutParameter(i, outTypesMap.get(i));
            }
            stmt.execute();
            for (Integer i : outTypesMap.keySet()) {
                valuesMap.put(i, stmt.getObject(i));
            }
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    final void parseTypes() {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Out) {
                    outTypesMap.put(i + 1, params[i].getTypes());
                } else if (params[i] instanceof InOut) {
                    outTypesMap.put(i + 1, params[i].getTypes());
                    inTypesMap.put(i + 1, params[i].getValue());
                } else {
                    inTypesMap.put(i + 1, params[i].getValue());
                }
            }
        }
    }
}
