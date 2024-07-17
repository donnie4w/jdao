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

public class JdaoCache {
    private static final Cache cache = Cache.newInstance();

    /**
     * register cache by dao package
     *
     * @param packageName
     */
    public static void bindPackage(String packageName) {
        cache.bindPackage(packageName);
    }

    /**
     * register cache by dao package and use new CacheHandle
     *
     * @param packageName
     * @param cacheHandle
     */
    public static void bindPackage(String packageName, CacheHandle cacheHandle) {
        cache.bindPackage(packageName, cacheHandle);
    }

    /**
     * register cache by table dao
     *
     * @param clazz
     */
    public static void bindClass(Class<? extends Table> clazz) {
        cache.bindClass(clazz);
    }

    /**
     * register cache by table dao and use new CacheHandle
     *
     * @param clazz
     */
    public static void bindClass(Class<? extends Table> clazz, CacheHandle cacheHandle) {
        cache.bindClass(clazz, cacheHandle);
    }

    /**
     * remove cache by dao package
     *
     * @param packageName
     */
    public static void removePackage(String packageName) {
        cache.removePackage(packageName);
    }

    /**
     * remove cache by dao package
     *
     * @param clazz
     */
    public static void removeClass(Class<? extends Table> clazz) {
        cache.removeClass(clazz);
    }

    /**
     * register cache for jdao Mapper by mapper interface
     *
     * @param mapperface
     */
    public static void bindMapper(Class<?> mapperface) {
        cache.bindMapper(mapperface);
    }

    /**
     * register cache for jdao Mapper by mapper interface and use new CacheHandle
     *
     * @param mapperface
     * @param cacheHandle
     */
    public static void bindMapper(Class<?> mapperface, CacheHandle cacheHandle) {
        cache.bindMapper(mapperface, cacheHandle);
    }

    /**
     * register cache for jdao Mapper by namespace
     *
     * @param namespace
     */
    public static void bindMapper(String namespace) {
        cache.bindMapper(namespace);
    }

    /**
     * register cache for jdao Mapper by namespace and use new CacheHandle
     *
     * @param namespace
     * @param cacheHandle
     */
    public static void bindMapper(String namespace, CacheHandle cacheHandle) {
        cache.bindMapper(namespace, cacheHandle);
    }

    /**
     * register cache for jdao Mapper by namespace and mapper id
     *
     * @param namespace
     */
    public static void bindMapper(String namespace, String id) {
        cache.bindMapper(namespace, id);
    }

    /**
     * register cache for jdao Mapper by namespace and mapper id and use new CacheHandle
     *
     * @param namespace
     * @param id
     * @param cacheHandle
     */
    public static void bindMapper(String namespace, String id, CacheHandle cacheHandle) {
        cache.bindMapper(namespace, id, cacheHandle);
    }

    /**
     * remove mapper cache by mapper interface
     *
     * @param mapperface
     */
    public static void removeMapper(Class<?> mapperface) {
        cache.removeMapper(mapperface);
    }

    /**
     * remove mapper cache by namespace
     *
     * @param namespace
     */
    public static void removeMapper(String namespace) {
        cache.removeMapper(namespace);
    }

    /**
     * remove mapper cache by namespace and mapper id
     *
     * @param namespace
     * @param id
     */
    public static void removeMapper(String namespace, String id) {
        cache.removeMapper(namespace, id);
    }


    /**
     * get cache domain by package and table dao
     *
     * @param packageName
     * @param clazz
     * @return
     */
    public static String getDomain(String packageName, Class<?> clazz) {
        return cache.getDomain(packageName, clazz);
    }

    /**
     * get cache value by domain,class and condition
     *
     * @param domain
     * @param clazz
     * @param condition
     * @return
     */
    public static Object getCache(String domain, Class<?> clazz, Condition condition) {
        return cache.getCache(domain, clazz, condition);
    }

    /**
     * set cache
     *
     * @param domain
     * @param clazz
     * @param condition
     * @param result
     */
    public static void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result) {
        cache.setCache(domain, clazz, condition, result);
    }

    /**
     * clear cache by domain
     *
     * @param domain
     */
    public static void clearCache(String domain) {
        cache.clearCache(domain);
    }

    /**
     * clear cache by domain and class and node
     *
     * @param domain
     * @param clazz
     * @param node
     */
    public static void clearCache(String domain, Class<?> clazz, String node) {
        cache.clearCache(domain, clazz, node);
    }
}
