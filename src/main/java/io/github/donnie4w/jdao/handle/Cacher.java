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

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cacher implements Cache {

    private final Map<String, CacheHandle> cacheMap = new ConcurrentHashMap();
    private final Map<Object, String> rmap = new ConcurrentHashMap();
    private final CacheHandle defaultCacheHandle = new CacheHandle();


    public Cacher() {
        new ClearThread().start();
    }

    public void register(String packageName) {
        rmap.put(packageName, defaultCacheHandle.getDomain());
    }

    public void register(String packageName, CacheHandle cacheHandle) {
        rmap.put(packageName, cacheHandle.getDomain());
        cacheMap.put(cacheHandle.getDomain(), cacheHandle);
    }

    public void register(Class<? extends Table> clazz) {
        rmap.put(clazz, defaultCacheHandle.getDomain());
    }

    public void register(Class<? extends Table> clazz, CacheHandle cacheHandle) {
        rmap.put(clazz, cacheHandle.getDomain());
        cacheMap.put(cacheHandle.getDomain(), cacheHandle);
    }

    public void remove(String packageName) {
        rmap.remove(packageName);
    }

    public void remove(Class<?> clazz) {
        rmap.remove(clazz);
    }

    public boolean registerMapper(Class<?> mapperface) {
        if (mapperface.isInterface()) {
            for (Method m : mapperface.getMethods()) {
                registerMapper(mapperface.getName(), m.getName());
            }
            return true;
        }
        return false;
    }

    public boolean registerMapper(Class<?> mapperface, CacheHandle cacheHandle) {
        if (mapperface.isInterface()) {
            for (Method m : mapperface.getMethods()) {
                registerMapper(mapperface.getName(), m.getName(), cacheHandle);
            }
            return true;
        }
        return false;
    }

    public boolean registerMapper(String namespace) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                register(namespace.concat(".").concat(name));
            }
            return true;
        }
        return false;
    }

    public boolean registerMapper(String namespace, CacheHandle cacheHandle) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                register(namespace.concat(".").concat(name), cacheHandle);
            }
            return true;
        }
        return false;
    }

    public boolean registerMapper(String namespace, String id) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                if (id.equals(name)) {
                    register(namespace.concat(".").concat(name));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean registerMapper(String namespace, String id, CacheHandle cacheHandle) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                if (id.equals(name)) {
                    register(namespace.concat(".").concat(name), cacheHandle);
                    return true;
                }
            }
        }
        return false;
    }

    public void removeMapper(Class<?> mapperface) {
        if (mapperface.isInterface()) {
            removeMapper(mapperface.getName());
        }
    }

    public void removeMapper(String namespace) {
        List<String> list = MapperParser.getMapperIds(namespace);
        if (list != null) {
            for (String name : list) {
                remove(namespace.concat(".").concat(name));
            }
        }
    }

    public void removeMapper(String namespace, String id) {
        remove(namespace.concat(".").concat(id));
    }


    public String getDomain(String packageName, Class<?> clazz) {
        String domain = null;
        if (clazz != null) {
            domain = rmap.get(clazz);
        }
        if (domain == null) {
            return rmap.get(packageName);
        }
        return domain;
    }


    public Object getCache(String domain, Class<?> clazz, Condition condition) {
        if (domain == null || domain.length() == 0) {
            domain = defaultCacheHandle.getDomain();
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

    public void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result) {
        CacheHandle ch = null;
        if (domain == null || domain.length() == 0) {
            ch = defaultCacheHandle;
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

    public void clearCache(String domain) {
        CacheHandle cb = cacheMap.get(domain);
        if (cb != null)
            cb.map.clear();
    }

    public void clearCache(String domain, Class<?> clazz, String node) {
        if (domain == null || domain.length() == 0) domain = defaultCacheHandle.getDomain();
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
