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

package io.github.donnie4w.jdao.handle;

import io.github.donnie4w.jdao.base.Table;
import io.github.donnie4w.jdao.util.Logger;

import javax.sql.DataSource;

public class JdaoSlave {

    protected JdaoSlave() {
    }

    private static final SlaveHandle defaultSlaveHandle = new SlaveHandler();

    public static int size() {
        return defaultSlaveHandle.size();
    }

    /**
     * Binds the specified package to use the given data source and database type for all standard entity classes within the package.
     *
     * @param packageName The name of the package containing the entity classes to bind.
     * @param dataSource  The data source to use for database connections.
     * @param dbtype      The type of the database (e.g., DBType.MYSQL, DBType.POSTGRESQL).
     *
     *                    <p>Description:
     *                    This method sets up all entity classes within the specified package to use the provided data source and database type for database qurey operation.
     *
     *                    <p>Example:
     *
     *                    <p> Assuming "com.example.entities" is the package containing the entity classes
     *                    <p> And "dataSource" is an instance of DataSource configured for the database connection
     *                    <p> And "DBType.MYSQL" represents the type of the database<p>
     *                    <blockquote><pre>
     *                                                                                                                     JdaoSlave.bindPackage("com.example.entities", dataSource, DBType.MYSQL);
     *                                                                                                                   </pre></blockquote>
     */
    public static void bindPackage(String packageName, DataSource dataSource, DBType dbtype) {
        defaultSlaveHandle.bindPackage(packageName, dataSource, dbtype);
        if (Logger.isVaild())
            Logger.log("[BIND SLAVE][PACKAGENAME:", packageName, ",DBType:", dbtype, "]");
    }

    /**
     * Binds the specified package to use the given DBhandle for all standard entity classes within the package.
     *
     * @param packageName The name of the package containing the entity classes to bind.
     * @param dBhandle    The DBhandle that encapsulates the CRUD operations and data source management.
     *
     *                    <p>Description:
     *                    This method sets up all entity classes within the specified package to use the provided DB handle for database qurey operation.
     *
     *                    <p>Example:
     *                    <p>  Assuming "com.example.entities" is the package containing the entity classes
     *                    <p>  And "dBhandle" is an instance of DBhandle configured for the database connection and CRUD operations
     *                    <blockquote><pre>
     *                     JdaoSlave.bindPackage("com.example.entities", dBhandle);
     *                     </pre></blockquote>
     */
    public static void bindPackage(String packageName, DBhandle dBhandle) {
        defaultSlaveHandle.bindPackage(packageName, dBhandle);
        if (Logger.isVaild())
            Logger.log("[BIND SLAVE][PACKAGENAME:", packageName, "]");
    }

    public static void unbindPackage(String packageName) {
        defaultSlaveHandle.unbindPackage(packageName);
        if (Logger.isVaild())
            Logger.log("[UNBIND SLAVE][PACKAGENAME:", packageName, "]");
    }


    /**
     * Binds the specified standard entity class to use the given data source and database type.
     *
     * @param clz        The class representing the entity to bind.
     * @param dataSource The data source to use for database connections.
     * @param dbtype     The type of the database (e.g., DBType.MYSQL, DBType.POSTGRESQL).
     *
     *                   <p>Description:
     *                   This method sets up the specified entity class to use the provided data source and database type for database qurey operation.
     *
     *                   <p>Example:
     *                   <p>  Assuming User is a class representing the user entity
     *                   <p>  And "dataSource" is an instance of DataSource configured for the database connection
     *                   <p>  And "DBType.MYSQL" represents the type of the database
     *                   <blockquote><pre>
     *                                                                                                               JdaoSlave.bindClass(User.class, dataSource, DBType.MYSQL);
     *                                                                                                             </pre></blockquote>
     */
    public static void bindClass(Class<? extends Table<?>> clz, DataSource dataSource, DBType dbtype) {
        defaultSlaveHandle.bindClass(clz, dataSource, dbtype);
        if (Logger.isVaild())
            Logger.log("[BIND SLAVE][ CLASS:", clz, ",DBType:", dbtype, "]");
    }

