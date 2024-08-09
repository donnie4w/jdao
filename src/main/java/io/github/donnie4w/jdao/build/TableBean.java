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

package io.github.donnie4w.jdao.build;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TableBean {
    String tableName;
    List<FieldBean> fieldlist;
    Map<String, FieldBean> fieldMap;

    public TableBean(String tableName, List<FieldBean> fields) {
        this.tableName = tableName;
        this.fieldlist = fields;
        this.fieldMap = new HashMap<>();
        for (FieldBean field : fields) {
            this.fieldMap.put(field.getFieldName(), field);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<FieldBean> getFieldlist() {
        return fieldlist;
    }

    public Map<String, FieldBean> getFieldMap() {
        return fieldMap;
    }

    public static TableBean getTableBean(String tableName, Connection connection) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE 1=0";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        List<FieldBean> fieldBeans = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            int dataType = metaData.getColumnType(i);
            String typeName = metaData.getColumnTypeName(i);
            String fieldName = metaData.getColumnName(i);

            FieldBean fieldBean = new FieldBean(fieldName, i, dataType, typeName);
            fieldBeans.add(fieldBean);
        }
        return new TableBean(tableName, fieldBeans);
    }

}
