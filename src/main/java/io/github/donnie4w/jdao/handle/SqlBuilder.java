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

import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

/**
 * SqlBuilder is used for building dynamic SQL queries.
 */
public abstract class SqlBuilder {

    /**
     * Sets the database handle to be used by the current SQL builder.
     * This allows the builder to make appropriate adjustments based on different database handles.
     *
     * @param dBhandle The database handle to use
     */
    public abstract void useDBhandle(DBhandle dBhandle);

    /**
     * Appends a piece of text to the current SQL statement.
     *
     * @param text The string to append
     * @return The SqlBuilder instance itself, supporting method chaining
     */
    public abstract SqlBuilder append(String text);

    /**
     * Appends a piece of text with parameters to the current SQL statement.
     *
     * @param text The string to append
     * @param params A variadic list of values that may be needed for subsequent parameters
     * @return The SqlBuilder instance itself, supporting method chaining
     */
    public abstract SqlBuilder append(String text, Object... params);

    /**
     * Conditionally appends a piece of text to the current SQL statement.
     *
     * @param expression The boolean expression string used to determine whether to append the text
     * @param context The object used to evaluate the expression
     * @param text The string to append if the condition is met
     * @param params A variadic list of values that may be needed for subsequent parameters
     * @return The SqlBuilder instance itself, supporting method chaining
     */
    public abstract SqlBuilder appendIf(String expression, Object context, String text, Object... params);

    /**
     * Conditionally appends different parts to the current SQL statement based on choices.
     *
     * @param object The object used to evaluate the choice logic
     * @param chooseBuilderConsumer A function that defines the content of different choice branches
     * @return The SqlBuilder instance itself, supporting method chaining
     */
    public abstract SqlBuilder appendChoose(Object object, Consumer<ChooseBuilder> chooseBuilderConsumer);

    /**
     * Appends a loop structure to the current SQL statement.
     *
     * @param collectionName The name of the variable used for iterating over a collection
     * @param context The object used to evaluate the loop logic
     * @param item The name of the variable representing each element within the loop
     * @param separator The text between loop iterations
     * @param open The opening text of the loop body
     * @param close The closing text of the loop body
     * @param foreachConsumer A function that defines the content of the loop body
     * @return The SqlBuilder instance itself, supporting method chaining
     */
    public abstract SqlBuilder appendForeach(String collectionName, Object context, String item, String separator, String open, String close, Consumer<ForeachBuilder> foreachConsumer);

    /**
     * Appends a trimming structure to the current SQL statement to remove extra prefixes or suffixes.
     *
     * @param prefix The trimming prefix text
     * @param suffix The trimming suffix text
     * @param prefixOverrides The text used to override the prefix
     * @param suffixOverrides The text used to override the suffix
     * @param contentBuilder A function that defines the trimming content
     * @return The SqlBuilder instance itself, supporting method chaining
     */
    public abstract SqlBuilder appendTrim(String prefix, String suffix, String prefixOverrides, String suffixOverrides, Consumer<SqlBuilder> contentBuilder);

    /**
     * Appends a SET clause to the current SQL statement.
     *
     * @param contentBuilder A function that defines the content of the SET clause
     * @return The SqlBuilder instance itself, supporting method chaining
     */
    public abstract SqlBuilder appendSet(Consumer<SqlBuilder> contentBuilder);

    /**
     * Retrieves the final constructed SQL statement.
     *
     * @return A string representing the complete SQL statement
     */
    public abstract String getSql();

    /**
     * Retrieves all collected parameters during the construction process.
     *
     * @return An array of objects containing all parameters
     */
    public abstract Object[] getParameters();

    /**
     * Creates a new SqlBuilder instance.
     *
     * @return A new SqlBuilder instance
     */
    public static SqlBuilder newInstance() {
        return new SqlBuildHandler();
    }

    /**
     * Adds parameters to the current SQL builder.
     *
     * @param params The parameters to add
     */
    protected abstract void addParameters(Object... params);

    /**
     * Executes a SQL query and returns the first record.
     *
     * @return The first record of the query result
     * @throws JdaoException If a Jdao exception occurs during execution
     * @throws SQLException If a SQL exception occurs during execution
     */
    public abstract DataBean selectOne() throws JdaoException, SQLException;

    /**
     * Executes a SQL query and returns all records.
     *
     * @return A list of all records of the query result
     * @throws JdaoException If a Jdao exception occurs during execution
     * @throws SQLException If a SQL exception occurs during execution
     */
    public abstract List<DataBean> selectList() throws JdaoException, SQLException;

    /**
     * Executes a SQL statement and returns the number of affected rows.
     *
     * @return The number of affected rows
     * @throws JdaoException If a Jdao exception occurs during execution
     * @throws SQLException If a SQL exception occurs during execution
     */
    public abstract int exec() throws JdaoException, SQLException;
}