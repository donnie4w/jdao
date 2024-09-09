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
import io.github.donnie4w.jdao.mapper.ParamContext;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RPNCalculator {
    public static boolean evaluate(String expression) {
        try {
            if (expression == null || expression.isEmpty()) {
                return false;
            }
            List<String> rpn = toRPN(expression);
            return evaluateRPN(rpn);
        } catch (Exception e) {
            throw new JdaoRuntimeException("Error evaluating expression: " + expression + " ,Error: " + e.getMessage());
        }
    }

    public static boolean evaluate(String expression, Object context) {
        return evaluate(ExpressionEvaluator.resolveExpression(expression, new ParamContext(context)));
    }

    private static List<String> toRPN(String expression) {
        String regex = "\\s*(\\(|\\)|&&|\\|\\||==|!=|<=|>=|\\+|\\-|\\*|/|%|&|\\||~|>|<|\\w+|\"[^\"]*\"|'[^']*')\\s*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);

        Stack<String> operatorStack = new Stack<>();
        List<String> output = new ArrayList<>();

        while (matcher.find()) {
            String token = matcher.group(1);
//            if (isNumeric(token) || isStringLiteral(token) || "null".equals(token)) {
//                output.add(token);
//            } else
            if (isOperator(token)) {
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token)) {
                    output.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if ("(".equals(token)) {
                operatorStack.push(token);
            } else if (")".equals(token)) {
                while (!operatorStack.isEmpty() && !"(".equals(operatorStack.peek())) {
                    output.add(operatorStack.pop());
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pop();
                }
            } else {
                output.add(token);
            }
        }

        while (!operatorStack.isEmpty()) {
            output.add(operatorStack.pop());
        }

        return output;
    }

    private static boolean evaluateRPN(List<String> tokens) {
        Stack<Object> stack = new Stack<>();
        for (String token : tokens) {
            if (isNumeric(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isStringLiteral(token)) {
                stack.push(token.substring(1, token.length() - 1));
            } else if ("null".equals(token)) {
                stack.push(null);
            } else if ("true".equals(token) || "false".equals(token)) {
                stack.push(Boolean.parseBoolean(token));
            } else if (isOperator(token)) {
                Object b = stack.pop();
                Object a = token.equals("~") ? b : stack.pop();
                switch (token) {
                    case "+":
                        stack.push((Double) a + (Double) b);
                        break;
                    case "-":
                        stack.push((Double) a - (Double) b);
                        break;
                    case "*":
                        stack.push((Double) a * (Double) b);
                        break;
                    case "/":
                        stack.push((Double) a / (Double) b);
                        break;
                    case "%":
                        stack.push((Double) a % (Double) b);
                        break;
                    case "&":
                        stack.push(castToInteger(a) & castToInteger(b));
                        break;
                    case "|":
                        stack.push(castToInteger(a) | castToInteger(b));
                        break;
                    case "~":
                        stack.push(~castToInteger(a));
                        break;
                    case "&&":
                        stack.push(toBoolean(a) && toBoolean(b));
                        break;
                    case "||":
                        stack.push(toBoolean(a) || toBoolean(b));
                        break;
                    case "==":
                        stack.push(equals(a, b));
                        break;
                    case "!=":
                        stack.push(!equals(a, b));
                        break;
                    case ">":
                        if (a == null || b == null) {
                            return false;
                        }
                        stack.push(compare(a, b) > 0);
                        break;
                    case "<":
                        if (a == null || b == null) {
                            return false;
                        }
                        stack.push(compare(a, b) < 0);
                        break;
                    case ">=":
                        if (a == null || b == null) {
                            return false;
                        }
                        stack.push(compare(a, b) >= 0);
                        break;
                    case "<=":
                        if (a == null || b == null) {
                            return false;
                        }
                        stack.push(compare(a, b) <= 0);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported operator: " + token);
                }
            } else {
                stack.push(token);
            }
        }

        return toBoolean(stack.pop());
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isStringLiteral(String str) {
        return str.length() > 1 && (str.startsWith("\"") && str.endsWith("\"") || str.startsWith("'") && str.endsWith("'"));
    }

    private static boolean isOperator(String token) {
        return "+-*/%&&||==!=><>=<=&|~".contains(token);
    }

    private static int precedence(String operator) {
        switch (operator) {
            case "||":
            case "&&":
                return 1;
            case "==":
            case "!=":
            case ">":
            case "<":
            case ">=":
            case "<=":
                return 2;
            case "+":
            case "-":
                return 3;
            case "*":
            case "/":
            case "%":
                return 4;
            case "|":
            case "&":
            case "~":
                return 5;
            default:
                return 0;
        }
    }

    private static boolean toBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof Double) {
            return (Double) obj != 0;
        } else if (obj instanceof String) {
            return !((String) obj).isEmpty();
        } else {
            return obj != null;
        }
    }

    private static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    private static int compare(Object a, Object b) {
        if (a instanceof Number && b instanceof Number) {
            return Double.compare(((Number) a).doubleValue(), ((Number) b).doubleValue());
        }
        if (a instanceof String && b instanceof String) {
            return ((String) a).compareTo((String) b);
        }
        throw new IllegalArgumentException("Cannot compare " + a + " with " + b);
    }

    private static int castToInteger(Object obj) {
        if (obj instanceof Double) {
            return ((Double) obj).intValue();
        } else {
            throw new IllegalArgumentException("Expected a numeric value but got: " + obj);
        }
    }
}
