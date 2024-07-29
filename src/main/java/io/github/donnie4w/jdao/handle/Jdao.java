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

import io.github.donnie4w.jdao.base.Params;
import io.github.donnie4w.jdao.base.Table;
import io.github.donnie4w.jdao.util.Logger;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * author: donnie4w <donnie4w@gmail.com>
 * <p>
 * This is the core class of Jdao, providing jdao data CRUD operation functions,
 * transaction, batch processing and other operation functions,
 * data source object DBhandle creation and acquisition,
 * multi-data source add and delete functions
 */
public class Jdao {

    final static String VERTION = "2.0.1";

    private final static String err_noinit = "the jdao DataSource was not initialized(Hint: jdao.init(dataSource, dbtype)) ";

    private Jdao() {
    }

    private static final DBContainer container = new DBContainer();

    private static DBhandle defaultDBhandle;

    /**
     * new DBhandle  by data source
     *
     * @param dataSource
     * @param dbtype
     * @return
     */
    public static DBhandle newDBhandle(DataSource dataSource, DBType dbtype) {
        return new DBhandler(dataSource, dbtype);
    }

    /**
     * Initializes the data source and binds it to a specific database type.
     *
     * @param dataSource The DataSource object representing the database connection pool.
     * @param dbtype     The database type, such as DBType.MYSQL, DBType.POSTGRESQL, etc.
     *<p>
     * Description:
     *   This method sets up the data source to work with the specified database type.
     *   It is typically called at the beginning of the application to configure the data source for use with a specific database.
     *<p>
     * Example:
     * <blockquote><pre>
     *   DataSource dataSource = new BasicDataSource();
     *   dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
     *   dataSource.setUsername("user");
     *   dataSource.setPassword("password");
     *
     *   Jdao.init(dataSource, DBType.MYSQL);
     * </pre></blockquote>
     */
    public static void init(DataSource dataSource, DBType dbtype) {
        defaultDBhandle = newDBhandle(dataSource, dbtype);
    }

    /**
     * Binds a standard entity class to a specific data source and database type.
     *
     * @param clz         The class representing the entity to bind to the data source.
     * @param dataSource  The DataSource object representing the database connection pool.
     * @param dbtype      The database type, such as DBType.MYSQL, DBType.POSTGRESQL, etc.
     *<p>
     * Description:
     *   This method sets up the specified entity class to work with the given data source and database type.
     *   It is typically called at the beginning of the application to configure the entity class for use with a specific database.
     *<p>
     * Example:
     * <p> Assuming Hstest is a class representing the user entity
     *  <blockquote><pre>
     *   DataSource dataSource = new BasicDataSource();
     *   dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
     *   dataSource.setUsername("user");
     *   dataSource.setPassword("password");
     *
     *
     *   Jdao.bindDataSource(Hstest.class, dataSource, DBType.MYSQL);
     *   </pre></blockquote>
     */
    public static void bindDataSource(Class<? extends Table<?>> clz, DataSource dataSource, DBType dbtype) {
        container.bind(clz, dataSource, dbtype);
    }

    /**
     * remove DataSource by class
     *
     * @param clz
     */
    public static void unbindDataSource(Class<?> clz) {
        container.unbind(clz);

    }

    /**
     * Check whether the class has a set data source and return the data source
     *
     * @param clz
     * @return
     */
    public static DBhandle getDBhandle(Class<?> clz) {
        return container.getDBhandle(clz);
    }

    /**
     * Check whether the package has a set data source and return the data source
     *
     * @param packageName
     * @return DBhandle
     */
    public static DBhandle getDBhandle(String packageName) {
        return container.getDBhandle(packageName);
    }


    public static DBhandle getMapperDBhandle(String namespace,String id) {
        return container.getMapperDBhandle(namespace,id);
    }


    /**
     * Binds all standard entity classes within a specified package to a data source and database type.
     *
     * @param packageName The name of the package containing the entity classes to bind.
     * @param dataSource  The DataSource object representing the database connection pool.
     * @param dbtype      The database type, such as DBType.MYSQL, DBType.POSTGRESQL, etc.
     *<p>
     * Description:
     *   This method sets up all entity classes within the specified package to work with the given data source and database type.
     *<p>
     * Example:
     * <p> Assuming "com.example.entities" is the package containing the entity classes
     *   <blockquote><pre>
     *   DataSource dataSource = new BasicDataSource();
     *   dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
     *   dataSource.setUsername("user");
     *   dataSource.setPassword("password");
     *
     *   Jdao.bindDataSource("com.example.entities", dataSource, DBType.MYSQL);
     *  </pre></blockquote>
     */
    public static void bindDataSource(String packageName, DataSource dataSource, DBType dbtype) {
        container.bind(packageName, dataSource, dbtype);
    }

