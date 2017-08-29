package com.elastic.stack.demo.elkDemo.annotation;

import java.lang.annotation.*;

/**
 * Created by liyang on 2017/8/24.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface IndexAnno {
}
