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

import io.github.donnie4w.jdao.base.FieldBean;
import io.github.donnie4w.jdao.base.Log;
import io.github.donnie4w.jdao.base.Scanner;
import io.github.donnie4w.jdao.base.Util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class DataBean implements Iterable<String> {

    private final static Log logger = Log.newInstance();

    private final Map<String, FieldBean> fieldNameMap = new HashMap<>();
    private final Map<Integer, FieldBean> fieldIndexMap = new HashMap<>();

    public void logger(boolean on) {
        logger.logOn(on);
    }

    public void put(String fieldName, int fieldIndex, Object fieldValue) {
        FieldBean fb = new FieldBean(fieldName, fieldIndex, fieldValue);
        this.fieldNameMap.put(fieldName, fb);
        this.fieldIndexMap.put(fieldIndex, fb);
    }

    public FieldBean findField(String fieldName) {
        return this.fieldNameMap.get(fieldName);
    }

    public FieldBean findField(int index) {
        return this.fieldIndexMap.get(index);
    }

    public Object getValue(String name) {
        FieldBean fb = fieldNameMap.get(name);
        if (fb != null) {
            return fb.value();
        }
        return null;
    }

    public Object getValue(int index) {
        FieldBean fb = fieldIndexMap.get(index);
        if (fb != null) {
            return fb.value();
        }
        return null;
    }

    public int size() {
        return this.fieldNameMap.size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        for (String name : this) {
            map.put(name, getValue(name));
        }
        return map;
    }

    public List<Object> toList() {
        List<Object> list = new ArrayList<>();
        for (String name : this) {
            list.add(getValue(name));
        }
        return list;
    }

    public Set<Object> toSet() {
        Set<Object> set = new LinkedHashSet<>();
        set.addAll(toList());
        return set;
    }

    public <T> T scan(Class<T> beanClass) throws JdaoException {
        try {
            T targetBean = beanClass.getDeclaredConstructor().newInstance();
            if (Scanner.class.isAssignableFrom(beanClass)) {
                Scanner scanner = (Scanner) targetBean;
                for (Map.Entry<String, FieldBean> entry : fieldNameMap.entrySet()) {
                    scanner.scan(entry.getKey(), entry.getValue().value());
                }
                return targetBean;
            }
            java.lang.reflect.Field[] fields = targetBean.getClass().getDeclaredFields();
            if (fields.length <= fieldNameMap.size()) {
                for (java.lang.reflect.Field field : fields) {
                    Class<?> fieldType = field.getType();
                    Object value = getValue(Util.decodeFieldname(field.getName()));
                    if (value != null) {
                        Object compatibleValue = convertIfCompatible(value, fieldType);
                        if (compatibleValue == null) {
                            logger.log("[IncompatibleType][Field: " + field.getName() + ", ExpectedType: " + fieldType.getName() + ", ActualType: " + value.getClass().getName() + "]");
                            continue;
                        }
                        String setterMethodName = "set" + capitalize(field.getName());
                        try {
                            Method setterMethod = beanClass.getMethod(setterMethodName, fieldType);
                            setterMethod.invoke(targetBean, compatibleValue);
                        } catch (NoSuchMethodException e) {
                            logger.log("[NoSuchMethodException][", setterMethodName, "]");
                        }
                    }
                }
            } else {
                for (Map.Entry<String, FieldBean> entry : fieldNameMap.entrySet()) {
                    String fieldName = Util.encodeFieldname(entry.getKey());
                    Object value = entry.getValue().value();
                    try {
                        Field field = beanClass.getDeclaredField(fieldName);
                        Class<?> fieldType = field.getType();
                        Object compatibleValue = convertIfCompatible(value, fieldType);
                        if (compatibleValue == null) {
                            logger.log("[IncompatibleType][Field: " + fieldName + ", ExpectedType: " + fieldType.getName() + ", ActualType: " + value.getClass().getName() + "]");
                            continue;
                        }
                        String setterMethodName = "set" + capitalize(fieldName);
                        try {
                            Method setterMethod = beanClass.getMethod(setterMethodName, fieldType);
                            setterMethod.invoke(targetBean, compatibleValue);
                        } catch (NoSuchMethodException e) {
                            logger.log("[NoSuchMethodException][", setterMethodName, "]");
                        }
                    } catch (Exception e) {
                        logger.log("[NoSuchFieldException][", fieldName, "]");
                    }
                }
            }
            return targetBean;
        } catch (Exception e) {
            throw new JdaoException(e);
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private Object convertIfCompatible(Object value, Class<?> fieldType) {
        if (fieldType.isInstance(value)) {
            return value;
        }
        switch (fieldType.getSimpleName()) {
            case "int":
                return Util.asInt(value);
            case "Integer":
                return Util.asInt(value);
            case "Long":
                return Util.asLong(value);
            case "long":
                return Util.asLong(value);
            case "Short":
                return Util.asShort(value);
            case "short":
                return Util.asShort(value);
            case "Float":
                return Util.asFloat(value);
            case "float":
                return Util.asFloat(value);
            case "Double":
                return Util.asDouble(value);
            case "double":
                return Util.asDouble(value);
            case "String":
                return Util.asString(value);
            case "BigDecimal":
                return Util.asBigDecimal(value);
            case "BigInteger":
                return Util.asBigInteger(value);
            case "Boolean":
                return Util.asBoolean(value);
            case "boolean":
                return Util.asBoolean(value);
            case "char":
                return Util.asChar(value);
            case "Character":
                return Util.asChar(value);
            case "Date":
                try {
                    return Util.asDate(value);
                } catch (JdaoException e) {
                }
            case "LocalDateTime":
                try {
                    return Util.asLocalDateTime(value);
                } catch (JdaoException e) {
                }
            case "LocalDate":
                try {
                    return Util.asLocalDate(value);
                } catch (JdaoException e) {
                }
            case "LocalTime":
                try {
                    return Util.asLocalTime(value);
                } catch (JdaoException e) {
                }
            default:
                return Util.asBytes(value);
        }
    }

    /**
     * @return
     */
    @Override
    public Iterator<String> iterator() {
        return fieldNameMap.keySet().iterator();
    }

    public Iterator<Integer> iteratorWithIndex() {
        return fieldIndexMap.keySet().iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DataBean{").append("\n");
        for (int i = 0; i < size(); i++) {
            FieldBean fb = findField(i);
            sb.append("\t").append(fb).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
