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

import io.github.donnie4w.jdao.util.Logger;
import io.github.donnie4w.jdao.util.Utils;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;

/**
 * Builds DAO (Data Access Object) instances.
 */
public class JdaoBuilder {

    /**
     * Builds a standardized Java DAO entity class based on the provided parameters.
     *
     * @param tableName   The name of the database table to generate the entity for.
     * @param dbType      The type of the database (e.g., MySQL, PostgreSQL).
     * @param packageName The package name for the generated Java class.
     * @param dataSource  An active JDBC DataSource to the database.
     */
    public static void build(String tableName, String dbType, String packageName, DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            build(tableName, tableName, dbType, packageName, connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Builds a standardized Java DAO entity class based on the provided parameters.
     *
     * @param dir         The directory where the generated Java file will be saved.
     * @param tableName   The name of the database table to generate the entity for.
     * @param dbType      The type of the database (e.g., MySQL, PostgreSQL).
     * @param packageName The package name for the generated Java class.
     * @param dataSource  An active JDBC DataSource to the database.
     */
    public static void build(String dir, String tableName, String dbType, String packageName, DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            build(dir, tableName, tableName, dbType, packageName, connection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Builds a standardized Java DAO entity class based on the provided parameters.
     *
     * @param tableName   The name of the database table to generate the entity for.
     * @param dbType      The type of the database (e.g., MySQL, PostgreSQL).
     * @param packageName The package name for the generated Java class.
     * @param connection  An active JDBC connection to the database.
     */
    public static void build(String tableName, String dbType, String packageName, Connection connection) {
        build(tableName, tableName, dbType, packageName, connection);
    }

    /**
     * Builds a standardized Java DAO entity class based on the provided parameters.
     *
     * @param dir         The directory where the generated Java file will be saved.
     * @param tableName   The name of the database table to generate the entity for.
     * @param dbType      The type of the database (e.g., MySQL, PostgreSQL).
     * @param packageName The package name for the generated Java class.
     * @param connection  An active JDBC connection to the database.
     */
    public static void build(String dir, String tableName, String dbType, String packageName, Connection connection) {
        try {
            String structBody = StructBuilder.build(dbType, packageName, tableName, tableName, connection);
            StructBuilder.createFileAndWriteBytes(dir, packageName, Utils.upperFirstChar(tableName), structBody);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Builds a standardized Java DAO entity class based on the provided parameters.
     *
     * @param dir         The directory where the generated Java file will be saved.
     * @param tableName   The name of the database table to generate the entity for.
     * @param tableAlias  An alias for the table name, used in the generated code.
     * @param dbType      The type of the database (e.g., MySQL, PostgreSQL).
     * @param packageName The package name for the generated Java class.
     * @param connection  An active JDBC connection to the database.
     */
    public static void build(String dir, String tableName, String tableAlias, String dbType, String packageName, Connection connection) {
        if (!Utils.stringValid(tableAlias)) {
            tableAlias = tableName;
        }
        try {
            String structBody = StructBuilder.build(dbType, packageName, tableName, tableAlias, connection);
            StructBuilder.createFileAndWriteBytes(dir, packageName, Utils.upperFirstChar(tableAlias), structBody);
            Logger.log("[Dao Builder][table:", tableName, "][dir:", (dir + File.separator + packageName).replaceAll("[\\.|\\\\|\\/]", File.separator.equals("\\") ? "\\\\" : File.separator) + File.separator + Utils.upperFirstChar(tableAlias) + ".java", "]");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
