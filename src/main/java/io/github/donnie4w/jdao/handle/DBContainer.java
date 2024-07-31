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
import io.github.donnie4w.jdao.mapper.MapperParser;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Container for database-related objects.
 */
public class DBContainer {

    private final String MAPPER_PRE =  String.valueOf(System.nanoTime());;

    private final Map<Object, DBhandle> dbhandleMap = new ConcurrentHashMap<>();


    void bind(Class<? extends Table<?>> clz, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(clz, Jdao.newDBhandle(dataSource, dbtype));
    }

    void unbind(Class<?> clz) {
        dbhandleMap.remove(clz);
    }

    DBhandle getDBhandle(Class<?> clz) {
        return dbhandleMap.get(clz);
    }

    DBhandle getDBhandle(String packageName) {
        return dbhandleMap.get(packageName);
    }

    DBhandle getMapperDBhandle(String namespace,String id) {
        return dbhandleMap.get(MAPPER_PRE.concat(namespace).concat(".").concat(id));
    }

    void bind(String packageName, DataSource dataSource, DBType dbtype) {
        dbhandleMap.put(packageName, Jdao.newDBhandle(dataSource, dbtype));
    }

    void unbind(String packageName) {
        dbhandleMap.remove(packageName);
    }

    boolean bindMapper(String namespace, DataSource dataSource, DBType dbtype) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                dbhandleMap.put(MAPPER_PRE.concat(namespace).concat(".").concat(name), Jdao.newDBhandle(dataSource, dbtype));
            }
            return true;
        }
        return false;
    }

    void unbindMapper(String namespace) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                dbhandleMap.remove(MAPPER_PRE.concat(namespace).concat(".").concat(name));
            }
        }
    }

    boolean bindMapper(String namespace, String id, DataSource dataSource, DBType dbtype) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                if (id.equals(name)) {
                    dbhandleMap.put(MAPPER_PRE.concat(namespace).concat(".").concat(id), Jdao.newDBhandle(dataSource, dbtype));
                    return true;
                }
            }
        }
        return false;
    }

    void unbindMapper(String namespace, String id) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                if (id.equals(name)) {
                    dbhandleMap.remove(MAPPER_PRE.concat(namespace).concat(".").concat(id));
                }
            }
        }
    }

    boolean bindMapper(Class<?> mapperface, DataSource dataSource, DBType dbtype) {
        String namespace = mapperface.getName();
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                dbhandleMap.put(MAPPER_PRE.concat(namespace).concat(".").concat(name), Jdao.newDBhandle(dataSource, dbtype));
            }
            return true;
        }
        return false;
    }

    void unbindMapper(Class<?> mapperface) {
        String namespace = mapperface.getName();
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                dbhandleMap.remove(MAPPER_PRE.concat(namespace).concat(".").concat(name));
            }
        }
    }

}
