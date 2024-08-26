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

import io.github.donnie4w.jdao.handle.*;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

public interface JStruct<T> extends Scanner<T>, JdaoSerializa, Serializable {

    T useMaster(boolean useMaster);

    T useTransaction(Transaction transaction);

    T useDataSource(DataSource dataSource, DBType dbType);

    T useDBhandle(DBhandle dbhandle);

    T useCache();

    T useCache(boolean use);

    T useCommentLine(String commentLine);

    T where(Where<T>... wheres);

    T orderBy(Sort<T>... sorts);

    T groupBy(Fields<T>... fields);

    T having(Where<T>... wheres);

    T limit(int limit);

    T limit(int offset, int limit);

    List<T> selects(Field<T>... fs) throws JdaoException, JdaoClassException, SQLException;

    T select(Fields<T>... fs) throws JdaoException, JdaoClassException, SQLException;

    int insert() throws JdaoException, SQLException;

    int update() throws JdaoException, SQLException;

    int delete() throws JdaoException, SQLException;

    void addBatch();

    int[] executeBatch() throws JdaoException, SQLException;

    void toJdao();

    void reset();

    String TableName();
}
