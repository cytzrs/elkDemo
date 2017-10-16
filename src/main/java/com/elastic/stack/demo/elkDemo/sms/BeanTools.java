package com.elastic.stack.demo.elkDemo.sms;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.BeanUtils.getPropertyDescriptor;
import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * Created by liyang on 2017/7/28.
 */
public class BeanTools {

    private final static Log logger = LogFactory.getLog(BeanTools.class);

/*    private Class<A> aClass;
    private Class<B> bClass;

    public BeanUtils(Class<A> aClass, Class<B> bClass) {
        this.aClass = aClass;
        this.bClass = bClass;
    }

    private void init() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.aClass = (Class) params[0];
        this.bClass = (Class) params[1];
    }

    public void beanCopier(A aBean, B bBean) {

    }

    public static void main(String[] args) {
        BeanUtils beanUtils = new BeanUtils(FundAccountBank.class, BankSignStatusIn.class);
        beanUtils.init();
    }*/

    private BeanTools(){}

    static class InnerClass {
        private final static BeanTools beanTools = new BeanTools();
    }

    public static BeanTools getInstance() {
        return InnerClass.beanTools;
    }

    //对springframe提供BeanUtils.copyProperties进行扩展，再拷贝字段是过滤掉值位null项
    public void copy(Object source, Object target) throws BeansException {
        BeanTools.getInstance().copyProperties(source, target, null, (String[]) null);
    }

    private static void copyProperties(Object source, Object target, Class<?> editable, String... ignoreProperties)
            throws BeansException {

        if(source == null || target == null) {
            logger.info("source和target不能为空，参数错误");
            return;
        }

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(source);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            if(value != null)
                                writeMethod.invoke(target, value);
                        }
                        catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }


}
