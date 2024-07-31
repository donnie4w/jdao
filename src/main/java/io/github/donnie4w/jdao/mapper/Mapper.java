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

import io.github.donnie4w.jdao.handle.JdaoClassException;
import io.github.donnie4w.jdao.handle.JdaoException;
import io.github.donnie4w.jdao.handle.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface Mapper {

    boolean isAutocommit();

    void setAutocommit(boolean on) throws SQLException;

    void useTransaction(Transaction transaction);

    void rollback() throws SQLException;

    void commit() throws SQLException;

    /**
     * Selects a single row of data based on the specified mapper ID and parameters.
     *
     * @param <T> The type of the result object.
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the query.
     * @return The first row of data matching the query, or null if no data is found.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws JdaoClassException If there is a problem with the class definition related to the query.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method executes a query based on the specified mapper ID and parameters, returning the first row of data found.
     *   If multiple rows match the query, only the first row is returned. If no rows match the query, null is returned.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUserById" is the select id of the mapper within the namespace
     * <blockquote><pre>
     *   jdaoMapper.selectOne("com.example.mappers.users.getUserById", userId);
     * </pre></blockquote>
     */
    <T> T selectOne(String mapperId, Object... param) throws JdaoException, JdaoClassException, SQLException;


    /**
     * Selects a single row of data based on the specified mapper id and a single parameter.
     *
     * @param <T> The type of the result object.
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameter to pass to the query. Can be of basic types, a collection (e.g., Map, List, Set), a JavaBean, or an array.
     * @return The first row of data matching the query, or null if no data is found.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws JdaoClassException If there is a problem with the class definition related to the query.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method executes a query based on the specified mapper ID and a single parameter, returning the first row of data found.
     *   If multiple rows match the query, only the first row is returned. If no rows match the query, null is returned.
     *   The parameter can be of various types, including basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUserById" is the select id of the mapper within the namespace
     * <blockquote><pre>
     *   User user = jdaoMapper.selectOne("com.example.mappers.users.getUserById", userId);
     *</pre></blockquote>
     * <p> Using a Map as the parameter
     * <blockquote><pre>
     *   Map params = new HashMap();
     *   params.put("name", "John Doe");
     *   params.put("age", 30);
     *   User userByParams = jdaoMapper.selectOne("com.example.mappers.users.getUserByNameAndAge", params);
     *</pre></blockquote>
     */
    <T> T selectOne(String mapperId, Object param) throws JdaoException, JdaoClassException, SQLException;


    /**
     * Selects multiple rows of data based on the specified mapper ID and parameters.
     *
     * @param <T> The type of the result objects.
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the query.
     * @return An array of objects representing the rows of data found. If no data is found, an empty array is returned.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws JdaoClassException If there is a problem with the class definition related to the query.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method executes a query based on the specified mapper ID and parameters, returning all rows of data found.
     *   If multiple rows match the query, all rows are returned as an array. If no rows match the query, an empty array is returned.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUsersByNameAndAge" is the ID of the mapper within the namespace
     * <blockquote><pre>
     *   jdaoMapper.selectArray("com.example.mappers.users.getUsersByNameAndAge", "John Doe",30);
     * </pre></blockquote>
     */
    <T> T[] selectArray(String mapperId, Object... param) throws JdaoException, JdaoClassException, SQLException;

    /**
     * Selects multiple rows of data based on the specified mapper ID and parameters.
     *
     * @param <T> The type of the result objects.
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the query.
     * @return An array of objects representing the rows of data found. If no data is found, an empty array is returned.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws JdaoClassException If there is a problem with the class definition related to the query.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method executes a query based on the specified mapper ID and parameters, returning all rows of data found.
     *   If multiple rows match the query, all rows are returned as an array. If no rows match the query, an empty array is returned.
     *   The parameters can be of various types, including basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     *<p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *<p> And "getUsersByName" is the ID of the mapper within the namespace
     * <blockquote><pre>
     *   jdaoMapper.selectArray("com.example.mappers.users.getUsersByName", "John Doe");
     *</pre></blockquote>
     * <p> Using a Map as the parameter
     *<blockquote><pre>
     *   Map params = new HashMap();
     *   params.put("name", "John Doe");
     *   params.put("age", 30);
     *   jdaoMapper.selectArray("com.example.mappers.users.getUsersByNameAndAge", params);
     * </pre></blockquote>
     */
    <T> T[] selectArray(String mapperId, Object param) throws JdaoException, JdaoClassException, SQLException;

    /**
     * Selects multiple rows of data based on the specified mapper ID and parameters, returning the results as a list.
     *
     * @param <T> The type of the result objects.
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the query.
     * @return A list of objects representing the rows of data found. If no data is found, an empty list is returned.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws JdaoClassException If there is a problem with the class definition related to the query.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method executes a query based on the specified mapper ID and parameters, returning all rows of data found as a list.
     *   If multiple rows match the query, all rows are returned as a list. If no rows match the query, an empty list is returned.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUsersByNameAndAge" is the ID of the mapper within the namespace
     *  <blockquote><pre>
     *   List users = jdaoMapper.selectList("com.example.mappers.users.getUsersByNameAndAge", "John Doe",30);
     * </pre></blockquote>
     */
    <T> List<T> selectList(String mapperId, Object... param) throws JdaoException, JdaoClassException, SQLException;

    /**
     * Selects multiple rows of data based on the specified mapper ID and parameters, returning the results as a list.
     *
     * @param <T> The type of the result objects.
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the query.
     * @return A list of objects representing the rows of data found. If no data is found, an empty list is returned.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws JdaoClassException If there is a problem with the class definition related to the query.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method executes a query based on the specified mapper ID and parameters, returning all rows of data found as a list.
     *   If multiple rows match the query, all rows are returned as a list. If no rows match the query, an empty list is returned.
     *   The parameters can be of various types, including basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     *<p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *<p> And "getUsersByName" is the ID of the mapper within the namespace
     * <blockquote><pre>
     *   List users = jdaoMapper.selectList("com.example.mappers.users.getUsersByName", "John Doe");
     *</pre></blockquote>
     *<p> Using a Map as the parameter
     * <blockquote><pre>
     *   Map params = new HashMap();
     *   params.put("name", "John Doe");
     *   params.put("age", 30);
     *   List usersByParams = jdaoMapper.selectList("com.example.mappers.users.getUsersByNameAndAge", params);
     * </pre></blockquote>
     */
    <T> List<T> selectList(String mapperId, Object param) throws JdaoException, JdaoClassException, SQLException;

    /**
     * Inserts data into the database based on the specified mapper ID and parameters.
     *
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the insert operation.
     * @return The number of rows affected by the insert operation.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method performs an insert operation based on the specified mapper ID and parameters.
     *   It inserts the data into the database and returns the number of rows affected by the operation.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     *<p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *<p> And "insertUser" is the insert ID of the mapper within the namespace
     *<blockquote><pre>
     *   int rowsAffected = jdaoMapper.insert("com.example.mappers.users.insertUser", newUser, 30);
     *</pre></blockquote>
     */
    int insert(String mapperId, Object... param) throws JdaoException, SQLException;

    /**
     * Inserts data into the database based on the specified mapper ID and parameters.
     *
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the insert operation. Can include basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     * @return The number of rows affected by the insert operation.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method performs an insert operation based on the specified mapper ID and parameters.
     *   It inserts the data into the database and returns the number of rows affected by the operation.
     *   The parameters can be of various types, including basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     *
     *<p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *<p> And "insertUser" is the insert ID of the mapper within the namespace
     * <blockquote><pre>
     *   User newUser = new User("John Doe", 30);
     *   int rowsAffected = jdaoMapper.insert("com.example.mappers.users.insertUser", newUser);
     *</pre></blockquote>
     *<p> Using a Map as the parameter
     * <blockquote><pre>
     *   Map params = new HashMap();
     *   params.put("name", "Jane Smith");
     *   params.put("age", 25);
     *   int rowsAffected = jdaoMapper.insert("com.example.mappers.users.insertUser", params);
     * </pre></blockquote>
     */
    int insert(String mapperId, Object param) throws JdaoException, SQLException;


    /**
     * Updates data in the database based on the specified mapper ID and parameters.
     *
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the update operation.
     * @return The number of rows affected by the update operation.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method performs an update operation based on the specified mapper ID and parameters.
     *   It updates the data in the database and returns the number of rows affected by the operation.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "updateUser" is the update ID of the mapper within the namespace
     * <blockquote><pre>
     *   int rowsAffected = jdaoMapper.update("com.example.mappers.users.updateUser", 1L, "John Doe", 30);
     * </pre></blockquote>
     */
    int update(String mapperId, Object... param) throws JdaoException, SQLException;

    /**
     * Updates data in the database based on the specified mapper ID and parameters.
     *
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameters to pass to the update operation. Can include basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     * @return The number of rows affected by the update operation.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method performs an update operation based on the specified mapper ID and parameters.
     *   It updates the data in the database and returns the number of rows affected by the operation.
     *   The parameters can be of various types, including basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "updateUser" is the update ID of the mapper within the namespace
     * <blockquote><pre>
     *   User updatedUser = new User(1L, "John Doe", 30);
     *   int rowsAffected = jdaoMapper.update("com.example.mappers.users.updateUser", updatedUser);
     *</pre></blockquote>
     * <p> Using a Map as the parameter
     * <blockquote><pre>
     *   Map params = new HashMap();
     *   params.put("name", "Jane Smith");
     *   params.put("age", 25);
     *   params.put("id", 2L);
     *   int rowsAffectedByParams = jdaoMapper.update("com.example.mappers.users.updateUser", params);
     *</pre></blockquote>
     */
    int update(String mapperId, Object param) throws JdaoException, SQLException;

    /**
     * Deletes data from the database based on the specified mapper ID and parameters.
     *
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameter to pass to the delete operation.
     * @return The number of rows affected by the delete operation.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *   This method performs a delete operation based on the specified mapper ID and parameter.
     *   It deletes the data from the database and returns the number of rows affected by the operation.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "deleteUserById" is the delete ID of the mapper within the namespace
     * <blockquote><pre>
     *   long userId = 1L;
     *   int rowsAffected = jdaoMapper.delete("com.example.mappers.users.deleteUserById", userId);
     * </pre></blockquote>
     */
    int delete(String mapperId, Object... param) throws JdaoException, SQLException;

    /**
     * Deletes data from the database based on the specified mapper ID and parameters.
     *
     * @param mapperId The ID of the mapper within the XML mapping file.
     * @param param The parameter to pass to the delete operation. Can be of basic types, a collection (e.g., Map, List, Set), a JavaBean, or an array.
     * @return The number of rows affected by the delete operation.
     * @throws JdaoException If there is a problem with the JDAO framework.
     * @throws SQLException If there is a problem executing the SQL statement.
     *<p>
     * Description:
     *<p>   This method performs a delete operation based on the specified mapper ID and parameter.
     *<p>   It deletes the data from the database and returns the number of rows affected by the operation.
     *<p>   The parameter can be of various types, including basic types, collections (e.g., Map, List, Set), JavaBeans, or arrays.
     *<p>
     * Example:
     *<blockquote><pre>
     *    JdaoMapper jdaoMapper = JdaoMapper.newInstance();
     *</pre></blockquote>
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "deleteUserById" is the delete ID of the mapper within the namespace
     * <blockquote><pre>
     *   int rowsAffected = jdaoMapper.delete("com.example.mappers.users.deleteUserById", 1);
     *</pre></blockquote>
     * <p> Using a Map as the parameter
     *   <blockquote><pre>
     *   Map params = new HashMap();
     *   params.put("id", 2L);
     *   int rowsAffectedByParams = jdaoMapper.delete("com.example.mappers.users.deleteUserById", params);
     *   </pre></blockquote>
     */
    int delete(String mapperId, Object param) throws JdaoException, SQLException;

}
