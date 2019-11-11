package com.nju.aop.utils.excel;

import java.lang.annotation.*;

/**
 * @author yinywf
 * Created on 2019-11-07
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelCell {
    String value();
    Class<?> Type() default String.class;

}