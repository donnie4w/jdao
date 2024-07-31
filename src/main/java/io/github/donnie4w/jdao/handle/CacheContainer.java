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

import io.github.donnie4w.jdao.base.Condition;
import io.github.donnie4w.jdao.base.Table;
import io.github.donnie4w.jdao.mapper.MapperParser;
import io.github.donnie4w.jdao.util.Logger;
import io.github.donnie4w.jdao.util.Utils;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a container for cache items.
 */
public class CacheContainer {

    private final String MAPPER_PRE = String.valueOf(System.nanoTime());

    private final Map<String, CacheHandle> cacheMap = new ConcurrentHashMap();

    private final Map<Object, String> rmap = new ConcurrentHashMap();

    private final CacheHandle cacheHandle;

    /**
     * Constructs a new CacheContainer instance with the given CacheHandle.
     *
     * @param cacheHandle the CacheHandle to use for this container.
     */
    public CacheContainer(CacheHandle cacheHandle) {
        this.cacheHandle = cacheHandle;
        new ClearThread().start();
    }

    void bind(String s) {
        rmap.put(s, cacheHandle.getDomain());
    }

    void bind(String s, CacheHandle cacheHandle) {
        rmap.put(s, cacheHandle.getDomain());
        cacheMap.put(cacheHandle.getDomain(), cacheHandle);
    }

    void unbind(String s) {
        rmap.remove(s);
    }

    void bind(Class<?> clazz) {
        rmap.put(clazz, cacheHandle.getDomain());
    }

    void bind(Class<?> clazz, CacheHandle cacheHandle) {
        rmap.put(clazz, cacheHandle.getDomain());
    }

    void unbind(Class<?> clazz) {
        rmap.remove(clazz);
    }

    void bindPackage(String packageName) {
        bind(packageName);
    }

    void bindPackage(String packageName, CacheHandle handle) {
        bind(packageName, handle);
    }

    void unbindPackage(String packageName) {
        unbind(packageName);
    }

    void bindClass(Class<?> clazz) {
        bind(clazz);
    }

    void bindClass(Class<?> clazz, CacheHandle handle) {
        bind(clazz, handle);
    }

    void unbindClass(Class<?> clazz) {
        unbind(clazz);
    }

