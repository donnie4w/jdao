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

import javax.sql.DataSource;

public class JdaoSlave {

    protected JdaoSlave() {
    }

    private static final SlaveHandler defaultSlaveHandler = new SlaveHandler();

    public static int size() {
        return defaultSlaveHandler.size();
    }

    /**
     * bind slave datasource  for table dao package
     *
     * @param packageName
     * @param dataSource
     * @param dbtype
     */
    public static void bindPackage(String packageName, DataSource dataSource, DBType dbtype) {
        defaultSlaveHandler.bindPackage(packageName, dataSource, dbtype);
    }

    public static void bindPackage(String packageName, DBhandle dBhandle) {
        defaultSlaveHandler.bindPackage(packageName, dBhandle);
    }

    public static void bindClass(Class<? extends Table<?>> clz, DataSource dataSource, DBType dbtype) {
        defaultSlaveHandler.bindClass(clz, dataSource, dbtype);
    }

    public static void bindClass(Class<? extends Table<?>> clz, DBhandle dBhandle) {
        defaultSlaveHandler.bindClass(clz, dBhandle);
    }

    public static boolean bindMapper(String namespace, String id, DataSource dataSource, DBType dbtype) {
        return defaultSlaveHandler.bindMapper(namespace, id, dataSource, dbtype);
    }

    public static boolean bindMapper(String namespace, String id, DBhandle dBhandle) {
        return defaultSlaveHandler.bindMapper(namespace, id, dBhandle);
    }

    public static void unbindMapper(String namespace, String id) {
        defaultSlaveHandler.unbindMapper(namespace, id);
    }


    public static boolean bindMapper(Class<?> clz, DBhandle dBhandle) {
        return defaultSlaveHandler.bindMapper(clz, dBhandle);
    }

    public static boolean bindMapper(Class<?> clz, DataSource dataSource, DBType dbtype) {
        return defaultSlaveHandler.bindMapper(clz, dataSource, dbtype);
    }

    public static void unbindMapper(Class<?> clz) {
        defaultSlaveHandler.unbindMapper(clz);
    }

    public static boolean bindMapper(String namespace, DataSource dataSource, DBType dbtype) {
        return defaultSlaveHandler.bindMapper(namespace, dataSource, dbtype);
    }

    public static boolean bindMapper(String namespace, DBhandle dBhandle) {
        return defaultSlaveHandler.bindMapper(namespace, dBhandle);
    }

    public static void unbindMapper(String namespace) {
        defaultSlaveHandler.unbindMapper(namespace);
    }

    public static void unbindClass(Class<? extends Table<?>> clz) {
        defaultSlaveHandler.unbindClass(clz);
    }

    public static void unbindPackage(String packageName) {
        defaultSlaveHandler.unbindPackage(packageName);
    }

    public static DBhandle get(Class<? extends Table<?>> clz, String packageName) {
        return defaultSlaveHandler.get(clz, packageName);
    }

    public static DBhandle getMapper(String namespace, String id) {
        return defaultSlaveHandler.getMapper(namespace, id);
    }

}
