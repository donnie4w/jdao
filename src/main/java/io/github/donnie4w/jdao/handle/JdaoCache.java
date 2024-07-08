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
import io.github.donnie4w.jdao.base.Log;
import io.github.donnie4w.jdao.base.Table;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdaoCache {
    private final static Log log = Log.newInstance(true, JdaoCache.class);
    private static final Map<String, CacheHandle> cacheMap = new ConcurrentHashMap();
    private static final Map<Object, String> rmap = new ConcurrentHashMap();
    private static final CacheHandle defaultCacheHandle = new CacheHandle();

    static {
        new ClearThread().start();
    }

    public static void register(String packageName) {
        rmap.put(packageName, defaultCacheHandle.getDomain());
    }

    public static void register(String packageName, CacheHandle cacheHandle) {
        rmap.put(packageName, cacheHandle.getDomain());
        cacheMap.put(cacheHandle.getDomain(), cacheHandle);
    }

    public static void register(Class<?> clazz) {
        rmap.put(clazz, defaultCacheHandle.getDomain());
    }

    public static void register(Class<?> clazz, CacheHandle cacheHandle) {
        rmap.put(clazz, cacheHandle.getDomain());
        cacheMap.put(cacheHandle.getDomain(), cacheHandle);
    }

    public static void remove(String packageName) {
        rmap.remove(packageName);
    }

    public static void remove(Class<?> clazz) {
        rmap.remove(clazz);
    }

    public static String getCacheDomain(String packageName, Class<?> clazz) {
        String domain = null;
        if (clazz != null) {
            domain = rmap.get(clazz);
        }
        if (domain == null) {
            return rmap.get(packageName);
        }
        return domain;
    }


    public static Object getCache(String domain, Class<?> clazz, Condition condition) {
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

    public static void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result) {
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

    public static void clearCache(String domain) {
        CacheHandle cb = cacheMap.get(domain);
        if (cb != null)
            cb.map.clear();
    }

    public static void clearCache(String domain, Class<?> clazz, String node) {
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

    static class ClearThread extends Thread {
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
                log.log("Cache cleanup thread interrupted.");
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.log("Error occurred during cache cleanup.", e.toString());
            }
        }
    }
}
