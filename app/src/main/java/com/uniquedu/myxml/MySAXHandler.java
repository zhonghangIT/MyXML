package com.uniquedu.myxml;

import com.uniquedu.myxml.bean.Student;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongHang on 2016/3/18.
 */
public class MySAXHandler extends DefaultHandler {
    private List<Student> clazz;
    private Student student;

    public List<Student> getClazz() {
        return clazz;
    }

    private String tagName;
    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("文档开始");
        clazz = new ArrayList<>();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("文档结束");
        for (Student item:clazz){
            System.out.println("学生信息"+item.getName()+item.getMajor()+item.getNum()+item.getSex()+item.getAge());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        System.out.println("标签开始" + qName);
        if (qName.equals("student")) {
            student = new Student();
            String num = attributes.getValue("num");
            String major = attributes.getValue("major");
            student.setNum(num);
            student.setMajor(major);
        }
        tagName=qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        System.out.println("标签结束" + qName);
        if (qName.equals("student")) {
            clazz.add(student);
        }
        tagName=null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        String value=new String(ch,start,length);
        System.out.println(tagName+"中得到的值"+value);
        if("name".equals(tagName)){
            student.setName(value);
        }else if("age".equals(tagName)){
            student.setAge(value);
        }else if("sex".equals(tagName)){
            student.setSex(value);
        }
    }
}
