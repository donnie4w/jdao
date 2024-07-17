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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SlaveHandler {

    private final Map<Object, List<DBhandle>> slaveMap = new ConcurrentHashMap<>();

    private final String mapperPre = String.valueOf(System.nanoTime());

    void bindPackage(String packageName, DataSource dataSource, DBType dbtype) {
        getAndset(packageName).add(Jdao.newDBhandle(dataSource, dbtype));
    }

    void bindPackage(String packageName, DBhandle dbhandle) {
        getAndset(packageName).add(dbhandle);
    }

    void bindMapper(String mapperId, DataSource dataSource, DBType dbtype) {
        getAndset(mapperPre.concat(mapperId)).add(Jdao.newDBhandle(dataSource, dbtype));
    }

    void bindMapper(String mapperId, DBhandle dbhandle) {
        getAndset(mapperPre.concat(mapperId)).add(dbhandle);
    }

    void bindClass(Class<?> clz, DataSource dataSource, DBType dbtype) {
        if (clz.isInterface()) {
            Method[] methods = clz.getMethods();
            for (Method method : methods) {
                bindMapper(clz.getName().concat(".").concat(method.getName()), dataSource,dbtype);
            }
        } else {
            getAndset(clz).add(Jdao.newDBhandle(dataSource, dbtype));
        }
    }


    void bindClass(Class<?> clz, DBhandle dbHandle) {
        if (clz.isInterface()) {
            Method[] methods = clz.getMethods();
            for (Method method : methods) {
                bindMapper(clz.getName().concat(".").concat(method.getName()), dbHandle);
            }
        } else {
            getAndset(clz).add(dbHandle);
        }
    }



    void removePackage(String packageName) {
        slaveMap.remove(packageName);
    }

    void removeMapperId(String mapperId) {
        slaveMap.remove(mapperPre.concat(mapperId));
    }

    void removeClass(Class<?> clz) {
        slaveMap.remove(clz);
    }

    private List<DBhandle> getAndset(Object obj) {
        if (!slaveMap.containsKey(obj)) {
            synchronized (SlaveHandler.class) {
                if (!slaveMap.containsKey(obj)) {
                    slaveMap.put(obj, new ArrayList<DBhandle>());
                }
            }
        }
        return slaveMap.get(obj);
    }


    int size() {
        return slaveMap.size();
    }


    DBhandle get(Class<?> clz, String packageName,String mapperId) {
        DBhandle dbHandle = null;
        if (clz != null) {
            dbHandle = getList(slaveMap.get(clz));
        }
        if (dbHandle == null && packageName != null) {
            dbHandle = getList(slaveMap.get(packageName));
        }

        if (dbHandle == null && mapperId != null) {
            dbHandle = getList(slaveMap.get(mapperPre.concat(mapperId)));
        }

        return dbHandle;
    }

    Random random = new Random();

    private <T> T getList(List<T> list) {
        if (list != null && list.size() > 0) {
            if (list.size() > 1) {
                return list.get(random.nextInt(list.size()));
            } else {
                return list.get(0);
            }
        }
        return null;
    }
}
