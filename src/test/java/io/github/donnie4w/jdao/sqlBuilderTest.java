package io.github.donnie4w.jdao;

import io.github.donnie4w.jdao.handle.SqlBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class sqlBuilderTest {

    @Test
    public void sqlbuildertest() {
        SqlBuilder builder = SqlBuilder.newInstance();
        Map<String, Object> context = new HashMap<>();
        context.put("username", "John Doe");
        context.put("age", 30);
        context.put("emails", Arrays.asList("john@example.com", "doe@example.com", "john.doe@example.com"));

        builder.append("SELECT * FROM users")
                .appendTrim("WHERE", "", "AND", "", trimBuilder -> {
                    trimBuilder.appendIf("username != null", context, "AND username = ?", context.get("username"))
                            .appendIf("age > 18", context, "AND age = ?", context.get("age"))
                            .appendChoose(context, choose -> choose
                                    .when(" age > 10", "AND age = ?", context.get("age"))
                                    .when("username!=null", "AND username = ?", context.get("username"))
                                    .otherwise("email IS NULL")
                            );
                })
                .append("AND email in")
                .appendForeach("emails", context, "email", ",", "(", ")", foreachBuilder -> foreachBuilder
                        .body("#{email}")
                )
                .append("ORDER BY id ASC");
        String sql = builder.getSql();
        System.out.println(sql);
        System.out.println(Arrays.asList(builder.getParameters()));
        System.out.println("-------------------------------------------------------------------");
        builder = SqlBuilder.newInstance()
                .append("UPDATE users ")
                .appendSet(set -> set
                        .appendIf("username != null", context, "username = ?,", context.get("username"))
                        .appendIf("age == 30", context, "age = ?,", context.get("age"))
                )
                .append("WHERE id = ?", 10);

        System.out.println(sql);
        System.out.println(Arrays.asList(builder.getParameters()));
    }
}
