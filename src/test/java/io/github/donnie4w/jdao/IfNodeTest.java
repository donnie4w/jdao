package io.github.donnie4w.jdao;

import io.github.donnie4w.jdao.mapper.IfNode;
import io.github.donnie4w.jdao.mapper.ParamContext;
import org.junit.Test;

public class IfNodeTest {

    @Test
    public void runTests() {
        testEqualExpression();
        testNotEqualExpression();
        testGreaterThanExpression();
        testLessThanExpression();
        testLogicalAndExpression();
    }

    @Test
    public void testEqualExpression() {
        ParamContext context = new ParamContext();
        context.put("age", "");

        IfNode ifNode = new IfNode("SELECT * FROM users WHERE age = 30", "age == \"\" ");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM users WHERE age = 30") : "Test failed: testEqualExpression";

        ifNode = new IfNode("SELECT * FROM users WHERE age = 25", "age == \"\" ");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM users WHERE age = 25") : "Test failed: testEqualExpression";
    }

    @Test
    public void testNotEqualExpression() {
        ParamContext context = new ParamContext();
        context.put("status", "active");

        IfNode ifNode = new IfNode("SELECT * FROM users WHERE status = 'active'", "status != \"inactive\"");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM users WHERE status = 'active'") : "Test failed: testNotEqualExpression";

        ifNode = new IfNode("SELECT * FROM users WHERE status = 'inactive'", "status == \"active\"");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM users WHERE status = 'inactive'") : "Test failed: testNotEqualExpression";
    }

    @Test
    public void testGreaterThanExpression() {
        ParamContext context = new ParamContext();
        context.put("salary", 5000);

        IfNode ifNode = new IfNode( "SELECT * FROM employees WHERE salary > 4000","salary > 4000");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM employees WHERE salary > 4000") : "Test failed: testGreaterThanExpression";

        ifNode = new IfNode( "SELECT * FROM employees WHERE salary > 6000","salary == 5000");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM employees WHERE salary > 6000") : "Test failed: testGreaterThanExpression";
    }

    @Test
    public void testLessThanExpression() {
        ParamContext context = new ParamContext();
        context.put("experience", 5);

        IfNode ifNode = new IfNode( "SELECT * FROM jobs WHERE experience < 10 years","experience < 10");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM jobs WHERE experience < 10 years") : "Test failed: testLessThanExpression";

        ifNode = new IfNode( "SELECT * FROM jobs WHERE experience < 3 years","experience == 5");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM jobs WHERE experience < 3 years") : "Test failed: testLessThanExpression";
    }

    @Test
    public void testLogicalAndExpression() {
        ParamContext context = new ParamContext();
        context.put("age", 30);
        context.put("status", "active");

        IfNode ifNode = new IfNode( "SELECT * FROM users WHERE age = 30 AND status = 'active'","age == 30 && status == \"active\"");
        assert ifNode.apply(context).getSql().equals("SELECT * FROM users WHERE age = 30 AND status = 'active'") : "Test failed: testLogicalAndExpression";
    }
}
