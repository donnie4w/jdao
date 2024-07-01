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
package io.github.donnie4w.jdao.base;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Cache {
    private final static Log log = Log.newInstance(false, Cache.class);
    protected static Map<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

    @SuppressWarnings("rawtypes")
    public static Object getCache(String domain, Class<?> clazz, Object condition) {
        if (!cacheMap.containsKey(domain) || condition == null) {
            return null;
        }
        CacheBeen cb = (CacheBeen) cacheMap.get(domain);
        Map<Object, List<?>> map = cb.map.get(clazz);
        if (map != null && map.containsKey(condition)) {
            List<?> list = map.get(condition);
            long createTime = Long.valueOf(list.get(0).toString());
            if (cb.getExpire() == 0 || Long.compare(System.currentTimeMillis(), (createTime + cb.getExpire())) == -1) {
                switch (cb.getStoreModel()) {
                    case STRONG:
                        return list.get(1);
                    case SOFT:
                        if (list.get(1) instanceof SoftReference) {
                            return ((SoftReference) list.get(1)).get();
                        } else {
                            return null;
                        }
                    case WEAK:
                        if (list.get(1) instanceof WeakReference) {
                            return ((WeakReference) list.get(1)).get();
                        } else {
                            return null;
                        }
                    default:
                        return list.get(1);
                }
            } else {
                list = null;
                map.remove(condition);
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void setCache(String domain, Class<Table<?>> clazz, Object condition, Object result) {
        CacheBeen cb = (CacheBeen) cacheMap.get(domain);
        if (cb == null) return;
        List<Object> list = new ArrayList<Object>();
        final long l = System.currentTimeMillis();
        list.add(l);
        Object o = result;
        switch (cb.getStoreModel()) {
            case SOFT:
                o = new SoftReference(result);
                break;
            case WEAK:
                o = new WeakReference(result);
                break;
            default:
                break;
        }
        list.add(o);
        if (cb.map.containsKey(clazz)) {
            cb.map.get(clazz).put(condition, list);
        } else {
            Map<Object, List<?>> map = new ConcurrentHashMap<Object, List<?>>();
            map.put(condition, list);
            cb.map.put(clazz, map);
        }
    }

    public abstract Cache setDomain(String domain);

    public abstract int getExpire();

    public abstract Cache setExpire(int expire);

    public abstract StoreModel getStoreModel();

    public abstract Cache setStoreModel(StoreModel storeModel);

    public void delDomain(String domain) {
        cacheMap.remove(domain);
    }

    public void clearAll() {
        cacheMap.clear();
    }

    public void delNode(String domain, String node) {
        if (domain == null || node == null) return;
        if ("".equals(node.trim())) delDomain(domain);
        if (cacheMap.containsKey(domain)) {
            CacheBeen cb = (CacheBeen) cacheMap.get(domain);
            for (Class<Table<?>> ct : cb.map.keySet()) {
                Map<Object, List<?>> m = cb.map.get(ct);
                for (Object o : m.keySet()) {
                    if (node.equals(((Condition) o).getNode())) {
                        m.remove(o);
                    }
                }
            }
        }
    }

    public void delNode(String domain, Class<?> clazz, String node) {
        if (domain == null || clazz == null || node == null) return;
        if (cacheMap.containsKey(domain)) {
            CacheBeen cb = (CacheBeen) cacheMap.get(domain);
            if (cb.map.containsKey(clazz)) {
                Map<Object, List<?>> m = cb.map.get(clazz);
                for (Object o : m.keySet()) {
                    if (node.equals(((Condition) o).getNode())) {
                        m.remove(o);
                    }
                }
            }
        }
    }

    public abstract boolean build();

    static class ClearThread extends Thread {
        private static final long SLEEP_INTERVAL_MS = 30_000; // 30秒检查一次

        {
            this.setDaemon(true);
            this.setName("ClearThread");
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    long currentTimeMillis = System.currentTimeMillis();
                    cacheMap.values().forEach(cacheBean -> {
                        long expire = cacheBean.getExpire();
                        if (expire != 0) {
                            ((CacheBeen) cacheBean).map.forEach((key, entries) -> {
                                entries.entrySet().removeIf(entry -> {
                                    long createTime = (Long) entry.getValue().get(0);
                                    return currentTimeMillis > (expire + createTime);
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
