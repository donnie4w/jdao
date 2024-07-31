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
import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages caching settings such as expiration and storage model.
 */
public class CacheHandle {
    private final static AtomicLong al = new AtomicLong(1);

    Map<Class<Table<?>>, Map<Condition, CacheBean>> map = new ConcurrentHashMap<>();

    private String domain = newDomain();
    private int expire = 5 * 60 * 1000;
    private StoreModel storeModel = StoreModel.SOFT;

    /**
     * Generates a new unique domain for caching purposes.
     *
     * @return a new domain string.
     */
    public static String newDomain() {
        return String.valueOf(System.nanoTime()) + al.incrementAndGet();
    }

    /**
     * Creates a new CacheHandle with default settings.
     */
    public CacheHandle() {
    }

    /**
     * Creates a new CacheHandle with a specified domain.
     *
     * @param domain the domain for the cache.
     */
    public CacheHandle(String domain) {
        this.domain = domain;
    }

    /**
     * Creates a new CacheHandle with a specified expiration time.
     *
     * @param expire the expiration time in milliseconds.
     */
    public CacheHandle(int expire) {
        this.expire = expire;
    }

    /**
     * Creates a new CacheHandle with a specified domain and expiration time.
     *
     * @param domain the domain for the cache.
     * @param expire the expiration time in milliseconds.
     */
    public CacheHandle(String domain, int expire) {
        this.domain = domain;
        this.expire = expire;
    }

    /**
     * Creates a new CacheHandle with a specified domain, expiration time, and storage model.
     *
     * @param domain the domain for the cache.
     * @param expire the expiration time in milliseconds.
     * @param storeModel the storage model to use.
     */
    public CacheHandle(String domain, int expire, StoreModel storeModel) {
        this.domain = domain;
        this.expire = expire;
        this.storeModel = storeModel;
    }

    /**
     * Gets the domain associated with this CacheHandle.
     *
     * @return the domain.
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the domain for this CacheHandle.
     *
     * @param domain the new domain.
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    /**
     * Gets the expiration time associated with this CacheHandle.
     *
     * @return the expiration time in milliseconds.
     */
    public int getExpire() {
        return expire;
    }

    /**
     * Sets the expiration time for this CacheHandle.
     *
     * @param expire the new expiration time in milliseconds.
     */
    public void setExpire(int expire) {
        this.expire = expire;
    }

    /**
     * Gets the storage model associated with this CacheHandle.
     *
     * @return the storage model.
     */
    public StoreModel getStoreModel() {
        return storeModel;
    }

    /**
     * Sets the storage model for this CacheHandle.
     *
     * @param storeModel the new storage model.
     */
    public void setStoreModel(StoreModel storeModel) {
        this.storeModel = storeModel;
    }
}
