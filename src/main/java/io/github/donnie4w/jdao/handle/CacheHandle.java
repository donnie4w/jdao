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
import io.github.donnie4w.jdao.base.StoreModel;
import io.github.donnie4w.jdao.base.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CacheHandle {

    private final static AtomicLong al = new AtomicLong(1);

    Map<Class<Table<?>>, Map<Condition, CacheBean>> map = new ConcurrentHashMap<>();

    private String domain = String.valueOf(System.nanoTime() + al.incrementAndGet());
    private int expire = 5 * 60 * 1000;
    private StoreModel storeModel = StoreModel.SOFT;


    public CacheHandle() {
    }

    public CacheHandle(String domain) {
        this.domain = domain;
    }

    public CacheHandle(int expire) {
        this.expire = expire;
    }

    public CacheHandle(String domain, int expire) {
        this.domain = domain;
        this.expire = expire;
    }

    public CacheHandle(String domain, int expire, StoreModel storeModel) {
        this.domain = domain;
        this.expire = expire;
        this.storeModel = storeModel;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public StoreModel getStoreModel() {
        return storeModel;
    }

    public void setStoreModel(StoreModel storeModel) {
        this.storeModel = storeModel;
    }
}
