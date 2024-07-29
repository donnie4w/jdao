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

public class Cacher extends Cache {

    private final CacheHandle defaultCacheHandle = new CacheHandle();
    private final CacheContainer container = new CacheContainer(defaultCacheHandle);

    public Cacher() {
    }

    public void bindPackage(String packageName) {
        container.bindPackage(packageName);
    }


    public void bindPackage(String packageName, CacheHandle cacheHandle) {
        container.bindPackage(packageName, cacheHandle);
    }

    public void bindClass(Class<? extends Table> clazz) {
        container.bindClass(clazz);
    }

    public void bindClass(Class<? extends Table> clazz, CacheHandle cacheHandle) {
        container.bindClass(clazz, cacheHandle);
    }

    public void unbindPackage(String packageName) {
        container.unbindPackage(packageName);
    }

    public void unbindClass(Class<? extends Table> clazz) {
        container.unbindClass(clazz);
    }

    public boolean bindMapper(Class<?> mapperface) {
        return container.bindMapper(mapperface);
    }

    public boolean bindMapper(Class<?> mapperface, CacheHandle cacheHandle) {
        return container.bindMapper(mapperface, cacheHandle);
    }

    public boolean bindMapper(String namespace) {
        return container.bindMapper(namespace);
    }

    public boolean bindMapper(String namespace, CacheHandle cacheHandle) {
        return container.bindMapper(namespace, cacheHandle);
    }

    public boolean bindMapper(String namespace, String id) {
        return container.bindMapper(namespace, id);
    }

    public boolean bindMapper(String namespace, String id, CacheHandle cacheHandle) {
        return container.bindMapper(namespace, id, cacheHandle);
    }

    public void unbindMapper(Class<?> mapperface) {
        container.unbindMapper(mapperface);
    }

    public void unbindMapper(String namespace) {
        container.unbindMapper(namespace);
    }

    public void unbindMapper(String namespace, String id) {
        container.unbindMapper(namespace, id);
    }


    public String getDomain(String packageName, Class<?> clazz) {
        return container.getDomain(packageName,clazz);
    }

    public String getDomain(String namespace, String id) {
        return container.getDomain(namespace,id);
    }


    public Object getCache(String domain, Class<?> clazz, Condition condition) {
        return container.getCache(domain,clazz,condition);
    }

    public void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result) {
        container.setCache(domain,clazz,condition,result);
    }

    public void clearCache(String domain) {
        container.clearCache(domain);
    }

    public void clearCache(String domain, Class<?> clazz, String node) {
        container.clearCache(domain,clazz,node);
    }

}
