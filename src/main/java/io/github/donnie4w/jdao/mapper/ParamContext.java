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

import io.github.donnie4w.jdao.util.Utils;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParamContext {

    Map<String, Object> paramMap;
    private Object[] paramArray = null;

    public ParamContext() {
    }

    public ParamContext(Object arg) {
        toParamContext(arg);
    }

    public ParamContext(Object... args) {
        toParamContext(args);
    }

    public ParamContext(ParamContext paramContext) {
        this.paramMap = paramContext.paramMap;
        this.paramArray = paramContext.paramArray;
    }

    public void put(String key, Object value) {
        if (paramMap == null) {
            paramMap = new HashMap();
        }
        this.paramMap.put(key, value);
    }

    public Object get(String key) {
        if (paramMap != null) {
            return paramMap.get(key);
        }
        return null;
    }

    public Map<String, Object> getMap() {
        return paramMap;
    }

    public Object[] getArray() {
        return paramArray;
    }

    public void setMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    private void toParamContext(Object... args) {
        if (args != null) {
            this.paramArray = args;
        }
    }

    private void toParamContext(Object arg) {
        if (arg instanceof Map) {
            this.paramMap = (Map) arg;
            return;
        } else if (arg instanceof Object[]) {
            this.paramArray = (Object[]) arg;
        } else if (arg.getClass().isArray()) {
            Object[] tempArray = new Object[Array.getLength(arg)];
            for (int i = 0; i < tempArray.length; i++) {
                tempArray[i] = Array.get(arg, i);
            }
            this.paramArray = tempArray;
        } else if (arg instanceof List) {
            this.paramArray = ((List) arg).toArray();
        } else if (java.util.List.class.isAssignableFrom(arg.getClass())) {
            this.paramArray = ((List) arg).toArray();
        } else if (arg instanceof Set) {
            this.paramArray = ((Set) arg).toArray();
        } else if (java.util.Set.class.isAssignableFrom(arg.getClass())) {
            this.paramArray = ((Set) arg).toArray();
        }else {
            if (arg instanceof Number || arg instanceof Character || arg instanceof Boolean || arg instanceof String) {
                this.paramArray = new Object[]{arg};
            }
        }
        if (paramArray == null && paramMap == null) {
            this.paramMap = Utils.toMap(arg);
        }
    }

}