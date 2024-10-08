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
import io.github.donnie4w.jdao.util.Logger;

/**
 * This class provides caching functionalities for JDAO.
 */
public class JdaoCache {
    private static final Cache cache = Cache.newInstance();

    private JdaoCache() {
    }

    /**
     * Binds the specified package to enable caching mechanisms for all standard entity classes within the package.
     *
     * @param packageName The name of the package containing the entity classes to bind.
     *                    <p>
     *                    Description:
     *                    This method sets up all entity classes within the specified package to utilize the caching mechanism.
     *                    <p>
     *                    Example:
     *                    <p> Assuming "com.example.entities" is the package containing the entity classes
     *                    <blockquote><pre>
     *                                                                                                                                                                                                                                                          JdaoCache.bindPackage("com.example.entities");
     *                                                                                                                                                                                                                                                          </pre></blockquote>
     */
    public static void bindPackage(String packageName) {
        cache.bindPackage(packageName);
        if (Logger.isVaild())
            Logger.log("[BIND CACHE][PACKAGENAME:", packageName, "]");
    }

    /**
     * Binds the specified package to enable caching mechanisms for all standard entity classes within the package, using the provided cache handle.
     *
     * @param packageName The name of the package containing the entity classes to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     *                    <p>
     *                    Description:
     *                    This method sets up all entity classes within the specified package to utilize the caching mechanism defined by the provided cache handle.
     *                    <p>
     *                    Example:
     *                    <p> Assuming "com.example.entities" is the package containing the entity classes
     *                    <blockquote><pre>
     *                                                                                                                                                                                                                                                          JdaoCache.bindPackage("com.example.entities", new CacheHandle(10000));
     *                                                                                                                                                                                                                                                        </pre></blockquote>
     */
    public static void bindPackage(String packageName, CacheHandle cacheHandle) {
        cache.bindPackage(packageName, cacheHandle);
        if (Logger.isVaild())
            Logger.log("[BIND CACHE][PACKAGENAME:", packageName, "]");
    }

    /**
     * Binds the specified standard entity class to enable caching mechanisms.
     *
     * @param clazz The class representing the entity to bind.
     *              <p>
     *              Description:
     *              This method sets up the specified entity class to utilize the caching mechanism.
     *              <p>
     *              Example:
     *              <p> Assuming Hstest is a class representing the user entity
     *              <blockquote><pre>
     *                                                                                                                                                                            JdaoCache.bindClass(Hstest.class);
     *                                                                                                                                                                           </pre></blockquote>
     */
    public static void bindClass(Class<? extends Table<?>> clazz) {
        cache.bindClass(clazz);
        if (Logger.isVaild())
            Logger.log("[BIND CACHE][CLASS:", clazz, "]");
    }

    /**
     * Binds the specified standard entity class to enable caching mechanisms,using the provided cache handle.
     *
     * @param clazz       The class representing the entity to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     *                    <p>
     *                    Description:
     *                    This method sets up the specified entity class to utilize the caching mechanism.
     *                    <p>
     *                    Example:
     *                    <p> Assuming Hstest is a class representing the user entity
     *                    <blockquote><pre>
     *                                                                                                                                                                                                                                                          JdaoCache.bindClass(Hstest.class,new CacheHandle(10000));
     *                                                                                                                                                                                                                                                         </pre></blockquote>
     */
    public static void bindClass(Class<? extends Table<?>> clazz, CacheHandle cacheHandle) {
        cache.bindClass(clazz, cacheHandle);
        if (Logger.isVaild())
            Logger.log("[BIND CACHE][CLASS:", clazz, "]");
    }

    /**
     * remove cache by dao package
     *
     * @param packageName The package of the class representing the entity
     */
    public static void unbindPackage(String packageName) {
        cache.unbindPackage(packageName);
        if (Logger.isVaild())
            Logger.log("[UNBIND CACHE][PACKAGENAME:", packageName, "]");
    }

    /**
     * remove cache by dao package
     *
     * @param clazz the class representing the entity
     */
    public static void unbindClass(Class<? extends Table<?>> clazz) {
        cache.unbindClass(clazz);
        if (Logger.isVaild())
            Logger.log("[UNBIND CACHE][CLASS:", clazz, "]");
    }

