package com.elastic.stack.demo.elkDemo.sms;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * bean map相互转换工具类
 * Created by liyang on 2017/7/31.
 */
public class BeanUtils {

    private final static BeanMapUtil beanMapUtil = BeanMapUtil.getInstance();

    private final static BeanTools beanTools = BeanTools.getInstance();

    /*public BeanUtils() {
        this(BeanMapUtil.getInstance(), BeanTools.getInstance());
    }

    private BeanUtils(BeanMapUtil beanMapUtil, BeanTools beanTools) {
        this.beanMapUtil = beanMapUtil;
        this.beanTools = beanTools;
    }*/

    /**
     * 将Bean转换为map，只注入非空字段
     * @param bean
     * @return map
     * */
    public static Map<String, String> bean2Map(Object bean) {
        Map<String, String> result = new HashMap<>();
        try {
            result = beanMapUtil.describePlus(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将Bean转换为map，只注入非空字段,返回的map中key位下划线命名法
     * @param bean
     * @return map
     * */
    public static Map<String, String> bean2Map2UnderLine(Object bean) {
        Map<String, String> result = new HashMap<>();
        try {
            result = beanMapUtil.describePlus2UnderLine(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将map转换为bean
     * @param bean
     * @param properties
     * */
    public static void map2Bean(Object bean, Map<String, ? extends Object> properties) {
        try {
            beanMapUtil.populatePlus(bean, properties);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将map转换为bean,将map的key由下划线命名法转成匈牙利命名法，然后将值注入到bean对应的字段
     * @param bean
     * @param properties
     * */
    public static void map2Bean2Camel(Object bean, Map<String, ? extends Object> properties) {
        try {
            beanMapUtil.populatePlus2Camel(bean, properties);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将source的值复制到target,支付至source非空字段，target位两个bean的并集
     * @param source
     * @param target
     * */
    public static void copy(Object source, Object target) {
        beanTools.copy(source, target);
    }

}
