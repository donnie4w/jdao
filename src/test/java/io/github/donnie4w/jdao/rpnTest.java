package io.github.donnie4w.jdao;

import io.github.donnie4w.jdao.util.RPNCalculator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class rpnTest {

    @Test
    public void rpntest() {
        Map<String, Object> context = new HashMap<>();
        context.put("x", 10);
        context.put("y", "hello");
        context.put("z", null);
        context.put("s", "11");
        context.put("t", true);

        String expression = "( x|1 < 3|15) && (y== \"hello\") && (z == null)";
        boolean result = RPNCalculator.evaluate(expression, context);
        System.out.println("Result: " + result);

        expression = "( x|1 < 3|15) && (y== \"hello\") && (z != null)";
        result = RPNCalculator.evaluate(expression, context);
        System.out.println("Result: " + result);

        expression = "( x|1 < 3|15) || (y== \"hello1\") && (z == null)";
        result = RPNCalculator.evaluate(expression, context);
        System.out.println("Result: " + result);

        expression = "( s==\"s\") && (y== \"hello\") && (z == null)";
        result = RPNCalculator.evaluate(expression, context);
        System.out.println("Result: " + result);


        expression = "z==null";
        result = RPNCalculator.evaluate(expression, context);
        System.out.println("Result: " + result);


        expression = "'www>>>>2'!= null";
        result = RPNCalculator.evaluate(expression, context);
        System.out.println("Result: " + result);

        expression = "t && true";
        result = RPNCalculator.evaluate(expression, context);
        System.out.println("Result: " + result);
    }
}
