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

import io.github.donnie4w.jdao.base.Util;
import io.github.donnie4w.jdao.handle.JdaoException;
import io.github.donnie4w.jdao.handle.JdaoRuntimeException;
import io.github.donnie4w.jdao.util.Logger;
import io.github.donnie4w.jdao.util.Utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamBean {
    private final String namespace;
    private final String id;
    private final SqlType sqlType;
    private String sql;
    private String[] parameterNames;
    private String inputType = "";
    private String outputType = "";
    private boolean inputIsClass = false;
    private Class<?> inputClass;
    private Class<?> resultClass;

    public String getSql() {
        return sql;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public String getOutputType() {
        return outputType;
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }

    public ParamBean(String namespace, String id, String sqlType, String sql, String inputType, String outputType) {
        this.namespace = namespace;
        this.id = id;
        switch (sqlType) {
            case "select":
                this.sqlType = SqlType.SELECT;
                break;
            case "update":
                this.sqlType = SqlType.UPDATE;
                break;
            case "insert":
                this.sqlType = SqlType.INSERT;
                break;
            case "delete":
                this.sqlType = SqlType.DELETE;
                break;
            default:
                throw new JdaoRuntimeException("Invalid sql type: " + sqlType);
        }
        this.sql = sql;
        this.inputType = inputType;
        this.outputType = outputType;

        parseSql(sql);

        if (Utils.stringValid(inputType) && !isDBType(inputType) && !isContainer(inputType)) {
            try {
                inputClass = Class.forName(inputType);
                inputIsClass = true;
            } catch (Exception e) {
                String s = String.format("[Input Type,Class Loading Failed:%s ],[namespace: %s],[id:%s]", inputType, namespace, id);
                Logger.severe(s);
                throw new JdaoRuntimeException(s);
            }
        }

        if (Utils.stringValid(outputType) && !isDBType(outputType) && !isContainer(outputType)) {
            try {
                resultClass = Class.forName(outputType);
            } catch (Exception e) {
                String s = String.format("[Output Type, Class Loading Failed:%s ],[namespace: %s],[id:%s]", outputType, namespace, id);
                Logger.severe(s);
                throw new JdaoRuntimeException(s);
            }
        }
    }


    public void parseSql(String sql) {
        Pattern pattern = Pattern.compile("#\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(sql);
        StringBuffer modifiedSql = new StringBuffer();
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String parameterName = matcher.group(1);
            list.add(parameterName);
            matcher.appendReplacement(modifiedSql, "?");
        }
        matcher.appendTail(modifiedSql);
        this.parameterNames = list.toArray(new String[list.size()]);
        this.sql = modifiedSql.toString();
    }

    private void throw_num_no_match(int expectvalue, int gotvalue) throws JdaoException {
        throw new JdaoException(String.format("the parameter number does not match the configuration:Expected %d but got %d. [namespace:%s][mapper id:%s]", expectvalue, gotvalue, namespace, id));
    }

    private void throw_invalid_parameter(String getType) throws JdaoException {
        String s = String.format("Invalid parameter type: Expected %s but get %s [namespace:%s][mapper id:%s]", inputType, getType, namespace, id);
        throw new JdaoException(s);
    }

    private Object[] setParameterByDBtype(Object parameter) throws JdaoException {
        List<Object> rlist = new ArrayList<>();
        if (parameterNames.length > 1) {
            if (!parameter.getClass().isArray()) {
                if (!isDBType(parameter.getClass().getSimpleName())) {
                    throw_invalid_parameter(parameter.getClass().getSimpleName());
                }
                throw_num_no_match(parameterNames.length, 1);
            }
            int size = Array.getLength(parameter);
            if (parameterNames.length != size) {
                throw_num_no_match(parameterNames.length, size);
            }
            for (int i = 0; i < size; i++) {
                rlist.add(Array.get(parameter, i));
            }
        } else if (parameterNames.length == 1) {
            if (parameter.getClass().isArray()) {
                int size = Array.getLength(parameter);
                if (size != 1) {
                    throw_num_no_match(1, size);
                }
                rlist.add(Array.get(parameter, 0));
            } else {
                if (!isDBType(parameter.getClass().getSimpleName())) {
                    throw_invalid_parameter(parameter.getClass().getSimpleName());
                }
                rlist.add(parameter);
            }
        }
        return rlist.toArray(new Object[rlist.size()]);
    }

    private Object[] setParameterByMap(Object parameter) throws JdaoException {
        List<Object> rlist = new ArrayList<>();
        Map args = null;
        if (parameter instanceof Map) {
            args = (Map) parameter;
        } else if (parameter.getClass().isArray()) {
            Object element = Array.get(parameter, 0);
            if (java.util.List.class.isAssignableFrom(element.getClass())) {
                rlist = (List) element;
                return rlist.toArray(new Object[rlist.size()]);
            } else if (element instanceof Map) {
                args = (Map) element;
            } else if (Array.getLength(element) == parameterNames.length) {
                return convertToArray(element);
            } else {
                throw_num_no_match(parameterNames.length, Array.getLength(element));
            }
        }
        if (args == null) {
            throw_invalid_parameter(parameter.getClass().getSimpleName());
        }
        for (String parameterName : parameterNames) {
            Object obj = args.get(parameterName);
            if (obj != null) {
                rlist.add(obj);
            } else {
                throw new JdaoException(String.format("The key [%s] is not found on the map [namespace:%s][mapper id:%s]", parameterName, namespace, id));
            }
        }
        return rlist.toArray(new Object[rlist.size()]);
    }

    private Object[] setParameterByList(Object parameter) throws JdaoException {
        List args = null;
        if (parameter instanceof List) {
            args = (List) parameter;
        } else if (parameter.getClass().isArray()) {
            Object element = Array.get(parameter, 0);
            if (java.util.List.class.isAssignableFrom(element.getClass())) {
                args = (List) element;
            } else if (Array.getLength(element) == parameterNames.length) {
                return convertToArray(element);
            } else {
                throw_num_no_match(parameterNames.length, Array.getLength(element));
            }
        }
        if (args == null) {
            throw_invalid_parameter(parameter.getClass().getSimpleName());
        }
        if (args.size() == parameterNames.length) {
            return args.toArray(new Object[args.size()]);
        } else {
            throw_num_no_match(parameterNames.length, args.size());
        }
        return null;
    }

    private Object[] setParameterBySet(Object parameter) throws JdaoException {
        Set args = null;
        if (parameter instanceof Set) {
            args = (Set) parameter;
        } else if (parameter.getClass().isArray()) {
            Object element = Array.get(parameter, 0);
            if (java.util.List.class.isAssignableFrom(element.getClass())) {
                List rlist = (List) element;
                return rlist.toArray(new Object[rlist.size()]);
            } else if (element instanceof Set) {
                args = (Set) element;
            } else if (Array.getLength(element) == parameterNames.length) {
                return convertToArray(element);
            } else {
                throw_num_no_match(parameterNames.length, Array.getLength(element));
            }
        }
        if (args == null) {
            throw_invalid_parameter(parameter.getClass().getSimpleName());
        }

        if (args.size() == parameterNames.length) {
            return args.toArray(new Object[args.size()]);
        } else {
            throw_num_no_match(parameterNames.length, args.size());
        }
        return null;
    }

    private Object[] setParameterByObject(Object parameter) throws JdaoException {
        Object arg = null;
        if (inputClass.isInstance(parameter)) {
            arg = parameter;
        } else if (parameter.getClass().isArray()) {
            Object element = Array.get(parameter, 0);
            if (inputClass.isInstance(element)) {
                arg = element;
            }
        }
        if (arg == null) {
            throw_invalid_parameter(parameter.getClass().getSimpleName());
        }
        return populateParameterMap(arg, parameterNames);
    }

    public Object[] setParameter(Object parameter) throws JdaoException {
        if (!Utils.stringValid(inputType) || parameter == null) {
            return null;
        }

        if (isDBType(inputType)) {
            return setParameterByDBtype(parameter);
        }

        if (inputType.equalsIgnoreCase("map")) {
            return setParameterByMap(parameter);
        }

        if (parameterNames.length > 1 && inputType.equalsIgnoreCase("list")) {
            return setParameterByList(parameter);
        }

        if (parameterNames.length > 1 && inputType.equalsIgnoreCase("set")) {
            return setParameterBySet(parameter);
        }

        if (inputIsClass) {
            return setParameterByObject(parameter);
        }

        if (java.util.Map.class.isAssignableFrom(parameter.getClass())) {
            return setParameterByMap(parameter);
        }

        if (java.util.List.class.isAssignableFrom(parameter.getClass())) {
            return setParameterByList(parameter);
        }

        if (java.util.Set.class.isAssignableFrom(parameter.getClass())) {
            return setParameterBySet(parameter);
        }

        List<Object> rlist = new ArrayList<>();
        if (parameterNames.length == 1) {
            if (!parameter.getClass().isArray()) {
                rlist.add(parameter);
            } else {
                Object element = Array.get(parameter, 0);
                if (element.getClass().isArray()) {
                    rlist.add(Array.get(element, 0));
                } else {
                    rlist.add(element);
                }
            }
        } else {
            if (!parameter.getClass().isArray()) {
                throw_invalid_parameter(parameter.getClass().getSimpleName());
            }
            if (Array.getLength(parameter) == parameterNames.length) {
                return convertToArray(parameter);
            }
            Object element = Array.get(parameter, 0);
            if (java.util.List.class.isAssignableFrom(element.getClass())) {
                rlist = (List) element;
                return rlist.toArray(new Object[rlist.size()]);
            } else if (element.getClass().isArray()) {
                if (Array.getLength(element) == parameterNames.length) {
                    return convertToArray(element);
                } else {
                    throw_num_no_match(parameterNames.length, Array.getLength(element));
                }
            }
            throw_invalid_parameter(parameter.getClass().getSimpleName());
        }
        return rlist.toArray(new Object[rlist.size()]);
    }

    private static final Set<String> dbTypes = new HashSet<>(Arrays.asList(
            "int", "Integer", "long", "Long", "byte", "Byte", "short", "Short", "float", "Float", "boolean", "Boolean",
            "string", "String", "Character", "char", "double", "Double", "BigDecimal", "BigInteger", "byte[]",
            "Date", "LocalDateTime", "LocalDate", "LocalTime"
    ));

    static boolean isDBType(String type) {
        return dbTypes.contains(type);
    }

    private static final Set<String> containerTypes = new HashSet<>(Arrays.asList(
            "Map", "java.util.Map", "List", "java.util.List", "Set", "java.util.Set",
            "ArrayList", "LinkedList", "Vector", "Stack", "HashSet", "LinkedHashSet",
            "TreeSet", "HashMap", "LinkedHashMap", "TreeMap", "Hashtable",
            "int[]", "Integer[]", "long[]", "Long[]", "byte[]", "Byte[]",
            "short[]", "Short[]", "float[]", "Float[]", "boolean[]", "Boolean[]",
            "double[]", "Double[]", "char[]", "Character[]",
            "String[]", "BigDecimal[]", "BigInteger[]", "Object[]",
            "map", "java.util.map", "list", "java.util.list", "set", "java.util.set",
            "arraylist", "linkedlist", "vector", "stack", "hashset", "linkedhashset",
            "treeset", "hashmap", "linkedhashmap", "treemap", "hashtable",
            "integer[]", "character[]", "string[]", "bigdecimal[]", "biginteger[]", "object[]"
    ));

    static boolean isContainer(String type) {
        return containerTypes.contains(type);
    }

    private static Object[] populateParameterMap(Object obj, String[] parameterNames) throws JdaoException {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        List<Object> rlist = new ArrayList<>();

        for (String parameterName : parameterNames) {
            try {
                Field field = clazz.getDeclaredField(Util.encodeFieldname(parameterName));
                field.setAccessible(true);
                rlist.add(field.get(obj));
            } catch (Exception e) {
                throw new JdaoException(String.format("parameter name [%s] could not be found in class[%s]", parameterName, clazz.getName()));
            }
        }
        return rlist.toArray(new Object[rlist.size()]);
    }

    public static Object[] convertToArray(Object array) throws JdaoException {
        if (array == null || !array.getClass().isArray()) {
            throw new JdaoException("Provided argument is not an array");
        }

        int length = Array.getLength(array);
        Object[] objectArray = new Object[length];

        for (int i = 0; i < length; i++) {
            objectArray[i] = Array.get(array, i);
        }

        return objectArray;
    }
}
