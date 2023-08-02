package io.github.donnie4w.jdao.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface DefName {
    String name() default "";
}
