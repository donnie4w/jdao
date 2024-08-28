package io.github.donnie4w.jdao.util;

import io.github.donnie4w.jdao.mapper.ForeachContext;
import io.github.donnie4w.jdao.mapper.ParamContext;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static List<String> extractVariables(String expression) {
        List<String> variables = new ArrayList<>();
        StringBuilder currentVariable = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '\'' || c == '\"') {
                insideQuotes = !insideQuotes;
            }
            if (insideQuotes) {
                continue;
            }
            if (Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '[' || c == ']') {
                currentVariable.append(c);
            } else {
                if (currentVariable.length() > 0) {
                    String s = currentVariable.toString();
                    if (!s.equals("null") && !s.matches("^\\d+$")) {
                        variables.add(s);
                    }
                    currentVariable.setLength(0);
                }
            }
        }
        if (currentVariable.length() > 0) {
            String s = currentVariable.toString();
            if (!s.equals("null") && !s.matches("^\\d+$")) {
                variables.add(s);
            }
        }
        return variables;
    }

    public static Map<String, Object> toMap(Object arg) {
        Map<String, Object> map = new HashMap();
        Field[] field = arg.getClass().getDeclaredFields();
        for (Field f : field) {
            try {
                f.setAccessible(true);
                map.put(f.getName(), f.get(arg));
            } catch (Exception e) {
                if (Logger.isVaild()) {
                    Logger.severe("Field " + f.getName() + " is not accessible");
                }
            }
        }
        Method[] methods = arg.getClass().getMethods();
        for (Method m : methods) {
            try {
                if (m.getName().toLowerCase().startsWith("get")) {
                    String name = m.getName().substring("get".length()).toLowerCase();
                    if (!map.containsKey(name)) {
                        map.put(name, m.invoke(arg));
                    }
                }
            } catch (Exception e) {
            }
        }
        return map;
    }

    public static Object resolveVariableValue(String variable, ParamContext paramContext) {
        Object value = null;
        if (variable.contains(".")) {
            String[] parts = variable.split("\\.");
            if (parts.length > 1) {
                String objectName = parts[0];
                String fieldName = parts[1];
                value = getObjectFromContext(objectName, paramContext);
                if (value != null) {
                    value = ReflectionUtil.getFieldValue(value, fieldName);
                }
            }
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

    private static Object getObjectFromContext(String key, ParamContext paramContext) {
        Object value = null;
        if (paramContext instanceof ForeachContext) {
            value = ((ForeachContext) paramContext).getItem(key);
        }
        if (value == null) {
            value = paramContext.get(key);
        }
        return value;
    }
}
