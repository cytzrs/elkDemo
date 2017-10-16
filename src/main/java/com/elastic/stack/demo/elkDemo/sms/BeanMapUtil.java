package com.elastic.stack.demo.elkDemo.sms;

import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.expression.Resolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by liyang on 2017/7/28.
 */
public class BeanMapUtil extends BeanUtilsBean {

    private final static Log logger = LogFactory.getLog(BeanMapUtil.class);

    private final static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);

    private final ConvertUtilsBean convertUtilsBean;

    private final PropertyUtilsBean propertyUtilsBean;

    private BeanMapUtil() {
        this.convertUtilsBean = new ConvertUtilsBean();
        this.propertyUtilsBean = new PropertyUtilsBean();
    }

    static class InnerClass {
        private static final BeanMapUtil instance = new BeanMapUtil();
    }

    public static BeanMapUtil getInstance() {
        return InnerClass.instance;
    }

    public PropertyUtilsBean getPropertyUtils() {
        return propertyUtilsBean;
    }

    public static Map<String, String> describePlus(Object bean)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        return BeanMapUtil.getInstance()._describePlus(bean, false);
    }

    public static Map<String, String> describePlus2UnderLine(Object bean)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        return BeanMapUtil.getInstance()._describePlus(bean, true);
    }

    public Map<String, String> _describePlus(Object bean, boolean toUnderLine)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        if (bean == null) {
            //            return (Collections.EMPTY_MAP);
            return (new HashMap<String, String>());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Describing bean: " + bean.getClass().getName());
        }

        Map<String, String> description = new HashMap<String, String>();
        if (bean instanceof DynaBean) {
            DynaProperty[] descriptors =
                    ((DynaBean) bean).getDynaClass().getDynaProperties();
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if("class".equals(name)) {
                    continue;
                }
                String value = getProperty(bean, name);
                if(value == null) {
                    continue;
                }
                /*if(toUnderLine) {
                    name = NumFormatUtils.ConvertR2B(name);
                }*/
                description.put(name, value);
            }
        } else {
            PropertyDescriptor[] descriptors =
                    getPropertyUtils().getPropertyDescriptors(bean);
            Class<?> clazz = bean.getClass();
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if("class".equals(name)) {
                    continue;
                }
                if (getPropertyUtils().getReadMethod(descriptors[i]) != null) {
                    String value = getProperty(bean, name);
                    if(value == null) {
                        continue;
                    }
                    /*if(toUnderLine) {
                        name = NumFormatUtils.ConvertR2B(name);
                    }*/
                    description.put(name,value );
                }
            }
        }
        return (description);

    }

    public static void populatePlus(Object bean, Map<String, ? extends Object> properties)
            throws IllegalAccessException, InvocationTargetException {

        BeanMapUtil.getInstance()._populatePlus(bean, properties, false);
    }

    public static void populatePlus2Camel(Object bean, Map<String, ? extends Object> properties)
            throws IllegalAccessException, InvocationTargetException {

        BeanMapUtil.getInstance()._populatePlus(bean, properties, true);
    }

    public void _populatePlus(Object bean, Map<String, ? extends Object> properties, boolean toCamel)
            throws IllegalAccessException, InvocationTargetException {

        // Do nothing unless both arguments have been specified
        if ((bean == null) || (properties == null)) {
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("BeanUtils.populate(" + bean + ", " +
                    properties + ")");
        }

        // Loop through the property name/value pairs to be set
        for(Map.Entry<String, ? extends Object> entry : properties.entrySet()) {
            // Identify the property name and value(s) to be assigned
            String name = entry.getKey();
            /*if(toCamel) {
                name = NumFormatUtils.ConvertB2R(name);
            }*/
            Object value = entry.getValue();
            if (name == null || value == null) {
                continue;
            }

            // Perform the assignment for this property
            setPropertyPlus(bean, name, value);

        }

    }

    private static Class<?> dynaPropertyType(DynaProperty dynaProperty,
                                             Object value) {
        if (!dynaProperty.isMapped()) {
            return dynaProperty.getType();
        }
        return (value == null) ? String.class : value.getClass();
    }

    public void setPropertyPlus(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {

        // Trace logging (if enabled)
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder("  setProperty(");
            sb.append(bean);
            sb.append(", ");
            sb.append(name);
            sb.append(", ");
            if (value == null) {
                sb.append("<NULL>");
            } else if (value instanceof String) {
                sb.append((String) value);
            } else if (value instanceof String[]) {
                String[] values = (String[]) value;
                sb.append('[');
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        sb.append(',');
                    }
                    sb.append(values[i]);
                }
                sb.append(']');
            } else {
                sb.append(value.toString());
            }
            sb.append(')');
            logger.trace(sb.toString());
        }

        // Resolve any nested expression to get the actual target bean
        Object target = bean;
        Resolver resolver = getPropertyUtils().getResolver();
        while (resolver.hasNested(name)) {
            try {
                target = getPropertyUtils().getProperty(target, resolver.next(name));
                if (target == null) { // the value of a nested property is null
                    return;
                }
                name = resolver.remove(name);
            } catch (NoSuchMethodException e) {
                return; // Skip this property setter
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("    Target bean = " + target);
            logger.trace("    Target name = " + name);
        }

        // Declare local variables we will require
        String propName = resolver.getProperty(name); // Simple name of target property
        Class<?> type = null;                         // Java type of target property
        int index  = resolver.getIndex(name);         // Indexed subscript value (if any)
        String key = resolver.getKey(name);           // Mapped key value (if any)

        // Calculate the property type
        if (target instanceof DynaBean) {
            DynaClass dynaClass = ((DynaBean) target).getDynaClass();
            DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
            if (dynaProperty == null) {
                return; // Skip this property setter
            }
            type = dynaPropertyType(dynaProperty, value);
        } else if (target instanceof Map) {
            type = Object.class;
        } else if (target != null && target.getClass().isArray() && index >= 0) {
            type = Array.get(target, index).getClass();
        } else {
            PropertyDescriptor descriptor = null;
            try {
                descriptor =
                        getPropertyUtils().getPropertyDescriptor(target, name);
                if (descriptor == null) {
                    return; // Skip this property setter
                }
            } catch (NoSuchMethodException e) {
                return; // Skip this property setter
            }
            if (descriptor instanceof MappedPropertyDescriptor) {
                if (((MappedPropertyDescriptor) descriptor).getMappedWriteMethod() == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = ((MappedPropertyDescriptor) descriptor).
                        getMappedPropertyType();
            } else if (index >= 0 && descriptor instanceof IndexedPropertyDescriptor) {
                if (((IndexedPropertyDescriptor) descriptor).getIndexedWriteMethod() == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = ((IndexedPropertyDescriptor) descriptor).
                        getIndexedPropertyType();
            } else if (key != null) {
                if (descriptor.getReadMethod() == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = (value == null) ? Object.class : value.getClass();
            } else {
                if (descriptor.getWriteMethod() == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping read-only property");
                    }
                    return; // Read-only, skip this property setter
                }
                type = descriptor.getPropertyType();
            }
        }

        // Convert the specified value to the required type
        Object newValue = null;
        if (type.isArray() && (index < 0)) { // Scalar value into array
            if (value == null) {
                String[] values = new String[1];
                values[0] = null;
                newValue = getConvertUtils().convert(values, type);
            } else if (value instanceof String) {
                newValue = getConvertUtils().convert(value, type);
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert((String[]) value, type);
            } else {
                newValue = convert(value, type);
            }
        } else if (type.isArray()) {         // Indexed value into array
            if (value instanceof String || value == null) {
                newValue = getConvertUtils().convert((String) value,
                        type.getComponentType());
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert(((String[]) value)[0],
                        type.getComponentType());
            } else {
                newValue = convert(value, type.getComponentType());
            }
        } else {                             // Value into scalar
            if (value instanceof String) {
                try {
                    String dateStr = String.valueOf(value);
                    newValue = sdf.parse(dateStr);
                } catch (ParseException e) {
                    logger.info("非日期类型字符串");
                    newValue = getConvertUtils().convert((String) value, type);
                }

                logger.info(newValue);
            } else if (value instanceof String[]) {
                newValue = getConvertUtils().convert(((String[]) value)[0],
                        type);
            } else {
                newValue = convert(value, type);
            }
        }

        // Invoke the setter method
        try {
            getPropertyUtils().setProperty(target, name, newValue);
        } catch (NoSuchMethodException e) {
            throw new InvocationTargetException
                    (e, "Cannot set " + propName);
        }

    }

}