    /**
     * remove DataSource by packageName
     *
     * @param packageName
     */
    public static void unbindDataSource(String packageName) {
        container.unbind(packageName);
    }


    /**
     * Binds the specified XML mapping namespace to a data source and database type.
     *
     * @param namespace   The namespace in the XML mapping files that corresponds to the tables and queries to bind.
     * @param dataSource  The DataSource object representing the database connection pool.
     * @param dbtype      The database type, such as DBType.MySQL, DBType.PostgreSQL, etc.
     *<p>
     * Description:
     *   This method sets up the specified XML mapping namespace to work with the given data source and database type.
     *<p>
     * Example:
     *<p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *  <blockquote><pre>
     *   DataSource dataSource = new BasicDataSource();
     *   dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
     *   dataSource.setUsername("user");
     *   dataSource.setPassword("password");
     *
     *   Jdao.bindMapperDataSource("com.example.mappers.users", dataSource, DBType.MYSQL);
     *  </pre></blockquote>
     */
    public static boolean bindMapperDataSource(String namespace, DataSource dataSource, DBType dbtype) {
        return  container.bindMapper(namespace, dataSource, dbtype);
    }


    public static void unbindMapperDataSource(String namespace){
        container.unbindMapper(namespace);
    }

    /**
     * Binds the specified XML mapping namespace and CRUD tag id to a data source and database type.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the tables and queries to bind.
     * @param id        The ID of the CRUD tag within the namespace to bind.
     * @param dataSource The DataSource object representing the database connection pool.
     * @param dbtype    The database type, such as DBType.MySQL, DBType.PostgreSQL, etc.
     *<p>
     * Description:
     *   This method sets up the specified XML mapping namespace and CRUD tag id to work with the given data source and database type.
     *<p>
     * Example:
     * <p>Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p>Assuming "userSelect" is the id of the select tag within the namespace
     *   <blockquote><pre>
     *   DataSource dataSource = new BasicDataSource();
     *   dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
     *   dataSource.setUsername("user");
     *   dataSource.setPassword("password");
     *
     *   Jdao.bindMapperDataSource("com.example.mappers.users", "userCrud", dataSource, DBType.MYSQL);
     *   </pre></blockquote>
     */
    public static boolean bindMapperDataSource(String namespace, String id, DataSource dataSource, DBType dbtype) {
        return container.bindMapper(namespace, id, dataSource, dbtype);
    }


    public static void unbindMapperDataSource(String namespace,String id){
         container.unbindMapper(namespace,id);
    }

    /**
     * Binds the specified mapper interface to a data source and database type, associating it with XML mapping files.
     *
     * @param mapperface The mapper interface class that defines the methods to bind, which correspond to the XML mappings.
     * @param dataSource The DataSource object representing the database connection pool.
     * @param dbtype     The database type, such as DBType.MYSQL, DBType.POSTGRESQL, etc.
     *<p>
     * Description:
     *   This method sets up the specified mapper interface to work with the given data source and database type.
     *<p>
     * Example:
     *<p> Assuming UserMapper is the mapper interface class
     *  <blockquote><pre>
     *   DataSource dataSource = new BasicDataSource();
     *   dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
     *   dataSource.setUsername("user");
     *   dataSource.setPassword("password");
     *
     *   Jdao.bindMapperDataSource(UserMapper.class, dataSource, DBType.MYSQL);
     *   </pre></blockquote>
     */
    public static boolean bindMapperDataSource(Class<?> mapperface, DataSource dataSource, DBType dbtype) {
        return  container.bindMapper(mapperface, dataSource, dbtype);
    }


    public static void unbindMapperDataSource(Class<?> mapperface){
        container.unbindMapper(mapperface);
    }

    /**
     * Gets the new transaction operation object
     *
     * @return
     * @throws JdaoException
     */
    public static Transaction newTransaction() throws SQLException {
        return defaultDBhandle.getJdbcHandle().newTransaction();
    }

    /**
     * reture the default DBhandle , which set with function initDataSource
     *
     * @return
     */
    public static DBhandle getDefaultDBhandle() {
        return defaultDBhandle;
    }