    /**
     * Binds the specified mapper interface to enable caching mechanisms for all methods within the interface.
     *
     * @param mapperface The mapper interface class that defines the methods to bind.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified mapper interface to utilize the caching mechanism for all methods within the interface.
     * <p>
     * Example:
     * <p> Assuming UserMapper is the mapper interface class
     * <blockquote><pre>
     *   boolean bound = JdaoCache.bindMapper(UserMapper.class);
     * </pre></blockquote>
     */
    public static boolean bindMapper(Class<?> mapperface) {
        if (cache.bindMapper(mapperface)) {
            if (Logger.isVaild())
                Logger.log("[BIND CACHE MAPPER][INTERFACE:", mapperface, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified mapper interface to enable caching mechanisms for all methods within the interface, using the provided cache handle.
     *
     * @param mapperface  The mapper interface class that defines the methods to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified mapper interface to utilize the caching mechanism defined by the provided cache handle for all methods within the interface.
     * <p>
     * Example:
     * <p> Assuming UserMapper is the mapper interface class
     * <p> And "cacheHandle" is an instance of CacheHandle configured with specific caching policies
     * <blockquote><pre>
     *   boolean bound = JdaoCache.bindMapper(UserMapper.class, new CacheHandle(10000));
     * </pre></blockquote>
     */
    public static boolean bindMapper(Class<?> mapperface, CacheHandle cacheHandle) {
        if (cache.bindMapper(mapperface, cacheHandle)) {
            if (Logger.isVaild())
                Logger.log("[BIND CACHE MAPPER][INTERFACE:", mapperface, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified XML mapping namespace to enable caching mechanisms for all CRUD tags within the namespace.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified XML mapping namespace to utilize the caching mechanism for all CRUD operations within the namespace.
     * <p>
     * Example:
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <blockquote><pre>
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users");
     *   </pre></blockquote>
     */
    public static boolean bindMapper(String namespace) {
        if (cache.bindMapper(namespace)) {
            if (Logger.isVaild())
                Logger.log("[BIND CACHE MAPPER][NAMESPACE:", namespace, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified XML mapping namespace to enable caching mechanisms for all CRUD tags within the namespace, using the provided cache handle.
     *
     * @param namespace   The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified XML mapping namespace to utilize the caching mechanism defined by the provided cache handle for all CRUD operations within the namespace.
     * <p>
     * Example:
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <blockquote><pre>
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users", new CacheHandle(10000));
     * </pre></blockquote>
     */
    public static boolean bindMapper(String namespace, CacheHandle cacheHandle) {
        if (cache.bindMapper(namespace, cacheHandle)) {
            if (Logger.isVaild())
                Logger.log("[BIND CACHE MAPPER][NAMESPACE:", namespace, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified XML mapping namespace and CRUD tag ID to enable caching mechanisms for the specified CRUD operation.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param id        The ID of the CRUD tag within the namespace to bind.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified XML mapping namespace and CRUD tag ID to utilize the caching mechanism for the specified CRUD operation.
     * <p>
     * Example:
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUserById" is the ID of the select tag within the namespace
     * <blockquote><pre>
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users", "getUserById");
     * </pre></blockquote>
     */
    public static boolean bindMapper(String namespace, String id) {
        if (cache.bindMapper(namespace, id)) {
            if (Logger.isVaild())
                Logger.log("[BIND CACHE MAPPER][NAMESPACE:", namespace, ",ID:", id, "]");
            return true;
        }
        return false;
    }

    /**
     * Binds the specified XML mapping namespace and CRUD tag ID to enable caching mechanisms for the specified CRUD operation, using the provided cache handle.
     *
     * @param namespace   The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param id          The ID of the CRUD tag within the namespace to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     * @return true if the binding was successful, false otherwise.
     * <p>
     * Description:
     * This method sets up the specified XML mapping namespace and CRUD tag ID to utilize the caching mechanism defined by the provided cache handle for the specified CRUD operation.
     * <p>
     * Example:
     * <p> Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     * <p> And "getUserById" is the ID of the select tag within the namespace
     * <blockquote><pre>
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users", "getUserById", new CacheHandle(10000));
     * </pre></blockquote>
     */
    public static boolean bindMapper(String namespace, String id, CacheHandle cacheHandle) {
        if (cache.bindMapper(namespace, id, cacheHandle)) {
            if (Logger.isVaild())
                Logger.log("[BIND CACHE MAPPER][NAMESPACE:", namespace, ",ID:", id, "]");
            return true;
        }
        return false;
    }

    /**
     * remove mapper cache by mapper interface
     *
     * @param mapperface the mapper interface class
     */
    public static void unbindMapper(Class<?> mapperface) {
        cache.unbindMapper(mapperface);
        if (Logger.isVaild())
            Logger.log("[UNBIND CACHE MAPPER][Class:", mapperface, "]");
    }

    /**
     * remove mapper cache by namespace
     *
     * @param namespace the namespace of xml mapper file
     */
    public static void unbindMapper(String namespace) {
        cache.unbindMapper(namespace);
        if (Logger.isVaild())
            Logger.log("[UNBIND CACHE MAPPER][NAMESPACE:", namespace, "]");
    }

    /**
     * remove mapper cache by namespace and mapper id
     *
     * @param namespace the namespace
     * @param id        the id
     */
    public static void unbindMapper(String namespace, String id) {
        cache.unbindMapper(namespace, id);
        if (Logger.isVaild())
            Logger.log("[UNBIND CACHE MAPPER][NAMESPACE:", namespace, ",ID:", id, "]");

    }


    /**
     * get cache domain by package and table dao
     *
     * @param clazz the class representing the entity
     * @return domain
     */
    public static String getDomain(Class<?> clazz) {
        return cache.getDomain(clazz);
    }


    /**
     * Gets the domain for the specified namespace and id.
     *
     * @param namespace the namespace
     * @param id        the id
     * @return the domain corresponding to the namespace and id
     */
    public static String getDomain(String namespace, String id) {
        return cache.getDomain(namespace, id);
    }

    /**
     * get cache value by domain,class and condition
     *
     * @param domain    the domain of cache
     * @param clazz     the class representing the entity
     * @param condition the condition of cache
     * @return value of cache
     */
    public static Object getCache(String domain, Class<?> clazz, Condition condition) {
        return cache.getCache(domain, clazz, condition);
    }

    /**
     * get cache value by domain,class and condition
     *
     * @param domain    the domain of cache
     * @param namespace namespace
     * @param id        mapper id
     * @param condition the condition of cache
     * @return value of cache
     */
    public static Object getCache(String domain, String namespace, String id, Condition condition) {
        return cache.getCache(domain, namespace, id, condition);
    }

    /**
     * set cache
     *
     * @param domain    the domain of cache
     * @param clazz     the class representing the entity
     * @param condition the condition of cache
     * @param result    value of qurey
     */
    public static void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result) {
        cache.setCache(domain, clazz, condition, result);
    }

    /**
     * set cache
     *
     * @param domain    the domain of cache
     * @param namespace namespace
     * @param id        mapper id
     * @param condition the condition of cache
     * @param result    value of qurey
     */
    public static void setCache(String domain, String namespace, String id, Condition condition, Object result) {
        cache.setCache(domain, namespace, id, condition, result);
    }

    /**
     * clear cache by the class representing the entity
     * @param clazz  the class representing the entity
     * @return return true if clear data
     */
    public static boolean clear(Class<? extends Table> clazz) {
        if (cache.clearCache(clazz)) {
            if (Logger.isVaild())
                Logger.log("[CLEAR CACHE][CLASS:", clazz, "]");
            return true;
        }
        return false;
    }

    /**
     * clear cache by namespace and id
     * @param namespace namespace
     * @param id        mapper id
     * @return return true if clear data
     */
    public static boolean clear(String namespace, String id) {
        if (cache.clearCache(namespace, id)) {
            if (Logger.isVaild())
                Logger.log("[CLEAR CACHE MAPPER][NAMESPACE:", namespace, ",ID:", id, "]");

            return true;
        }
        return false;
    }
}
