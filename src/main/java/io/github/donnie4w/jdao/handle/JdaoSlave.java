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

    public static void bindClass(Class<?> clz, DataSource dataSource, DBType dbtype) {
        defaultSlaveHandler.bindClass(clz, dataSource, dbtype);
    }

    public static void bindClass(Class<?> clz, DBhandle dBhandle) {
        defaultSlaveHandler.bindClass(clz, dBhandle);
    }

    public static void bindMapper(String mapperId, DataSource dataSource, DBType dbtype) {
        defaultSlaveHandler.bindMapper(mapperId, dataSource, dbtype);
    }

    public static void bindMapper(String mapperId, DBhandle dBhandle) {
        defaultSlaveHandler.bindMapper(mapperId, dBhandle);
    }


    public static void removeClass(Class<?> clz) {
        defaultSlaveHandler.removeClass(clz);
    }

    public static void removePackage(String packageName) {
        defaultSlaveHandler.removePackage(packageName);
    }

    public static void removeMapperId(String mapperId) {
        defaultSlaveHandler.removeMapperId(mapperId);
    }

    public static DBhandle get(Class<?> clz, String packageName, String mapperId) {
        return defaultSlaveHandler.get(clz, packageName, mapperId);
    }

}
