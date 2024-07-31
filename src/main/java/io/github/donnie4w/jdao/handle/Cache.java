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

/**
 * Represents a caching mechanism for database operations.
 */
public abstract class Cache {

    static Cache newInstance(){
        return new Cacher();
    }

    /**
     * Binds the specified package to enable caching mechanisms for all standard entity classes within the package.
     *
     * @param packageName The name of the package containing the entity classes to bind.
     *
     * Description:
     *   This method sets up all entity classes within the specified package to utilize the caching mechanism.
     *
     * Example:
     *   // Assuming "com.example.entities" is the package containing the entity classes
     *   JdaoCache.bindPackage("com.example.entities");
     */
    abstract void bindPackage(String packageName);

    /**
     * Binds the specified package to enable caching mechanisms for all standard entity classes within the package, using the provided cache handle.
     *
     * @param packageName The name of the package containing the entity classes to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     *
     * Description:
     *   This method sets up all entity classes within the specified package to utilize the caching mechanism defined by the provided cache handle.
     *
     * Example:
     *   // Assuming "com.example.entities" is the package containing the entity classes
     *   JdaoCache.bindPackage("com.example.entities", new CacheHandle(10000));
     */
    abstract void bindPackage(String packageName, CacheHandle cacheHandle);

    abstract void unbindPackage(String packageName);

    /**
     * Binds the specified standard entity class to enable caching mechanisms.
     *
     * @param clazz The class representing the entity to bind.
     *
     * Description:
     *   This method sets up the specified entity class to utilize the caching mechanism.
     *
     * Example:
     *   // Assuming Hstest is a class representing the user entity
     *   JdaoCache.bindClass(Hstest.class);
     */
    abstract void bindClass(Class<? extends Table> clazz);

    /**
     * Binds the specified standard entity class to enable caching mechanisms,using the provided cache handle.
     *
     * @param clazz The class representing the entity to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     *
     * Description:
     *   This method sets up the specified entity class to utilize the caching mechanism.
     *
     * Example:
     *   // Assuming Hstest is a class representing the user entity
     *   JdaoCache.bindClass(Hstest.class,new CacheHandle(10000));
     */
    abstract void bindClass(Class<? extends Table> clazz, CacheHandle cacheHandle);

    abstract void unbindClass(Class<? extends Table> clazz);

    /**
     * Binds the specified mapper interface to enable caching mechanisms for all methods within the interface.
     *
     * @param mapperface The mapper interface class that defines the methods to bind.
     *
     * @return true if the binding was successful, false otherwise.
     *
     * Description:
     *   This method sets up the specified mapper interface to utilize the caching mechanism for all methods within the interface.
     *
     * Example:
     *   // Assuming UserMapper is the mapper interface class
     *   boolean bound = JdaoCache.bindMapper(UserMapper.class);
     *   if (bound) {
     *       System.out.println("UserMapper is now bound with caching enabled.");
     *   } else {
     *       System.out.println("Failed to bind UserMapper.");
     *   }
     */
    abstract boolean bindMapper(Class<?> mapperface);

    /**
     * Binds the specified mapper interface to enable caching mechanisms for all methods within the interface, using the provided cache handle.
     *
     * @param mapperface The mapper interface class that defines the methods to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     *
     * @return true if the binding was successful, false otherwise.
     *
     * Description:
     *   This method sets up the specified mapper interface to utilize the caching mechanism defined by the provided cache handle for all methods within the interface.
     *
     * Example:
     *   // Assuming UserMapper is the mapper interface class
     *   // And "cacheHandle" is an instance of CacheHandle configured with specific caching policies
     *   boolean bound = JdaoCache.bindMapper(UserMapper.class, new CacheHandle(10000));
     *   if (bound) {
     *       System.out.println("The UserMapper interface is now bound with caching enabled using the specified cache handle.");
     *   } else {
     *       System.out.println("Failed to bind the UserMapper interface with caching.");
     *   }
     */
    abstract boolean bindMapper(Class<?> mapperface, CacheHandle cacheHandle);

