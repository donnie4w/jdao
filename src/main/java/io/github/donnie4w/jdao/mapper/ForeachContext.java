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

package io.github.donnie4w.jdao.mapper;

import java.util.HashMap;
import java.util.Map;

public class ForeachContext extends ParamContext {
    ParamContext superParamContext;
    Map<String, Object> itemMap;
    Map<String, Integer> indexMap;

    public ForeachContext(ParamContext paramContext, Object arg) {
        super(arg);
        this.superParamContext = paramContext;
    }

    public ForeachContext(ParamContext paramContext) {
        super(paramContext);
    }

    public Object getItem(String itemName) {
        if (this.itemMap == null) {
            return null;
        }
        return itemMap.get(itemName);
    }

    public void setItem(String itemName, Object itemValue) {
        if (itemMap == null) {
            itemMap = new HashMap();
        }
        itemMap.put(itemName, itemValue);
    }

    public Integer getIndex(String indexName) {
        if (this.indexMap == null) {
            return 0;
        }
        return indexMap.get(indexName);
    }

    public void setIndex(String indexName, int iter) {
        if (indexMap == null) {
            indexMap = new HashMap<>();
        }
        indexMap.put(indexName, iter);
    }

    public Object get(String key) {
        Object r = super.get(key);
        if (r == null && superParamContext != null) {
            r = superParamContext.get(key);
        }
        return r;
    }

}
