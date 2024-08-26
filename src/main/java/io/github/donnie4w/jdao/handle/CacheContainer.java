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

    private final static String MAPPER_PRE = String.valueOf(System.nanoTime());

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
            for (String id : list) {
                bind(mapperId(namespace, id));
            }
            return true;
        }
        return false;
    }

    boolean bindMapper(String namespace, CacheHandle handle) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String id : list) {
                bind(mapperId(namespace, id), handle);
            }
            return true;
        }
        return false;
    }

    void unbindMapper(String namespace) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String id : list) {
                unbind(mapperId(namespace, id));
            }
        }
    }

    boolean bindMapper(String namespace, String id) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                if (id.equals(name)) {
                    bind(mapperId(namespace, id));
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
                    bind(mapperId(namespace, id), handle);
                    return true;
                }
            }
        }
        return false;
    }

    void unbindMapper(String namespace, String id) {
        unbind(mapperId(namespace, id));
    }

    boolean bindMapper(Class<?> mapperface) {
        if (mapperface.isInterface()) {
            String namespace = mapperface.getName();
            for (Method m : mapperface.getMethods()) {
                bind(mapperId(namespace, m.getName()));
            }
            return true;
        }
        return false;
    }

    boolean bindMapper(Class<?> mapperface, CacheHandle handle) {
        if (mapperface.isInterface()) {
            String namespace = mapperface.getName();
            for (Method m : mapperface.getMethods()) {
                bind(mapperId(namespace, m.getName()), handle);
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
                for (String id : list) {
                    unbind(mapperId(namespace, id));
                }
            }
        }
    }

    String getDomain(Class<?> clazz) {
        String domain = null;
        if (clazz != null) {
            domain = rmap.get(clazz);
        }
        if (domain == null) {
            return rmap.get(Utils.getPackageName(clazz));
        }
        return domain;
    }

    String getDomain(String namespace, String id) {
        if (Utils.stringValid(namespace) && Utils.stringValid(id)) {
            return rmap.get(mapperId(namespace, id));
        }
        return null;
    }

    Object getCache(String domain, String namespace, String id, Condition condition) {
        return getCache(domain, mapperId(namespace, id), condition);
    }

    Object getCache(String domain, Object object, Condition condition) {
        if (!Utils.stringValid(domain)) {
            domain = cacheHandle.getDomain();
        }
        CacheHandle ch = cacheMap.get(domain);
        if (ch == null || condition == null) {
            return null;
        }
        Map<Condition, CacheBean> map = ch.map.get(object);
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

    void setCache(String domain, String namespace, String id, Condition condition, Object result) {
        setCache(domain, mapperId(namespace, id), condition, result);
    }

    void setCache(String domain, Object cacheId, Condition condition, Object result) {
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
        if (ch.map.containsKey(cacheId)) {
            ch.map.get(cacheId).put(condition, new CacheBean(t, o));
        } else {
            Map<Condition, CacheBean> map = new ConcurrentHashMap();
            map.put(condition, new CacheBean(t, o));
            ch.map.put(cacheId, map);
        }
    }

//    void clearCache(String domain) {
//        CacheHandle cb = cacheMap.get(domain);
//        if (cb != null)
//            cb.map.clear();
//    }

    boolean clearCache(String domain, String namespace, String id, String node) {
        return clearCache(domain, mapperId(namespace, id), node);
    }

    boolean clearCache(String domain, Object object, String node) {
        if (!Utils.stringValid(domain)) domain = cacheHandle.getDomain();
        CacheHandle cb = cacheMap.get(domain);
        if (cb != null && cb.map.containsKey(object)) {
            Map<Condition, CacheBean> cbm = cb.map.get(object);
            if (node == null) {
                cbm.clear();
                return true;
            } else {
                for (Condition o : cbm.keySet()) {
                    if (node.equals(o.getNode())) {
                        cbm.remove(o);
                    }
                }
                return true;
            }
        }
        return false;
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

    static String mapperId(String namespace, String id) {
        return MAPPER_PRE.concat(namespace).concat(".").concat(id);
    }

}

