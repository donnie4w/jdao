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

@Deprecated
public class Util {

//    protected static boolean evaluateExpression(String expression, ParamContext context) {
//        if (expression.contains("&&")) {
//            String[] parts = expression.split("&&");
//            for (String part : parts) {
//                if (!evaluateSimpleExpression(part.trim(), context)) {
//                    return false;
//                }
//            }
//            return true;
//        } else if (expression.contains(" and ")) {
//            String[] parts = expression.split(" and ");
//            for (String part : parts) {
//                if (!evaluateSimpleExpression(part.trim(), context)) {
//                    return false;
//                }
//            }
//            return true;
//        } else if (expression.contains("||")) {
//            String[] parts = expression.split("\\|\\|");
//            for (String part : parts) {
//                if (evaluateSimpleExpression(part.trim(), context)) {
//                    return true;
//                }
//            }
//            return false;
//        } else if (expression.contains(" or ")) {
//            String[] parts = expression.split(" or ");
//            for (String part : parts) {
//                if (evaluateSimpleExpression(part.trim(), context)) {
//                    return true;
//                }
//            }
//            return false;
//        } else {
//            return evaluateSimpleExpression(expression.trim(), context);
//        }
//    }
//
//    private static boolean evaluateSimpleExpression(String expression, ParamContext context) {
//        String operator = "";
//        if (expression.contains("==")) {
//            operator = "==";
//        } else if (expression.contains("!=")) {
//            operator = "!=";
//        } else if (expression.contains(">=")) {
//            operator = ">=";
//        } else if (expression.contains("<=")) {
//            operator = "<=";
//        } else if (expression.contains(">")) {
//            operator = ">";
//        } else if (expression.contains("<")) {
//            operator = "<";
//        } else {
//            throw new IllegalArgumentException("Jdao Mapper invalid expression: " + expression);
//        }
//
//        String[] parts = expression.split(operator);
//        if (parts.length != 2) {
//            throw new IllegalArgumentException("Jdao Mapper invalid expression: " + expression);
//        }
//
//        String key = parts[0].trim();
//        String valueStr = parts[1].trim().replace("\"", "");
//        Object actualValue = null;
//        if (context instanceof ForeachContext) {
//            if (key.contains(".")) {
//                String[] vs = key.split(".");
//                String v_key = vs[0];
//                String v_attr = vs[1];
//                Object o = context.get(v_key);
//                if (o == null) {
//                    o = context.get(v_key);
//                }
//                if (o != null) {
//                    if (o instanceof Map) {
//                        actualValue = ((Map) o).get(v_attr);
//                    } else {
//                        actualValue = getPropertyValue(o, v_attr);
//                    }
//                }
//            } else if (key.matches(".*\\[.*\\].*")) {
//                String[] vs = key.split("\\[");
//                String v_key = vs[0];
//                actualValue = context.get(v_key);
//            } else {
//                actualValue = context.get(key);
//            }
//        } else {
//            actualValue = context.get(key);
//        }
//
//        boolean isNullCheck = "null".equals(valueStr) | "nil".equals(valueStr) | "none".equals(valueStr);
//
//        switch (operator) {
//            case "==":
//                if (isNullCheck) {
//                    return actualValue == null;
//                } else {
//                    return valueStr.equals(String.valueOf(actualValue));
//                }
//            case "!=":
//                if (isNullCheck) {
//                    return actualValue != null;
//                } else {
//                    return !valueStr.equals(String.valueOf(actualValue));
//                }
//            case ">":
//                return compareValues(actualValue, valueStr) > 0;
//            case "<":
//                return compareValues(actualValue, valueStr) < 0;
//            case ">=":
//                return compareValues(actualValue, valueStr) >= 0;
//            case "<=":
//                return compareValues(actualValue, valueStr) <= 0;
//            default:
//                throw new IllegalArgumentException("Jdao Mapper unsupported operator: " + operator);
//        }
//    }
//
//    public static Object getPropertyValue(Object obj, String propertyName) {
//        try {
//            Field field = obj.getClass().getDeclaredField(propertyName);
//            field.setAccessible(true);
//            return field.get(obj);
//        } catch (Exception e) {
//        }
//        return null;
//    }
//
//
//    private static int compareValues(Object actualValue, String expectedValue) {
//        if (actualValue instanceof Number) {
//            Double actual = ((Number) actualValue).doubleValue();
//            Double expected = Double.valueOf(expectedValue);
//            return actual.compareTo(expected);
//        } else if (actualValue instanceof String) {
//            return ((String) actualValue).compareTo(expectedValue);
//        } else {
//            throw new IllegalArgumentException("Jdao Mapper unsupported comparison for type: " + actualValue.getClass().getName());
//        }
//    }
}