    /**
     * Binds the specified standard entity class to use the given DB handle for database interactions.
     *
     * @param clz      The class representing the entity to bind.
     * @param dBhandle The DB handle that encapsulates the CRUD operations and data source management.
     *
     *                 <p>Description:
     *                 This method sets up the specified entity class to use the provided DB handle for database qurey operation.
     *
     *                 <p>Example:
     *                 <p> Assuming User is a class representing the user entity
     *                 <p> And "dBhandle" is an instance of DBhandle configured for the database connection and CRUD operations
     *                 <blockquote><pre>
     *                                                                                                   JdaoSlave.bindClass(User.class, dBhandle);
     *                                                                                                 </pre></blockquote>
     */
    public static void bindClass(Class<? extends Table<?>> clz, DBhandle dBhandle) {
        defaultSlaveHandle.bindClass(clz, dBhandle);
        if (Logger.isVaild())
            Logger.log("[BIND SLAVE][ CLASS:", clz, "]");
    }

    public static void unbindClass(Class<? extends Table<?>> clz) {
        defaultSlaveHandle.unbindClass(clz);
        if (Logger.isVaild())
            Logger.log("[UNBIND SLAVE][ CLASS:", clz, "]");
    }


    /**
     * Binds the specified XML mapping namespace and CRUD tag ID to use the given data source and database type for the specified CRUD operation.
     *
     * @param namespace  The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param id         The ID of the CRUD tag within the namespace to bind.
     * @param dataSource The data source to use for database connections.
     * @param dbtype     The type of the database (e.g., DBType.MYSQL, DBType.POSTGRESQL).
     * @return true if the binding was successful, false otherwise.
     *
     * <p> Description:
     * This method sets up the specified XML mapping namespace and CRUD tag ID to use the provided data source and database type for database qurey operation.
     *
     * <p> Example:
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUserById" is the ID of the CRUD tag within the namespace
     * <p> And "dataSource" is an instance of DataSource configured for the database connection
     * <p> And "DBType.MYSQL" represents the type of the database
     * <blockquote><pre>
     *   boolean bound = JdaoSlave.bindMapper("com.example.mappers.users", "getUserById", dataSource, DBType.MYSQL);
     * </pre></blockquote>
     */
    public static boolean bindMapper(String namespace, String id, DataSource dataSource, DBType dbtype) {
        if (defaultSlaveHandle.bindMapper(namespace, id, dataSource, dbtype)) {
            if (Logger.isVaild())
                Logger.log("[BIND SLAVE MAPPER][NAMESPACE:", namespace, ",ID:", id, ",DBType:", dbtype, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified XML mapping namespace and CRUD tag ID to use the given DB handle for the specified CRUD operation.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param id        The ID of the CRUD tag within the namespace to bind.
     * @param dBhandle  The DB handle that encapsulates the CRUD operations and data source management.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified XML mapping namespace and CRUD tag ID to use the provided DB handle for the database qurey operation.
     * <p>
     * Example:
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUserById" is the ID of the CRUD tag within the namespace
     * <p>  And "dBhandle" is an instance of DBhandle configured for the database connection and CRUD operations
     * <blockquote><pre>
     *   boolean bound = JdaoSlave.bindMapper("com.example.mappers.users", "getUserById", dBhandle);
     * </pre></blockquote>
     */
    public static boolean bindMapper(String namespace, String id, DBhandle dBhandle) {
        if (defaultSlaveHandle.bindMapper(namespace, id, dBhandle)) {
            if (Logger.isVaild())
                Logger.log("[BIND SLAVE MAPPER][NAMESPACE:", namespace, ",ID:", id, "]");
            return true;
        }
        return false;
    }

    public static void unbindMapper(String namespace, String id) {
        defaultSlaveHandle.unbindMapper(namespace, id);
        if (Logger.isVaild())
            Logger.log("[UNBIND SLAVE MAPPER][NAMESPACE:", namespace, ",ID:", id, "]");
    }

    /**
     * Binds the specified mapper interface to use the given DB handle for all methods within the interface.
     *
     * @param mapperface The mapper interface class that defines the methods to bind.
     * @param dBhandle   The DB handle that encapsulates the CRUD operations and data source management.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified mapper interface to use the provided DB handle for the database qurey operation.
     * <p>
     * Example:
     * <p>  Assuming UserMapper is the mapper interface class
     * <p>  And "dBhandle" is an instance of DBhandle configured for the database connection and CRUD operations
     * <blockquote><pre>
     *   boolean bound = JdaoSlave.bindMapper(UserMapper.class, dBhandle);
     * </pre></blockquote>
     */
    public static boolean bindMapper(Class<?> mapperface, DBhandle dBhandle) {
        if (defaultSlaveHandle.bindMapper(mapperface, dBhandle)) {
            if (Logger.isVaild())
                Logger.log("[BIND SLAVE MAPPER][ INTERFACE:", mapperface, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified mapper interface to use the given data source and database type for all methods within the interface.
     *
     * @param mapperface The mapper interface class that defines the methods to bind.
     * @param dataSource The data source to use for database connections.
     * @param dbtype     The type of the database (e.g., DBType.MYSQL, DBType.POSTGRESQL).
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified mapper interface to use the provided data source and database type for the database qurey operation.
     * <p>
     * Example:
     * <p> Assuming UserMapper is the mapper interface class
     * <p> And "dataSource" is an instance of DataSource configured for the database connection
     * <p> And "DBType.MYSQL" represents the type of the database
     * <blockquote><pre>
     *   boolean bound = JdaoSlave.bindMapper(UserMapper.class, dataSource, DBType.MYSQL);
     * </pre></blockquote>
     */
    public static boolean bindMapper(Class<?> mapperface, DataSource dataSource, DBType dbtype) {
        if (defaultSlaveHandle.bindMapper(mapperface, dataSource, dbtype)) {
            if (Logger.isVaild())
                Logger.log("[BIND SLAVE MAPPER][ INTERFACE:", mapperface, ",DBType:", dbtype, "]");
            return true;
        }
        return false;
    }

    public static void unbindMapper(Class<?> mapperface) {
        defaultSlaveHandle.unbindMapper(mapperface);
        if (Logger.isVaild())
            Logger.log("[UNBIND SLAVE MAPPER][ interface:", mapperface, "]");
    }

    /**
     * Binds the specified XML mapping namespace to use the given data source and database type for all methods within the namespace.
     *
     * @param namespace  The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param dataSource The data source to use for database connections.
     * @param dbtype     The type of the database (e.g., DBType.MYSQL, DBType.POSTGRESQL).
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified XML mapping namespace to use the provided data source and database type for the database qurey operation.
     * <p>
     * Example:
     * <p>  // Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p>  // And "dataSource" is an instance of DataSource configured for the database connection
     * <p>  // And "DBType.MYSQL" represents the type of the database
     * <blockquote><pre>
     *   boolean bound = JdaoSlave.bindMapper("com.example.mappers.users", dataSource, DBType.MYSQL);
     * </pre></blockquote>
     */
    public static boolean bindMapper(String namespace, DataSource dataSource, DBType dbtype) {
        if (defaultSlaveHandle.bindMapper(namespace, dataSource, dbtype)) {
            if (Logger.isVaild())
                Logger.log("[BIND SLAVE MAPPER][NAMESPACE:", namespace, ",DBType:", dbtype, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified XML mapping namespace to use the given DB handle for all methods within the namespace.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param dBhandle  The DB handle that encapsulates the CRUD operations and data source management.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified XML mapping namespace to use the provided DB handle for all the database qurey operation.
     * <p>
     * Example:
     * <p>   Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p>   And "dBhandle" is an instance of DBhandle configured for the database connection and CRUD operations
     * <blockquote><pre>
     *   boolean bound = JdaoSlave.bindMapper("com.example.mappers.users", dBhandle);
     * </pre></blockquote>
     */
    public static boolean bindMapper(String namespace, DBhandle dBhandle) {
        if (defaultSlaveHandle.bindMapper(namespace, dBhandle)) {
            if (Logger.isVaild())
                Logger.log("[BIND SLAVE MAPPER][NAMESPACE:", namespace, "]");
            return true;
        }
        return false;
    }

    public static void unbindMapper(String namespace) {
        defaultSlaveHandle.unbindMapper(namespace);
        if (Logger.isVaild())
            Logger.log("[UNBIND SLAVE MAPPER][NAMESPACE:", namespace, "]");
    }

    public static DBhandle get(Class<? extends Table<?>> clz, String packageName) {
        return defaultSlaveHandle.get(clz, packageName);
    }

    public static DBhandle getMapper(String namespace, String id) {
        return defaultSlaveHandle.getMapper(namespace, id);
    }

}