    abstract void unbindMapper(Class<?> mapperface);

    /**
     * Binds the specified XML mapping namespace to enable caching mechanisms for all CRUD tags within the namespace.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     *
     * @return true if the binding was successful, false otherwise.
     *
     * Description:
     *   This method sets up the specified XML mapping namespace to utilize the caching mechanism for all CRUD operations within the namespace.
     *
     * Example:
     *   // Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users");
     *   if (bound) {
     *       System.out.println("The 'com.example.mappers.users' namespace is now bound with caching enabled.");
     *   } else {
     *       System.out.println("Failed to bind the 'com.example.mappers.users' namespace.");
     *   }
     */
    abstract boolean bindMapper(String namespace);

    /**
     * Binds the specified XML mapping namespace to enable caching mechanisms for all CRUD tags within the namespace, using the provided cache handle.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     *
     * @return true if the binding was successful, false otherwise.
     *
     * Description:
     *   This method sets up the specified XML mapping namespace to utilize the caching mechanism defined by the provided cache handle for all CRUD operations within the namespace.
     *
     * Example:
     *   // Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users", new CacheHandle(10000));
     *   if (bound) {
     *       System.out.println("The 'com.example.mappers.users' namespace is now bound with caching enabled using the specified cache handle.");
     *   } else {
     *       System.out.println("Failed to bind the 'com.example.mappers.users' namespace with caching.");
     *   }
     */
    abstract boolean bindMapper(String namespace, CacheHandle cacheHandle);

    abstract void unbindMapper(String namespace);

    /**
     * Binds the specified XML mapping namespace and CRUD tag ID to enable caching mechanisms for the specified CRUD operation.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param id The ID of the CRUD tag within the namespace to bind.
     *
     * @return true if the binding was successful, false otherwise.
     *
     * Description:
     *   This method sets up the specified XML mapping namespace and CRUD tag ID to utilize the caching mechanism for the specified CRUD operation.
     *
     * Example:
     *   // Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *   // And "getUserById" is the ID of the select tag within the namespace
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users", "getUserById");
     *   if (bound) {
     *       System.out.println("The 'getUserById' select operation is now bound with caching enabled.");
     *   } else {
     *       System.out.println("Failed to bind the 'getUserById' select operation.");
     *   }
     */
    abstract boolean bindMapper(String namespace, String id);

    /**
     * Binds the specified XML mapping namespace and CRUD tag ID to enable caching mechanisms for the specified CRUD operation, using the provided cache handle.
     *
     * @param namespace The namespace in the XML mapping files that corresponds to the CRUD operations to bind.
     * @param id The ID of the CRUD tag within the namespace to bind.
     * @param cacheHandle The cache handle that specifies caching behaviors such as data expiration and eviction policies.
     *
     * @return true if the binding was successful, false otherwise.
     *
     * Description:
     *   This method sets up the specified XML mapping namespace and CRUD tag ID to utilize the caching mechanism defined by the provided cache handle for the specified CRUD operation.
     *
     * Example:
     *   // Assuming "com.example.mappers.users" is the namespace in the XML mapping files
     *   // And "getUserById" is the ID of the select tag within the namespace
     *   boolean bound = JdaoCache.bindMapper("com.example.mappers.users", "getUserById", new CacheHandle(10000));
     *   if (bound) {
     *       System.out.println("The 'getUserById' select operation is now bound with caching enabled using the specified cache handle.");
     *   } else {
     *       System.out.println("Failed to bind the 'getUserById' select operation with caching.");
     *   }
     */
    abstract boolean bindMapper(String namespace, String id, CacheHandle cacheHandle);

    abstract void unbindMapper(String namespace, String id);

    abstract String getDomain(String packageName, Class<?> clazz);

    abstract String getDomain(String namespace, String id);

    abstract Object getCache(String domain, Class<?> clazz, Condition condition);

    abstract void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result);

    abstract void clearCache(String domain);

    abstract void clearCache(String domain, Class<?> clazz, String node);
}
