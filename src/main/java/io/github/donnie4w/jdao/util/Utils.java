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

import io.github.donnie4w.jdao.handle.JdaoRuntimeException;
import io.github.donnie4w.jdao.mapper.ForeachContext;
import io.github.donnie4w.jdao.mapper.ParamContext;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String upperFirstChar(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        char firstChar = word.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            return Character.toUpperCase(firstChar) + word.substring(1);
        }
        return word;
    }

    @Deprecated
    public static String dateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    @Deprecated
    public static boolean isContainsLowerCase(String str) {
        if (str == null)
            return false;
        int length = str.length();
        if (length == 0)
            return false;
        for (int i = 0; i < length; i++) {
            if (Character.isLowerCase(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    //java8
    public static String getPackageName(Class<?> clazz) {
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return className.substring(0, lastDotIndex);
        } else {
            return "";
        }
    }

    public static boolean stringValid(String str) {
        return str != null && str.trim().length() > 0;
    }

    public static String extractVariables(String expression, ParamContext paramContext) {
        StringBuilder currentVariable = new StringBuilder();
        boolean insideQuotes = false;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '\'' || c == '\"') {
                insideQuotes = !insideQuotes;
            }
            if (insideQuotes) {
                result.append(c);
                continue;
            }
            if (Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '[' || c == ']') {
                currentVariable.append(c);
            } else {
                if (currentVariable.length() > 0) {
                    String s = currentVariable.toString();
                    if (isVariable(s)) {
                        Object value = Utils.resolveVariableValue(s, paramContext);
                        if (value != null) {
                            if (value instanceof String) {
                                result.append("'" + value + "'");
                            } else {
                                result.append(value);
                            }
                        } else {
                            result.append("null");
                        }
                    } else {
                        result.append(currentVariable);
                    }
                    currentVariable.setLength(0);
                }
                result.append(c);
            }
        }
        if (currentVariable.length() > 0) {
            String s = currentVariable.toString();
            if (isVariable(s)) {
                Object value = Utils.resolveVariableValue(s, paramContext);
                if (value != null) {
                    if (value instanceof String) {
                        result.append("'" + value + "'");
                    } else {
                        result.append(value);
                    }
                } else {
                    result.append("null");
                }
            } else {
                result.append(currentVariable);
            }
        }
        return result.toString();
    }

    public static boolean isVariable(String s) {
        return !s.equals("null") && !s.matches("^\\d+$") && !s.equals("true") && !s.equals("false");
    }

    public static Map<String, Object> toMap(Object arg) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = arg.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (!Modifier.isStatic(f.getModifiers())) {
                try {
                    f.setAccessible(true);
                    Object value = f.get(arg);
                    if (value != null) {
                        map.put(f.getName(), value);
                    }
                } catch (Exception e) {
                    if (Logger.isVaild()) {
                        Logger.severe("Field [", f.getName(), "] is not accessible for Object [", arg.getClass().getName(), "]", e);
                    }
                }
            }
        }

        Method[] methods = arg.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().toLowerCase().startsWith("get") && !Modifier.isStatic(m.getModifiers())) {
                try {
                    String name = m.getName().substring(3).toLowerCase();
                    if (!map.containsKey(name)) {
                        Object value = m.invoke(arg);
                        if (value != null) {
                            map.put(name, value);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

        return map;
    }

    public static Object resolveVariableValue(String variable, ParamContext paramContext) {
        Object value = null;
        if (variable.contains(".")) {
            return getValueByPath(variable, paramContext);
        } else if (variable.contains("[")) {
            int startIndex = variable.indexOf("[");
            int endIndex = variable.indexOf("]");
            if (startIndex > 0 && endIndex > startIndex) {
                String objectName = variable.substring(0, startIndex);
                String indexStr = variable.substring(startIndex + 1, endIndex);
                int index = 0;
                if (indexStr.matches("^\\d+$")) {
                    index = Integer.parseInt(indexStr);
                } else if (paramContext instanceof ForeachContext) {
                    index = ((ForeachContext) paramContext).getIndex(indexStr);
                }
                value = getObjectFromContext(objectName, paramContext);
                if (value != null && value.getClass().isArray()) {
                    int length = Array.getLength(value);
                    if (index >= 0 && index < length) {
                        value = Array.get(value, index);
                    } else {
                        value = null;
                    }
                }
            }
        } else {
            value = getObjectFromContext(variable, paramContext);
        }
        return value;
    }


    public static Object getValueByPath(String path, Object obj) {
        if (obj == null || path == null || path.isEmpty()) {
            return null;
        }
        String[] parts = path.split("\\.");
        Object currentObj = obj;
        String part = null;
        for (int i = 0; i < parts.length; i++) {
            if (currentObj == null) {
                break;
            }
            part = parts[i];
            if (currentObj instanceof ParamContext) {
                currentObj = getObjectFromContext(part, (ParamContext) currentObj);
            } else if (currentObj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) currentObj;
                currentObj = map.get(part);
            } else {
                currentObj = ReflectionUtil.getFieldValue(currentObj, part);
            }
        }
        if (currentObj == null) {
            throw new JdaoRuntimeException("Can't find object or value [" + part + "] in " + path);
        }
        return currentObj;
    }

    private static Object getObjectFromContext(String key, ParamContext paramContext) {
        Object value = null;
        if (paramContext instanceof ForeachContext) {
            value = ((ForeachContext) paramContext).getItem(key);
            if (value == null) {
                value = ((ForeachContext) paramContext).getIndex(key);
            }
        }
        if (value == null) {
            value = paramContext.get(key);
        }
        return value;
    }

}
