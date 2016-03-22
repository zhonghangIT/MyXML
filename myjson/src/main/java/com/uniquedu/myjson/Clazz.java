package com.uniquedu.myjson;

import java.util.List;

/**
 * Created by ZhongHang on 2016/3/23.
 */
public class Clazz {
    private String clazzName;
    private String clazzNum;
    private Teacher teacher;

    public Clazz() {
    }

    public Clazz(String clazzName, String clazzNum, Teacher teacher, List<Student> students) {
        this.clazzName = clazzName;
        this.clazzNum = clazzNum;
        this.teacher = teacher;
        this.students = students;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getClazzNum() {
        return clazzNum;
    }

    public void setClazzNum(String clazzNum) {
        this.clazzNum = clazzNum;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    private List<Student> students;
}
