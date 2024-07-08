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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SlaveSource {
    private static final Map<Object, List<DBhandle>> dbhandleSlaveMap = new ConcurrentHashMap<>();

    static void add(String packageName, DataSource dataSource, DBType dbtype) {
        getAndset(packageName).add(Jdao.newDBhandle(dataSource, dbtype));
    }

    static void add(String packageName, DBhandle dbhandle) {
        getAndset(packageName).add(dbhandle);
    }

    private static List<DBhandle> getAndset(String packageName) {
        if (!dbhandleSlaveMap.containsKey(packageName)) {
            synchronized (SlaveSource.class) {
                if (!dbhandleSlaveMap.containsKey(packageName)) {
                    dbhandleSlaveMap.put(packageName, new ArrayList<DBhandle>());
                }
            }
        }
        return  dbhandleSlaveMap.get(packageName);
    }

    private static List<DBhandle> getAndset(Object obj) {
        if (!dbhandleSlaveMap.containsKey(obj)) {
            synchronized (SlaveSource.class) {
                if (!dbhandleSlaveMap.containsKey(obj)) {
                    dbhandleSlaveMap.put(obj, new ArrayList<DBhandle>());
                }
            }
        }
        return  dbhandleSlaveMap.get(obj);
    }

    static void remove(String packageName) {
        dbhandleSlaveMap.remove(packageName);
    }

    static void remove(Class<?> clz) {
        dbhandleSlaveMap.remove(clz);
    }

    public static int size() {
        return dbhandleSlaveMap.size();
    }

    static void add(Class<?> clz, DataSource dataSource, DBType dbtype) {
        getAndset(clz).add(Jdao.newDBhandle(dataSource, dbtype));
    }


    static void add(Class<?> clz, DBhandle dbHandle) {
        getAndset(clz).add(dbHandle);
    }

    public static DBhandle get(Class<?> clz, String packageName) {
        DBhandle dbHandle = null;
        if (clz != null) {
            dbHandle = getList(dbhandleSlaveMap.get(clz));
        }
        if (dbHandle == null && packageName != null) {
            dbHandle = getList(dbhandleSlaveMap.get(packageName));
        }
        return dbHandle;
    }

    static Random random = new Random();

    private static <T> T getList(List<T> list) {
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