    boolean bindMapper(String namespace) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                bind(MAPPER_PRE.concat(namespace).concat(".").concat(name));
            }
            return true;
        }
        return false;
    }

    boolean bindMapper(String namespace, CacheHandle handle) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                bind(MAPPER_PRE.concat(namespace).concat(".").concat(name), handle);
            }
            return true;
        }
        return false;
    }

    void unbindMapper(String namespace) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                unbind(MAPPER_PRE.concat(namespace).concat(".").concat(name));
            }
        }
    }

    boolean bindMapper(String namespace, String id) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                if (id.equals(name)) {
                    bind(MAPPER_PRE.concat(namespace).concat(".").concat(name));
                    return true;
                }
            }
        }
        return false;
    }

    boolean bindMapper(String namespace, String id, CacheHandle handle) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                if (id.equals(name)) {
                    bind(MAPPER_PRE.concat(namespace).concat(".").concat(name), handle);
                    return true;
                }
            }
        }
        return false;
    }

    void unbindMapper(String namespace, String id) {
        unbind(MAPPER_PRE.concat(namespace).concat(".").concat(id));
    }

    boolean bindMapper(Class<?> mapperface) {
        if (mapperface.isInterface()) {
            String namespace = mapperface.getName();
            for (Method m : mapperface.getMethods()) {
                bind(MAPPER_PRE.concat(namespace).concat(".").concat(m.getName()));
            }
            return true;
        }
        return false;
    }

    boolean bindMapper(Class<?> mapperface, CacheHandle handle) {
        if (mapperface.isInterface()) {
            String namespace = mapperface.getName();
            for (Method m : mapperface.getMethods()) {
                bind(MAPPER_PRE.concat(namespace).concat(".").concat(m.getName()), handle);
            }
            return true;
        }
        return false;
    }

    void unbindMapper(Class<?> mapperface) {
        if (mapperface.isInterface()) {
            String namespace = mapperface.getName();
            List<String> list = MapperParser.getMapperIds(namespace);
            if (list != null) {
                for (String name : list) {
                    unbind(MAPPER_PRE.concat(namespace).concat(".").concat(name));
                }
            }
        }
    }

    String getDomain(String packageName, Class<?> clazz) {
        String domain = null;
        if (clazz != null) {
            domain = rmap.get(clazz);
        }
        if (domain == null) {
            return rmap.get(packageName);
        }
        return domain;
    }

    String getDomain(String namespace, String id) {
        if (Utils.stringValid(namespace)&&Utils.stringValid(id)) {
            return rmap.get(MAPPER_PRE.concat(namespace).concat(".").concat(id));
        }
        return null;
    }

    Object getCache(String domain, Class<?> clazz, Condition condition) {
        if (!Utils.stringValid(domain)) {
            domain = cacheHandle.getDomain();
        }
        CacheHandle ch = cacheMap.get(domain);
        if (ch == null || condition == null) {
            return null;
        }
        Map<Condition, CacheBean> map = ch.map.get(clazz);
        if (map != null) {
            CacheBean cb = map.get(condition);
            if (cb == null) {
                return null;
            }
            if (ch.getExpire() == 0 || Long.compare(System.currentTimeMillis(), (cb.getTimestamp() + ch.getExpire())) == -1) {
                switch (ch.getStoreModel()) {
                    case SOFT:
                        if (cb.getValue() instanceof SoftReference) {
                            return ((SoftReference) cb.getValue()).get();
                        } else {
                            return null;
                        }
                    case WEAK:
                        if (cb.getValue() instanceof WeakReference) {
                            return ((WeakReference) cb.getValue()).get();
                        } else {
                            return null;
                        }
                    default:
                        return cb.getValue();
                }
            } else {
                map.remove(condition);
            }
        }
        return null;
    }

    void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result) {
        CacheHandle ch = null;
        if (!Utils.stringValid(domain)) {
            ch = cacheHandle;
            if (!cacheMap.containsKey(ch.getDomain())) {
                cacheMap.put(ch.getDomain(), ch);
            }
        } else {
            ch = cacheMap.get(domain);
            if (ch == null) {
                ch = new CacheHandle();
                cacheMap.put(domain, ch);
            }
        }
        final long t = System.currentTimeMillis();
        Object o = result;
        switch (ch.getStoreModel()) {
            case SOFT:
                o = new SoftReference(result);
                break;
            case WEAK:
                o = new WeakReference(result);
                break;
            default:
                break;
        }
        if (ch.map.containsKey(clazz)) {
            ch.map.get(clazz).put(condition, new CacheBean(t, o));
        } else {
            Map<Condition, CacheBean> map = new ConcurrentHashMap();
            map.put(condition, new CacheBean(t, o));
            ch.map.put(clazz, map);
        }
    }

    void clearCache(String domain) {
        CacheHandle cb = cacheMap.get(domain);
        if (cb != null)
            cb.map.clear();
    }

    void clearCache(String domain, Class<?> clazz, String node) {
        if (!Utils.stringValid(domain)) domain = cacheHandle.getDomain();
        CacheHandle cb = cacheMap.get(domain);
        if (cb != null && cb.map.containsKey(clazz)) {
            Map<Condition, CacheBean> cbm = cb.map.get(clazz);
            if (node == null) {
                cbm.clear();
            } else {
                for (Condition o : cbm.keySet()) {
                    if (node.equals(o.getNode())) {
                        cbm.remove(o);
                    }
                }
            }
        }
    }


    class ClearThread extends Thread {
        private static final long SLEEP_INTERVAL_MS = 30_000;

        {
            this.setDaemon(true);
            this.setName("ClearThread");
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    cacheMap.values().forEach(cacheHandle -> {
                        long expire = cacheHandle.getExpire();
                        if (expire != 0) {
                            cacheHandle.map.forEach((key, entries) -> {
                                entries.entrySet().removeIf(entry -> {
                                    long createTime = entry.getValue().getTimestamp();
                                    return System.currentTimeMillis() > (expire + createTime);
                                });
                            });
                        }
                    });
                    Thread.sleep(SLEEP_INTERVAL_MS);
                }
            } catch (InterruptedException e) {
                Logger.severe("Cache cleanup thread interrupted.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                Logger.severe("Error occurred during cache cleanup.", e.toString());
            }
        }
    }

}

