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

public abstract class Cache {

    static Cache newInstance(){
        return new Cacher();
    }

    abstract void bindPackage(String packageName);

    abstract void bindPackage(String packageName, CacheHandle cacheHandle);

    abstract void unbindPackage(String packageName);

    abstract void bindClass(Class<? extends Table> clazz);

    abstract void bindClass(Class<? extends Table> clazz, CacheHandle cacheHandle);

    abstract void unbindClass(Class<? extends Table> clazz);

    abstract boolean bindMapper(Class<?> mapperface);

    abstract boolean bindMapper(Class<?> mapperface, CacheHandle cacheHandle);

    abstract void unbindMapper(Class<?> mapperface);

    abstract boolean bindMapper(String namespace);

    abstract boolean bindMapper(String namespace, CacheHandle cacheHandle);

    abstract void unbindMapper(String namespace);

    abstract boolean bindMapper(String namespace, String id);

    abstract boolean bindMapper(String namespace, String id, CacheHandle cacheHandle);

    abstract void unbindMapper(String namespace, String id);

    abstract String getDomain(String packageName, Class<?> clazz);

    abstract String getDomain(String namespace, String id);

    abstract Object getCache(String domain, Class<?> clazz, Condition condition);

    abstract void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result);

    abstract void clearCache(String domain);

    abstract void clearCache(String domain, Class<?> clazz, String node);
}
