package io.github.donnie4w.jdao.util;

import io.github.donnie4w.jdao.handle.Jdao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Builds DAO (Data Access Object) instances.
 */
@Deprecated
public class DaoBuilder {


    /**
     * Creates a DAO file with the specified table name, destination path, connection, and character set.
     *
     * @param tablename the name of the table for which to generate the DAO.
     * @param destPath the destination path where the DAO file will be created.
     * @param conn the database connection.
     * @param charset the character set to use for the file.
     * @throws UnsupportedEncodingException if the character set is not supported.
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    public static void createFile(String tablename, String destPath, Connection conn, String charset) throws UnsupportedEncodingException, IOException, SQLException {
        createFile_(null, tablename, destPath, conn, charset, false);
    }

    /**
     * Creates a DAO file with the specified table name, destination path, connection, and character set, overwriting existing files.
     *
     * @param tablename the name of the table for which to generate the DAO.
     * @param destPath the destination path where the DAO file will be created.
     * @param conn the database connection.
     * @param charset the character set to use for the file.
     * @throws UnsupportedEncodingException if the character set is not supported.
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    public static void createFileForce(String tablename, String destPath, Connection conn, String charset) throws UnsupportedEncodingException, IOException, SQLException {
        createFile_(null, tablename, destPath, conn, charset, true);
    }

    /**
     * Creates a DAO file with the specified package name, table name, destination path, connection, and character set.
     *
     * @param packageName the package name for the generated DAO.
     * @param tablename the name of the table for which to generate the DAO.
     * @param destPath the destination path where the DAO file will be created.
     * @param conn the database connection.
     * @param charset the character set to use for the file.
     * @throws UnsupportedEncodingException if the character set is not supported.
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    public static void createFile(String packageName, String tablename, String destPath, Connection conn, String charset) throws UnsupportedEncodingException, IOException, SQLException {
        createFile_(packageName, tablename, destPath, conn, charset, false);
    }

    /**
     * Creates a DAO file with the specified package name, table name, destination path, connection, and character set, overwriting existing files.
     *
     * @param packageName the package name for the generated DAO.
     * @param tablename the name of the table for which to generate the DAO.
     * @param destPath the destination path where the DAO file will be created.
     * @param conn the database connection.
     * @param charset the character set to use for the file.
     * @throws UnsupportedEncodingException if the character set is not supported.
     * @throws IOException if an I/O error occurs.
     * @throws SQLException if a SQL related exception occurs.
     */
    public static void createFileForce(String packageName, String tablename, String destPath, Connection conn, String charset) throws UnsupportedEncodingException, IOException, SQLException {
        createFile_(packageName, tablename, destPath, conn, charset, true);
    }

