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
package io.github.donnie4w.jdao.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public enum SeriaType {
    BOOLEAN(1),
    STRING(2),
    DOUBLE(3),
    FLOAT(4),
    LONG(5),
    INTEGER(6),
    SHORT(7),
    BYTE(8),
    CHAR(9),
    BYTE_ARRAY(10),
    DATE(11),
    BIGINTEGER(12),
    BIGDECIMAL(13);


    private final int code;

    SeriaType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    private static final Map<Class<?>, SeriaType> typeMapping = new HashMap<>();

    static {
        typeMapping.put(Boolean.class, BOOLEAN);
        typeMapping.put(boolean.class, BOOLEAN);
        typeMapping.put(String.class, STRING);
        typeMapping.put(Double.class, DOUBLE);
        typeMapping.put(double.class, DOUBLE);
        typeMapping.put(Float.class, FLOAT);
        typeMapping.put(float.class, FLOAT);
        typeMapping.put(Long.class, LONG);
        typeMapping.put(long.class, LONG);
        typeMapping.put(Integer.class, INTEGER);
        typeMapping.put(int.class, INTEGER);
        typeMapping.put(Short.class, SHORT);
        typeMapping.put(short.class, SHORT);
        typeMapping.put(Byte.class, BYTE);
        typeMapping.put(byte.class, BYTE);
        typeMapping.put(Character.class, CHAR);
        typeMapping.put(char.class, CHAR);
        typeMapping.put(byte[].class, BYTE_ARRAY);
        typeMapping.put(java.util.Date.class, DATE);
        typeMapping.put(java.sql.Date.class, DATE);
        typeMapping.put(java.sql.Time.class, DATE);
        typeMapping.put(java.sql.Timestamp.class, DATE);
        typeMapping.put(BigInteger.class, BIGINTEGER);
        typeMapping.put(BigDecimal.class, BIGDECIMAL);


    }

    public static SeriaType fromClass(Class<?> clazz) {
        return typeMapping.get(clazz);
    }

}