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

import io.github.donnie4w.jdao.base.Util;
import io.github.donnie4w.jdao.base.Version;
import io.github.donnie4w.jdao.util.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

class StructBuilder {
    protected static void createFileAndWriteBytes(String dir, String packagename, String filename, String structBody) throws IOException {
        String packagePath = packagename.replace('.', File.separatorChar);
        if (Utils.stringValid(dir)) {
            if (dir.endsWith(File.separator)) {
                packagePath = dir + packagePath;
            } else {
                packagePath = dir + File.separator + packagePath;
            }
        }
        String fullPath = packagePath + File.separator + filename + ".java";
        Path path = Paths.get(fullPath);
        Files.createDirectories(path.getParent());
        Files.write(path, structBody.getBytes());
    }

    protected static String build(String dbType, String packageName, String tablename, String tableAlias, Connection conn) throws SQLException {
        if (!Utils.stringValid(tableAlias)) {
            tableAlias = tablename;
        }
        String structName = Utils.upperFirstChar(tableAlias);
        TableBean tb = TableBean.getTableBean(tablename, conn);
        StringBuilder sb = new StringBuilder();

        sb.append("/*").append('\n');
        sb.append("* Copyright (c) 2024, donnie4w <donnie4w@gmail.com> All rights reserved.").append('\n');
        sb.append("*").append('\n');
        sb.append("* Licensed under the Apache License, Version 2.0 (the \"License\");").append('\n');
        sb.append("* you may not use this file except in compliance with the License.").append('\n');
        sb.append("* You may obtain a copy of the License at").append('\n');
        sb.append("*").append('\n');
        sb.append("*      http://www.apache.org/licenses/LICENSE-2.0").append('\n');
        sb.append("*").append('\n');
        sb.append("* Unless required by applicable law or agreed to in writing, software").append('\n');
        sb.append("* distributed under the License is distributed on an \"AS IS\" BASIS,").append('\n');
        sb.append("* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.").append('\n');
        sb.append("* See the License for the specific language governing permissions and").append('\n');
        sb.append("* limitations under the License.").append('\n');
        sb.append("*").append('\n');
        sb.append("* github.com/donnie4w/jdao").append('\n');
        sb.append("*/").append('\n').append('\n');

        if (packageName != null) {
            sb.append("package ").append(packageName).append(";\n\n");
        }

        // Import statements
        sb.append("import java.util.Objects;\n")
                .append("import java.util.Map;\n")
                .append("import java.util.HashMap;\n")
                .append("import io.github.donnie4w.jdao.base.Fields;\n")
                .append("import io.github.donnie4w.jdao.base.Table;\n")
                .append("import io.github.donnie4w.jdao.base.Util;\n")
                .append("import io.github.donnie4w.jdao.util.Serializer;\n")
                .append("import io.github.donnie4w.jdao.handle.JdaoException;\n");

        boolean importDate = false;
        boolean importBigDecimal = false;
        boolean importArrays = false;

        for (FieldBean field : tb.getFieldlist()) {
            String fieldType = javaType(field.getFieldType());
            if (fieldType.equals("Date")) {
                importDate = true;
            }
            if (fieldType.equals("BigDecimal")) {
                importBigDecimal = true;
            }
            if (fieldType.equals("byte[]")) {
                importArrays = true;
            }
        }

        if (importDate) {
            sb.append("import java.util.Date;\n");
        }
        if (importBigDecimal) {
            sb.append("import java.math.BigDecimal;\n");
        }
        if (importArrays) {
            sb.append("import java.util.Arrays;\n");
        }

        // Class declaration
        sb.append("/**\n")
                .append(" * dbtype: ").append(dbType).append(", database: , table: ").append(tablename).append("\n")
                .append(" *\n")
                .append(" * @version jdao version ").append(Version.VERTIONCODE).append("\n")
                .append(" * @date ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n")
                .append(" */\n")
                .append("public class ").append(structName).append(" extends Table<").append(structName).append("> {\n\n")
                .append("    private static final long serialVersionUID = 6118074828633154000L;\n\n")
                .append("    private final static String TABLENAME_ = \"").append(tablename).append("\";\n");

        // Static fields
        for (FieldBean field : tb.getFieldlist()) {
            String fieldName = field.getFieldName().toUpperCase();
            sb.append("    public final static Fields<").append(structName).append("> ").append(fieldName).append(" = new Fields(\"").append(field.getFieldName()).append("\");\n");
        }

        // Constructors
        sb.append("\n    public ").append(structName).append("() {\n")
                .append("        super(TABLENAME_, ").append(structName).append(".class);\n")
                .append("        super.initFields(");
        for (int i = 0; i < tb.getFieldlist().size(); i++) {
            sb.append(tb.getFieldlist().get(i).getFieldName().toUpperCase());
            if (i < tb.getFieldlist().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(");\n    }\n\n")
                .append("    public ").append(structName).append("(String tableName) {\n")
                .append("        super(tableName, ").append(structName).append(".class);\n")
                .append("        super.initFields(");
        for (int i = 0; i < tb.getFieldlist().size(); i++) {
            sb.append(tb.getFieldlist().get(i).getFieldName().toUpperCase());
            if (i < tb.getFieldlist().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(");\n    }\n");

        // Fields and methods
        for (FieldBean field : tb.getFieldlist()) {
            String fieldType = javaType(field.getFieldType());
            String fieldName = Util.encodeFieldname(field.getFieldName());
//            if (fieldType.equals("BigDecimal")){
//                sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(" = BigDecimal.valueOf(0);\n");
//            }else{
            sb.append("    private ").append(fieldType).append(" ").append(fieldName).append(";\n");
//            }
            sb.append("    public ").append(fieldType).append(" get").append(Utils.upperFirstChar(field.getFieldName())).append("() {\n")
                    .append("        return this.").append(fieldName).append(";\n")
                    .append("    }\n")
                    .append("    public void set").append(Utils.upperFirstChar(field.getFieldName())).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n")
                    .append("        fieldPut(").append(field.getFieldName().toUpperCase()).append(", ").append(fieldName).append(");\n")
                    .append("        this.").append(fieldName).append(" = ").append(fieldName).append(";\n")
                    .append("    }\n\n");
        }

        // toString method
        sb.append("\n    @Override\n")
                .append("    public String toString() {\n")
                .append("        return ");
        for (int i = 0; i < tb.getFieldlist().size(); i++) {
            FieldBean field = tb.getFieldlist().get(i);
            String fieldName = Util.encodeFieldname(field.getFieldName());
            sb.append("\"").append(field.getFieldName()).append(":\" + ").append(fieldName);
            if (i < tb.getFieldlist().size() - 1) {
                sb.append(" + \", \" + ");
            }
        }
        sb.append(";\n    }\n");

        // copy method
        sb.append("\n    @Override\n")
                .append("    public ").append(structName).append(" copy(").append(structName).append(" h) {\n");
        for (FieldBean field : tb.getFieldlist()) {
            sb.append("        this.set").append(Utils.upperFirstChar(field.getFieldName())).append("(h.get").append(Utils.upperFirstChar(field.getFieldName())).append("());\n");
        }
        sb.append("        return this;\n    }\n");

        // scan method
        sb.append("\n    @Override\n")
                .append("    public void scan(String fieldname, Object obj) throws JdaoException {\n")
                .append("        try {\n")
                .append("            switch (fieldname) {\n");
        for (FieldBean field : tb.getFieldlist()) {
            String fieldType = javaType(field.getFieldType());
            if (fieldType.equals("byte[]")) {
                fieldType = "bytes";
            }
            sb.append("                case \"").append(field.getFieldName()).append("\":\n")
                    .append("                    set").append(Utils.upperFirstChar(field.getFieldName())).append("(Util.as").append(Utils.upperFirstChar(fieldType)).append("(obj));\n")
                    .append("                    break;\n");
        }
        sb.append("            }\n")
                .append("        } catch (Exception e) {\n")
                .append("            throw new JdaoException(e);\n")
                .append("        }\n    }\n");

        // equals and hashCode methods
        sb.append("\n    @Override\n")
                .append("    public boolean equals(Object o) {\n")
                .append("        if (this == o) return true;\n")
                .append("        if (o == null || getClass() != o.getClass()) return false;\n")
                .append("        ").append(structName).append(" _").append(structName).append(" = (").append(structName).append(") o;\n")
                .append("        return ");
        for (int i = 0; i < tb.getFieldlist().size(); i++) {
            FieldBean field = tb.getFieldlist().get(i);
            String fieldName = Util.encodeFieldname(field.getFieldName());
            String fieldType = javaType(field.getFieldType());
            if (fieldType.equals("BigDecimal") || fieldType.equals("String") || fieldType.equals("Date")) {
                sb.append("Objects.equals(").append(fieldName).append(", _").append(structName).append(".").append(fieldName).append(")");
            } else if (fieldType.equals("byte[]")) {
                sb.append("Objects.deepEquals(").append(fieldName).append(", _").append(structName).append(".").append(fieldName).append(")");
            } else {
                sb.append(fieldName).append(" == _").append(structName).append(".").append(fieldName);
            }
            if (i < tb.getFieldlist().size() - 1) {
                sb.append(" && ");
            }
        }
        sb.append(";\n    }\n\n")
                .append("    @Override\n")
                .append("    public int hashCode() {\n")
                .append("        return Objects.hash(");
        for (int i = 0; i < tb.getFieldlist().size(); i++) {
            FieldBean field = tb.getFieldlist().get(i);
            String fieldName = Util.encodeFieldname(field.getFieldName());
            String fieldType = javaType(field.getFieldType());
            if (fieldType.equals("byte[]")) {
                sb.append("Arrays.hashCode(").append(fieldName).append(")");
            } else {
                sb.append(fieldName);
            }
            if (i < tb.getFieldlist().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(");\n    }\n");

        // Serialization methods
        sb.append("\n    @Override\n")
                .append("    public byte[] encode() {\n")
                .append("        Map<String, Object> map = new HashMap<>();\n");
        for (FieldBean field : tb.getFieldlist()) {
            sb.append("        map.put(\"").append(field.getFieldName()).append("\", this.get").append(Utils.upperFirstChar(field.getFieldName())).append("());\n");
        }
        sb.append("        return Serializer.encode(map);\n    }\n\n")
                .append("    @Override\n")
                .append("    public ").append(structName).append(" decode(byte[] bs) throws JdaoException {\n")
                .append("        Map<String, Object> map = Serializer.decode(bs);\n")
                .append("        if (map != null) {\n")
                .append("            for (Map.Entry<String, Object> entry : map.entrySet()) {\n")
                .append("                scan(entry.getKey(), entry.getValue());\n")
                .append("            }\n")
                .append("        }\n")
                .append("        return this;\n    }\n\n");

        // toJdao method
        sb.append("    @Override\n");
        sb.append("    public void toJdao() {\n");
        sb.append("        super.init(").append(structName).append(".class);\n");
        sb.append("    }\n");
        sb.append("}");
        return sb.toString();
    }


    public static String javaType(int javaType) {
        switch (javaType) {
            case Types.BIT:
            case Types.TINYINT:
                return "byte";
            case Types.SMALLINT:
                return "short";
            case Types.INTEGER:
                return "int";
            case Types.BIGINT:
                return "long";
            case Types.REAL:
                return "float";
            case Types.FLOAT:
            case Types.DOUBLE:
                return "double";
            case Types.NUMERIC:
            case Types.DECIMAL:
                return "BigDecimal";
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.DATALINK:
            case Types.ROWID:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.SQLXML:
                return "String";
            case Types.DATE:
            case Types.TIMESTAMP:
            case Types.TIME:
                return "Date";
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
            case Types.ARRAY:
            case Types.STRUCT:
            case Types.OTHER:
            case Types.JAVA_OBJECT:
            case Types.DISTINCT:
                return "byte[]";
            case Types.BOOLEAN:
                return "boolean";
            default:
                return "String";
        }
    }
}
