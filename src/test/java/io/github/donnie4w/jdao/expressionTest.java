package io.github.donnie4w.jdao;

import io.github.donnie4w.jdao.mapper.ForeachContext;
import io.github.donnie4w.jdao.mapper.ParamContext;
import io.github.donnie4w.jdao.util.ExpressionEvaluator;
import io.github.donnie4w.jdao.util.RPNCalculator;
import org.junit.Test;

public class expressionTest {

    @Test
    public void test() throws Exception {
        String expression = "user != null && user[index].name == 'Alice' || user.id!=0";
        ParamContext paramContext = new ParamContext();
        User user = new User("Alice", "Engineer");
        paramContext.put("user", user);

        runTest("user!= null", paramContext, true); // user 不为 null
        runTest("user.name == 'Alice'", paramContext, true); // user.name == 'Alice'
        runTest("user.name != 'Bob'", paramContext, true); // user.name != 'Bob'
        runTest("user.job == 'Engineer'", paramContext, true); // user.job == 'Engineer'

        ForeachContext foreachContext = new ForeachContext(paramContext, user);
        foreachContext.setItem("user", user);
        foreachContext.setIndex("index", 0);

        runTest("user.name== 'Alice'", foreachContext, true); // user.name == 'Alice' in ForeachContext

        String[] names = {"Alice", "Bob", "Charlie"};
        paramContext.put("names", names);
        foreachContext.put("names", names);
        runTest("names[index] == 'Alice'", foreachContext, true); // names[0] == 'Alice'
        runTest("names[1] == 'Bob'", paramContext, true); // names[1] == 'Bob'
        runTest("names[2] == 'Charlie'", paramContext, true); // names[2] == 'Charlie'
    }


     void runTest(String expression, ParamContext context, boolean expected) throws Exception {
        boolean r = RPNCalculator.evaluate(ExpressionEvaluator.resolveExpression(expression, context));
        System.out.println("Expression: " + expression + ":" + (r == expected ? "true" : "false"));
    }

     class User {
        private final String name;
        private final String job;

        public User(String name, String job) {
            this.name = name;
            this.job = job;
        }

        public String getName() {
            return name;
        }

        public String getJob() {
            return job;
        }
    }
}