    private static void createFile_(String packageName, String tablename, String destPath, Connection conn, String charset, boolean isForce) throws UnsupportedEncodingException, IOException, SQLException {
        String curdir = destPath;
        if (curdir == null || "".equals(curdir.trim()))
            curdir = System.getProperty("user.dir");
        String filePath = curdir + File.separator + Utils.upperFirstChar(Utils.delUnderline(tablename)) + ".java";
        File file = new File(filePath);
        if (file.exists()) {
            if (!isForce) {
                System.out.println(filePath + " is exist!");
                return;
            } else {
                file.delete();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(fillContent4Dao(packageName, tablename, conn).toString().getBytes(charset));
            fos.flush();
        } finally {
            if (fos != null)
                fos.close();
        }
        System.out.println("create " + filePath + " successful");
    }

    static String fillContent4Dao(String packageName, String tablename, Connection conn) throws SQLException {
        String sql = "select * from " + tablename + " limit 1";
        String Tablename = Utils.upperFirstChar(Utils.delUnderline(tablename));
        ResultSet rs = conn.prepareStatement(sql).executeQuery();
        StringBuilder sb = new StringBuilder();

        if (packageName != null)
            sb.append("package ").append(packageName).append(";\n\n");

        sb.append("import java.io.Serializable;").append("\n");
        sb.append("import com.jdao.base.Table;").append("\n");
        sb.append("import com.jdao.base.Fields;").append("\n\n");
        sb.append("/**").append("\n");
        sb.append(" * @date " + Utils.dateFormat() + "  dao for table ").append(tablename).append("\n");
        sb.append(" */").append("\n");

        sb.append("public class " + Tablename + " extends Table<" + Tablename + "> implements Serializable{").append("\n");
        sb.append("\tprivate static final long serialVersionUID = 1L;").append("\n");
        sb.append("\tprivate final static String TABLENAME_ = \"" + tablename + "\";").append("\n");

        ResultSetMetaData meta = rs.getMetaData();
        StringBuilder sb4GetSet = new StringBuilder();
        StringBuilder sb4args = new StringBuilder();
        StringBuilder sb4FieldArgs = new StringBuilder();
        StringBuilder sb4Fields = new StringBuilder();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String fileName = Utils.delUnderline(meta.getColumnName(i));
            int javaType = meta.getColumnType(i);
            if (!meta.isSigned(i) && (javaType == Types.INTEGER)) {
                javaType = Types.BIGINT;
            }
            String type = type2String(javaType) + " ";
            sb4Fields.append("\t/**").append(meta.getColumnLabel(i)).append("*/").append("\n");
            sb4Fields.append("\tpublic final static Fields ").append(fileName.toUpperCase() + " = ").append("new Fields(\"`" + meta.getColumnName(i) + "`\")").append(";").append("\n");

            sb4FieldArgs.append(fileName.toUpperCase());
            if (i < meta.getColumnCount())
                sb4FieldArgs.append(",");
            String initField = initField(type2String(javaType));
            if (!Utils.isContainsLowerCase(fileName)) {
                fileName = fileName.toLowerCase();
            }
            if (initField != null) {
                sb4args.append("\tprivate ").append(type).append("" + fileName + " = ").append(initField).append(";").append("\n");
            } else {
                sb4args.append("\tprivate ").append(type).append("" + fileName + "").append(";").append("\n");
            }
            sb4GetSet.append("\tpublic ").append(type).append("get").append(Utils.upperFirstChar(fileName)).append("(){").append("\n");
            sb4GetSet.append("\t\treturn this.").append(fileName).append(";").append("\n");
            sb4GetSet.append("\t}").append("\n\n");
            sb4GetSet.append("\tpublic void ").append("set").append(Utils.upperFirstChar(fileName)).append("(").append(type).append(fileName).append("){").append("\n");
            sb4GetSet.append("\t\tfieldValueMap.put(" + fileName.toUpperCase() + ", " + fileName + ");").append("\n");
            sb4GetSet.append("\t\t this.").append(fileName).append("=").append(fileName).append(";").append("\n");
            sb4GetSet.append("\t}").append("\n");
        }

        sb.append(sb4Fields.toString()).append("\n");
        sb.append(sb4args.toString()).append("\n");

        sb.append("\tpublic ").append(Tablename).append("(){").append("\n");
        sb.append("\t\tsuper(TABLENAME_," + Tablename + ".class);").append("\n");
        sb.append("\t\tsuper.setFields(" + sb4FieldArgs + ");").append("\n");
        sb.append("\t}").append("\n\n");

        sb.append("\tpublic ").append(Tablename).append("(String tableName4sharding){").append("\n");
        sb.append("\t\tsuper(tableName4sharding," + Tablename + ".class);").append("\n");
        sb.append("\t\tsuper.setFields(" + sb4FieldArgs + ");").append("\n");
        sb.append("\t}").append("\n\n");

        sb.append(sb4GetSet.toString());
        sb.append("}");
        return sb.toString();
    }

    static String type2String(int javaType) {
        switch (javaType) {
            case Types.DECIMAL:
                return "java.math.BigDecimal";
            case Types.NUMERIC:
                return "java.math.BigDecimal";
            case Types.SMALLINT:
                return "int";
            case Types.TINYINT:
                return "int";
            case Types.INTEGER:
                return "int";
            case Types.DOUBLE:
                return "java.math.BigDecimal";
            case Types.FLOAT:
                return "java.math.BigDecimal";
            case Types.REAL:
                return "java.math.BigDecimal";
            case Types.TIMESTAMP:
                return "java.util.Date";
            case Types.BIGINT:
                return "java.math.BigDecimal";
            case Types.DATE:
                return "java.util.Date";
            case Types.TIME:
                return "java.util.Date";
            case Types.CHAR:
                return "java.lang.String";
            case Types.LONGVARBINARY:
                return "byte[]";
            case Types.VARCHAR:
                return "java.lang.String";
            case Types.BINARY:
                return "byte[]";
            case Types.VARBINARY:
                return "byte[]";
            case Types.LONGNVARCHAR:
                return "java.lang.String";
            case Types.BIT:
                return "boolean";
            default:
                return "java.lang.String";
        }
    }

    static String initField(String type) {
        if ("java.math.BigDecimal".equals(type)) {
            return "new java.math.BigDecimal(\"0\")";
        } else if ("int".equals(type)) {
            return "0";
        } else {
            return null;
        }
    }
}
