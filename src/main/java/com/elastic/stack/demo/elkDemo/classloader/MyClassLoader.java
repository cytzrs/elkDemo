package com.elastic.stack.demo.elkDemo.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by liyang on 2017/8/24.
 */
public class MyClassLoader extends ClassLoader {

    //类加载器的名称
    private String name;
    //类存放的路径
    private String classpath = "C:\\Users\\liyang\\Desktop\\elk\\elkDemo\\src\\main\\java\\com\\elastic\\stack\\demo\\elkDemo\\classloader";


    MyClassLoader(String name) {
        this.name = name;
    }

    MyClassLoader(ClassLoader parent, String name) {
        super(parent);
        this.name = name;
    }
    /**
     * 重写findClass方法
     */
    @Override
    public Class<?> findClass(String name) {
        byte[] data = loadClassData(name);
        System.out.println("----------==================++++++++++++++++----------------");
        return this.defineClass(name, data, 0, data.length);
    }

    public byte[] loadClassData(String name) {
        try {
            name = name.replace(".", "//");
            FileInputStream is = new FileInputStream(new File(classpath + name + ".class"));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b = 0;
            while ((b = is.read()) != -1) {
                baos.write(b);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) throws Exception{
        MyClassLoader my = new MyClassLoader("myLoader");
        Class<?> loadClass = my.loadClass("com.elastic.stack.demo.elkDemo.classloader.Foo");
        Foo cast = (Foo)loadClass.newInstance();
        cast.sayHi();

    }
}