    /**
     * select * from table
     *
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     * @throws JdaoClassException
     * @throws SQLException
     * <P>
     * Example:
     *   <blockquote><pre>
     *   Hstest hstest = Jdao.executeQuery(Hstest.class,"select id,age,rowname,updatetime from hstest where id = ?", 10);
     *   </pre></blockquote>
     */
    public static <T> T executeQuery(Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQuery(clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     */
    public static <T> T executeQuery(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQuery(transaction, clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     * @throws JdaoClassException
     * @throws SQLException
     * <P>
     * Example:
     *   <blockquote><pre>
     *   List<Hstest> list = Jdao.executeQueryList(Hstest.class,"select id,age,rowname,updatetime from hstest limit ?", 10);
     *   </pre></blockquote>
     */
    public static <T> List<T> executeQueryList(Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryList(clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param clz
     * @param sql
     * @param values
     * @param <T>
     * @return List<T>
     * @throws JdaoException
     * @throws JdaoClassException
     * @throws SQLException
     */
    public static <T> List<T> executeQueryList(Transaction transaction, Class<T> clz, String sql, Object... values) throws JdaoException, JdaoClassException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryList(transaction, clz, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param sql
     * @param values
     * @return DataBean
     * @throws JdaoException
     */
    public static DataBean executeQueryBean(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBean(transaction, sql, values);
    }

    /**
     * select * from table
     *
     * @param sql
     * @param values
     * @return DataBean
     * @throws JdaoException
     * @throws SQLException
     * <P>
     * Example:
     *   <blockquote><pre>
     *   DataBean dataBean = Jdao.executeQueryBean("select id,age,rowname,updatetime from hstest where id = ?", 15);
     *   </pre></blockquote>
     */
    public static DataBean executeQueryBean(String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBean(null, sql, values);
    }

    /**
     * select * from table
     *
     * @param transaction
     * @param sql
     * @param values
     * @return List<DataBean>
     * @throws JdaoException
     * @throws SQLException
     */
    public static List<DataBean> executeQueryBeans(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBeans(transaction, sql, values);
    }

    /**
     * select * from table
     *
     * @param sql
     * @param values
     * @return List<DataBean>
     * @throws JdaoException
     * @throws SQLException
     * <P>
     * Example:
     *   <blockquote><pre>
     *   List<DataBean> list = Jdao.executeQueryBeans("select id,age,rowname,updatetime from hstest limit ?", 10);
     *   </pre></blockquote>
     */
    public static List<DataBean> executeQueryBeans(String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeQueryBeans(null, sql, values);
    }

    /**
     * update  insert into  delete
     *
     * @param transaction
     * @param sql
     * @param values
     * @return int
     * @throws JdaoException
     * @throws SQLException
     */
    public static int executeUpdate(Transaction transaction, String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeUpdate(transaction, sql, values);
    }

    /**
     * update  insert into  delete
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     * @throws SQLException
     * <P>
     * Example:
     *   <blockquote><pre>
     *   List<DataBean> list = Jdao.executeUpdate("update hstest set rowname = ? where id = ?", "hello world" , 1);
     *   </pre></blockquote>
     */
    public static int executeUpdate(String sql, Object... values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeUpdate(null, sql, values);
    }

    /**
     * insert into
     *
     * @param transaction
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     * @throws SQLException
     */
    public static int[] executeBatch(Transaction transaction, String sql, List<Object[]> values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeBatch(transaction, sql, values);
    }

    /**
     * insert into
     *
     * @param sql
     * @param values
     * @return
     * @throws JdaoException
     * @throws SQLException
     * <P>
     * Example:
     *   <blockquote><pre>
     *   List<Object[]> list = new ArrayList<>();
     *   list.add(new Object[]{"111", "aaa"});
     *   list.add(new Object[]{"222", "bbb"});
     *   Jdao.executeBatch("insert into hstest1(`rowname`,`value`) values(?,?)", list);
     *   </pre></blockquote>
     */
    public static int[] executeBatch(String sql, List<Object[]> values) throws JdaoException, SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeBatch(null, sql, values);
    }


    /**
     * Store Procedure operations
     * @param procedureName  StoreProcedure Name
     * @param params
     * @return
     * @throws SQLException
     */
    public static Map<Integer, Object> executeCall(String procedureName, Params... params) throws SQLException {
        notnull(defaultDBhandle, err_noinit);
        return defaultDBhandle.executeCall(procedureName, params);
    }

    private static void notnull(Object obj, String message) {
        if (obj == null) {
            throw new JdaoRuntimeException(message);
        }
    }

    public static void setLogger(boolean on) {
        Logger.setLogger(on);
    }

}