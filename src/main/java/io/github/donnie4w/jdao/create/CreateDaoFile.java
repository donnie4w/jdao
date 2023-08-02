/**
 * https://github.com/donnie4w/jdao
 * Copyright jdao Author. All Rights Reserved.
 * Email: donnie4w@gmail.com
 */
package io.github.donnie4w.jdao.create;

import io.github.donnie4w.jdao.base.DaoFactory;
import io.github.donnie4w.jdao.base.JException;
import io.github.donnie4w.jdao.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @Copyright 2012-2013 donnie(donnie4w@gmail.com)
 * @date 2013-1-10
 * @verion 1.0
 * 生成数据表映射 Jdao Bean文件
 */
public class CreateDaoFile {
    /**
     * @param tablename
     * @param destPath
     * @param conn
     * @param charset
     * @throws JException                 通过jdbc连接数据库，根据参数 tableName(表名) 生成相应的 java源文件。 生成的java文件没有package ，所以 实际使用时，需在生成的文件中自行加上相应的包名。 destPath
     *                                      为java文件的目标地址。如果空值则生成的文件在根目录下(文件创建完成后会在控制台提示生成文件的绝对地址)。
     */
    public static void createDao(String tablename, String destPath, Connection conn, Charset charset, boolean override) throws JException {
        createFile_(null, tablename, destPath, conn, charset, override);
    }

    /**
     * @param packageName
     * @param tablename
     * @param destPath
     * @param conn
     * @param charset
     * @throws JException
     */
    public static void createDaoWithPackage(String packageName, String tablename, String destPath, Connection conn, Charset charset, boolean override) throws JException {
        createFile_(packageName, tablename, destPath, conn, charset, override);
    }

    private static void createFile_(String packageName, String tablename, String destPath, Connection conn, Charset charset, boolean isForce) throws JException {
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
            fos.write(fillContentBean(packageName, tablename, conn).getBytes(charset));
            fos.flush();
        } catch (Exception e) {
            throw new JException(e);
        } finally {
            if (fos != null)
                Utils.close(fos);
        }
        System.out.println("create " + filePath + " successful");
    }

    private static String fillContentBean(String packageName, String tablename, Connection conn) throws SQLException {
        String sql = "select * from " + tablename + " limit 1";
        String Tablename = Utils.upperFirstChar(Utils.delUnderline(tablename));
        ResultSet rs = conn.prepareStatement(sql).executeQuery();
        StringBuilder sb = new StringBuilder();


        sb.append("/**").append("\n");
        sb.append(" * https://github.com/donnie4w/jdao").append("\n");
        sb.append(" * Copyright jdao Author. All Rights Reserved.").append("\n");
        sb.append(" * Email: donnie4w@gmail.com").append("\n");
        sb.append("*/").append("\n\n");
        if (packageName != null)
            sb.append("package ").append(packageName).append(";\n\n");

        sb.append("import io.github.donnie4w.jdao.base.Table;").append("\n");
        sb.append("import io.github.donnie4w.jdao.type.*;").append("\n\n");
        sb.append("/**").append("\n");
        sb.append(" * @date " + Utils.dateFormat() + "   table bean  for  jdao : ").append(tablename).append("\n");
        sb.append(" */").append("\n");
        sb.append("public class " + Tablename + " extends Table<" + Tablename + "> {").append("\n");
        ResultSetMetaData meta = rs.getMetaData();
        StringBuilder sb4Fields = new StringBuilder();

        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String fileName = Utils.delUnderline(meta.getColumnName(i));
            int javaType = meta.getColumnType(i);
            String type = type2String(javaType);
            if (!Utils.isContainsLowerCase(fileName)) {
                fileName = fileName.toLowerCase();
            }
            sb4Fields.append("\tpublic ").append(type).append(" " + fileName).append(";\n");
        }
        sb.append(sb4Fields.toString());
        sb.append("}");
        return sb.toString();
    }

    private static String type2String(int javaType) {
        switch (javaType) {
            case Types.DECIMAL:
                return "BIGDECIMAL";
            case Types.NUMERIC:
                return "BIGDECIMAL";
            case Types.SMALLINT:
                return "SHORT";
            case Types.TINYINT:
                return "SHORT";
            case Types.INTEGER:
                return "INT";
            case Types.DOUBLE:
                return "DOUBLE";
            case Types.FLOAT:
                return "FLOAT";
            case Types.REAL:
                return "FLOAT";
            case Types.TIMESTAMP:
                return "DATE";
            case Types.BIGINT:
                return "LONG";
            case Types.DATE:
                return "DATE";
            case Types.TIME:
                return "DATE";
            case Types.CHAR:
                return "STRING";
            case Types.LONGVARBINARY:
                return "BINARY";
            case Types.VARCHAR:
                return "STRING";
            case Types.BINARY:
                return "BINARY";
            case Types.VARBINARY:
                return "BINARY";
            case Types.LONGNVARCHAR:
                return "STRING";
            case Types.BIT:
                return "BOOLEAN";
            default:
                return "STRING";
        }
    }

    /**
     * 创建Jdao bean文件
     * @param tableName   表名
     * @param filePath    文件输出路径
     * @throws JException
     */
    public void createDao(String tableName, String filePath) throws JException {
        Connection conn = DaoFactory.getDefaultHandler().getConnection();
        if (conn != null) {
            CreateDaoFile.createFile_(null, tableName, filePath, conn, StandardCharsets.UTF_8, true);
        }
    }

    /**
     * @param tableName  表名
     * @param packageName  包名
     * @param filePath   文件输出路径
     * @throws JException
     */
    public void createDaoPackage(String tableName,String packageName, String filePath) throws JException {
        Connection conn = DaoFactory.getDefaultHandler().getConnection();
        if (conn != null) {
            CreateDaoFile.createFile_(packageName, tableName, filePath, conn, StandardCharsets.UTF_8, true);
        }
    }

    /**
     *  创建Jdao bean文件
     * @param tableName   表名
     * @param filePath    文件输出路径
     * @param packageName 包名
     * @param charset     文件编码
     * @param isOverride  是否覆盖已存在文件
     * @throws JException
     */
    public void createDaoFull(String tableName, String filePath, String packageName, Charset charset, Boolean isOverride) throws JException {
        Connection conn = DaoFactory.getDefaultHandler().getConnection();
        if (conn != null) {
            CreateDaoFile.createFile_(packageName, tableName, filePath, conn, charset, isOverride);
        }
    }

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/hstest?serverTimezone=Asia/Shanghai", "root", "123");
        String path = System.getProperty("user.dir") + "/jdao";
        System.out.println(path);
        CreateDaoFile.createDaoWithPackage("io.github.donnie4w.jdao.dao", "hstest", path, conn, StandardCharsets.UTF_8, true);
    }
}
