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

import io.github.donnie4w.jdao.mapper.MapperParser;
import io.github.donnie4w.jdao.util.Utils;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SlaveHandler implements SlaveHandle {

    private final Map<Object, List<DBhandle>> slaveMap = new ConcurrentHashMap<>();

    private final String MAPPER_PRE = String.valueOf(System.nanoTime());

    public void bindPackage(String packageName, DataSource dataSource, DBType dbtype) {
        getAndset(packageName).add(Jdao.newDBhandle(dataSource, dbtype));
    }

    public void bindPackage(String packageName, DBhandle dbhandle) {
        getAndset(packageName).add(dbhandle);
    }

    public void bindClass(Class<?> clz, DBhandle dbHandle) {
        getAndset(clz).add(dbHandle);
    }

    public void bindClass(Class<?> clz, DataSource dataSource, DBType dbtype) {
        getAndset(clz).add(Jdao.newDBhandle(dataSource, dbtype));
    }

    public boolean bindMapper(String namespace, String id, DBhandle dbhandle) {
        if (MapperParser.getParamBean(namespace, id) != null) {
            getAndset(MAPPER_PRE.concat(namespace).concat(".".concat(id))).add(dbhandle);
            return true;
        }
        return false;
    }

    public boolean bindMapper(String namespace, String id, DataSource dataSource, DBType dbtype) {
        if (MapperParser.getParamBean(namespace, id) != null) {
            getAndset(MAPPER_PRE.concat(namespace).concat(".".concat(id))).add(Jdao.newDBhandle(dataSource, dbtype));
            return true;
        }
        return false;
    }

    public boolean bindMapper(Class<?> clz, DataSource dataSource, DBType dbtype) {
        if (clz.isInterface()) {
            Method[] methods = clz.getMethods();
            if (methods == null || methods.length == 0) {
                return false;
            }
            String namespace = clz.getName();
            for (Method method : methods) {
                bindMapper(namespace, method.getName(), dataSource, dbtype);
            }
            return true;
        }
        return false;
    }

    public boolean bindMapper(Class<?> clz, DBhandle dbHandle) {
        if (clz.isInterface()) {
            Method[] methods = clz.getMethods();
            if (methods == null || methods.length == 0) {
                return false;
            }
            String namespace = clz.getName();
            for (Method method : methods) {
                bindMapper(namespace, method.getName(), dbHandle);
            }
            return true;
        }
        return false;
    }

    public boolean bindMapper(String namespace, DataSource dataSource, DBType dbtype) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                bindMapper(namespace, name, dataSource, dbtype);
            }
            return true;
        }
        return false;
    }

    public boolean bindMapper(String namespace, DBhandle dbHandle) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                bindMapper(namespace, name, dbHandle);
            }
            return true;
        }
        return false;
    }


    public void unbindPackage(String packageName) {
        slaveMap.remove(packageName);
    }

    public void unbindMapper(String namespace, String id) {
        slaveMap.remove(MAPPER_PRE.concat(namespace).concat(".".concat(id)));
    }

    public void unbindClass(Class<?> clz) {
        slaveMap.remove(clz);
    }

    public void unbindMapper(Class<?> clz) {
        if (clz.isInterface()) {
            Method[] methods = clz.getMethods();
            if (methods == null || methods.length == 0) {
                return;
            }
            String namespace = clz.getName();
            for (Method method : methods) {
                unbindMapper(namespace, method.getName());
            }
        }
    }

    public void unbindMapper(String namespace){
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                unbindMapper(namespace, name);
            }
        }
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


    public  int size() {
        return slaveMap.size();
    }


    public DBhandle get(Class<?> clz, String packageName) {
        DBhandle dbHandle = null;
        if (clz != null) {
            dbHandle = getList(slaveMap.get(clz));
        }
        if (dbHandle == null && packageName != null) {
            dbHandle = getList(slaveMap.get(packageName));
        }
        return dbHandle;
    }


    public DBhandle getMapper(String namespace, String id) {
        if (Utils.stringValid(namespace) && Utils.stringValid(id)) {
            return getList(slaveMap.get(MAPPER_PRE.concat(namespace).concat(".").concat(id)));
        }
        return null;
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
