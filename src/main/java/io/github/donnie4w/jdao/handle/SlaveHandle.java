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

import javax.sql.DataSource;

public interface SlaveHandle {

    void bindPackage(String packageName, DataSource dataSource, DBType dbtype);

    void bindPackage(String packageName, DBhandle dbhandle);

    void bindClass(Class<?> clz, DBhandle dbHandle);

    void bindClass(Class<?> clz, DataSource dataSource, DBType dbtype);

    boolean bindMapper(String namespace, String id, DBhandle dbhandle);

    boolean bindMapper(String namespace, String id, DataSource dataSource, DBType dbtype);

    boolean bindMapper(Class<?> clz, DataSource dataSource, DBType dbtype);

    boolean bindMapper(Class<?> clz, DBhandle dbHandle);

    boolean bindMapper(String namespace, DataSource dataSource, DBType dbtype);

    boolean bindMapper(String namespace, DBhandle dbHandle);


    void unbindPackage(String packageName);

    void unbindMapper(String namespace, String id);

    void unbindClass(Class<?> clz);

    void unbindMapper(Class<?> clz);

    void unbindMapper(String namespace);

    int size();

    DBhandle get(Class<?> clz, String packageName);

    DBhandle getMapper(String namespace, String id);
}
