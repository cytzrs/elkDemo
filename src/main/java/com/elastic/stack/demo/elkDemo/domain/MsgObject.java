package com.elastic.stack.demo.elkDemo.domain;

import java.io.Serializable;

/**
 * Created by liyang on 2017/8/22.
 */
public class MsgObject implements Serializable {

    private String name;
    private boolean sex;
    private int age;

    public MsgObject(String name, boolean sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
