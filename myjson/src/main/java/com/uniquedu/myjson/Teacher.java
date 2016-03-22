package com.uniquedu.myjson;

/**
 * Created by ZhongHang on 2016/3/23.
 */
public class Teacher {
    private String name;
    private String major;
    private String position;

    public Teacher() {
    }

    @Override
    public String toString() {
        return name+major+position;
    }

    public Teacher(String name, String major, String position) {
        this.name = name;
        this.major = major;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
