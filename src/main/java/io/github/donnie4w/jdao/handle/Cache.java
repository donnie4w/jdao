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

public interface Cache {
    void register(String packageName);

    void register(String packageName, CacheHandle cacheHandle);

    void register(Class<? extends Table> clazz);

    void register(Class<? extends Table> clazz, CacheHandle cacheHandle);

    void remove(String packageName);

    void remove(Class<?> clazz);

    boolean registerMapper(Class<?> mapperface);

    boolean registerMapper(Class<?> mapperface, CacheHandle cacheHandle);

    boolean registerMapper(String namespace);

    boolean registerMapper(String namespace, CacheHandle cacheHandle);

    boolean registerMapper(String namespace, String id);

    boolean registerMapper(String namespace, String id, CacheHandle cacheHandle);

    void removeMapper(Class<?> mapperface);

    void removeMapper(String namespace);

    void removeMapper(String namespace, String id);

    String getDomain(String packageName, Class<?> clazz);

    Object getCache(String domain, Class<?> clazz, Condition condition);

    void setCache(String domain, Class<Table<?>> clazz, Condition condition, Object result);

    void clearCache(String domain);

    void clearCache(String domain, Class<?> clazz, String node);
}
