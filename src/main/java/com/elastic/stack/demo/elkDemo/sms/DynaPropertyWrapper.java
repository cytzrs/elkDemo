package com.elastic.stack.demo.elkDemo.sms;

import org.apache.commons.beanutils.DynaProperty;

/**
 * Created by liyang on 2017/10/12.
 */
public class DynaPropertyWrapper extends DynaProperty {

    public DynaPropertyWrapper(String name) {
        super(name);
    }

    public DynaPropertyWrapper(String name, Class<?> type) {
        super(name, type);
    }

    public DynaPropertyWrapper(String name, Class<?> type, Class<?> contentType) {
        super(name, type, contentType);
    }

}
