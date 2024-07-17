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

import io.github.donnie4w.jdao.handle.JdaoException;
import io.github.donnie4w.jdao.handle.Transaction;

import java.util.List;

public interface Mapper {

    boolean isAutocommit();

    void setAutocommit(boolean on) throws JdaoException;

    void useTransaction(Transaction transaction);

    void rollback() throws JdaoException;

    void commit() throws JdaoException;

    <T> T selectOne(String mapperId, Object... param) throws JdaoException;

    <T> T selectOne(String mapperId, Object param) throws JdaoException;

    <T> T[] selectArray(String mapperId, Object... param) throws JdaoException;

    <T> T[] selectArray(String mapperId, Object param) throws JdaoException;

    <T> List<T> selectList(String mapperId, Object... param) throws JdaoException;

    <T> List<T> selectList(String mapperId, Object param) throws JdaoException;

    int insert(String mapperId, Object... param) throws JdaoException;

    int insert(String mapperId, Object param) throws JdaoException;

    int update(String mapperId, Object... param) throws JdaoException;

    int update(String mapperId, Object param) throws JdaoException;

    int delete(String mapperId, Object... param) throws JdaoException;

    int delete(String mapperId, Object param) throws JdaoException;

}
