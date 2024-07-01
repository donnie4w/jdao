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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheBeen extends Cache {
    private static final String defaultDomain = String.valueOf(System.nanoTime());

    static {
        new ClearThread().start();
    }

    Map<Class<Table<?>>, Map<Object, List<?>>> map = new ConcurrentHashMap<Class<Table<?>>, Map<Object, List<?>>>();
    private String domain = null;
    //millisecond
    private int expire = 5 * 60 * 1000;
    private StoreModel storeModel = StoreModel.SOFT;

    private CacheBeen() {
    }

    public static Cache newInstance() {
        return new CacheBeen();
    }

    @Override
    public Cache setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public Cache setExpire(int expire) {
        this.expire = expire;
        return this;
    }

    @Override
    public Cache setStoreModel(StoreModel storeModel) {
        this.storeModel = storeModel;
        return this;
    }

    @Override
    public boolean build() {
        if (domain == null) {
            domain = defaultDomain;
        }
        cacheMap.put(domain, this);
        return true;
    }

    @Override
    public int getExpire() {
        return this.expire;
    }

    @Override
    public StoreModel getStoreModel() {
        return this.storeModel;
    }
}
