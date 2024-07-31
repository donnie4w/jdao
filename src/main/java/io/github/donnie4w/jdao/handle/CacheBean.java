
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

/**
 * Represents a cached item with a timestamp and value.
 */
public class CacheBean {
    private long timestamp;
    private Object value;

    /**
     * Constructs a new CacheBean instance.
     *
     * @param timestamp the timestamp when the item was cached.
     * @param value the value being cached.
     */
    public CacheBean(long timestamp, Object value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    /**
     * Gets the timestamp of the cached item.
     *
     * @return the timestamp.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the cached item.
     *
     * @param timestamp the new timestamp.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the value of the cached item.
     *
     * @return the value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the cached item.
     *
     * @param value the new value.
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
