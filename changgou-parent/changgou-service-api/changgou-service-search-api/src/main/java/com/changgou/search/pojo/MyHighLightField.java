package com.changgou.search.pojo;

import java.lang.annotation.*;

/**
 * @author pengmin
 * @date 2020/11/16 21:37
 */
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyHighLightField {

}
