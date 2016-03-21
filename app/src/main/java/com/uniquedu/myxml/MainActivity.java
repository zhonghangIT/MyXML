package com.uniquedu.myxml;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;

import com.uniquedu.myxml.bean.Student;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    /**这个是DOM解析的按钮*/
    private Button mButtomDOM;
    /**这是一个SAX解析的按钮*/
    private Button mButtomSAX;
    private Button mButtonPULL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtomDOM = (Button) findViewById(R.id.button_dom);
        mButtomDOM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseXmlDOM();
            }
        });
        mButtomSAX = (Button) findViewById(R.id.button_sax);
        mButtomSAX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseXmlSAX();
            }
        });
        mButtonPULL= (Button) findViewById(R.id.button_pull);
        mButtonPULL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseXmlPULL();
            }
        });
    }

    private void parseXmlPULL() {
        try {
        //创建解析器工厂
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            //创建解析器
            XmlPullParser parser=factory.newPullParser();
            //开始解析
            AssetManager manager = getAssets();
            parser.setInput(manager.open("clazz.xml"),"utf-8");
            int eventType=parser.getEventType();
            List<Student> clazz=new ArrayList<Student>();
            Student student=null;
            String tagName=null;
            while(eventType!=XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        System.out.println("文档开始");
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        System.out.println("文档结束");
                        break;
                    case XmlPullParser.START_TAG:
                        System.out.println("标签开始"+parser.getName());
                        if(parser.getName().equals("student")){
                             student=new Student();
                            String num=parser.getAttributeValue(null,"num");
                            String major=parser.getAttributeValue(null,"major");
                            student.setNum(num);
                            student.setMajor(major);
                        }
                        tagName=parser.getName();
                        break;
                    case XmlPullParser.END_TAG:
                        System.out.println("标签结束"+parser.getName());
                        if(parser.getName().equals("student")){
                            clazz.add(student);
                        }
                        tagName=null;
                        break;
                    case XmlPullParser.TEXT:
                        System.out.println("文档内容"+parser.getText());
                        String value=parser.getText();
                        if("name".equals(tagName)){
                            student.setName(value);
                        }else if("age".equals(tagName)){
                            student.setAge(value);
                        }else if("sex".equals(tagName)){
                            student.setSex(value);
                        }
                        break;
                    default:
                        break;
                }
                eventType=parser.next();//处理下一个事件
            }

            for (Student item : clazz) {
                System.out.println("得到学生的信息 姓名" + item.getName() + "学号:" + item.getNum() + " 专业:" + item.getMajor());
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXmlSAX() {
        //使用SAX的方式解析xml文件，SAX的是一种事件的处理方式。
        //1创建解析器工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2创建xml的解析器
        try {
            SAXParser parser = factory.newSAXParser();
            //3开始解析,接收两个参数，第一个参数是指的要解析的xml文件的流，第二个参数是解析的事件处理
            AssetManager manager = getAssets();
            MySAXHandler saxHandler = new MySAXHandler();
            parser.parse(manager.open("clazz.xml"), saxHandler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXmlDOM() {
        //使用DOM的方式解析xml文件，解析本地文件
        //得到xml文件的流
        AssetManager manager = getAssets();
        try {
            InputStream in = manager.open("clazz.xml");
            //开始进行DOM解析，DOM解析是将xml文件完整的读取到内存当中，以一种倒挂的树Document的形式，解析这个倒挂的树
            //开始将xml文件读取到内存中
            //1 得到文档构建器的工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //2 得到文档的构建器
            DocumentBuilder builder = factory.newDocumentBuilder();
            //3 得到xml的文档,将流in解析到内存中以倒挂树形式存在。
            Document document = builder.parse(in);
            //得到所有的节点student的对象集合
            NodeList list = document.getElementsByTagName("student");
            ArrayList<Student> clazz = new ArrayList<Student>();
            for (int i = 0; i < list.getLength(); i++) {
                Student person = new Student();
                Node student = list.item(i);//得到其中一条student节点
                NamedNodeMap map = student.getAttributes();//得到student节点的attrs major="cumputer" num="201323453453"
                Node num = map.getNamedItem("num");//得到该学生的学号
                Node major = map.getNamedItem("major");//得到该学生的专业
                person.setNum(num.getNodeValue());
                person.setMajor(major.getNodeValue());
                System.out.println("该学生的学号" + num.getNodeValue() + " 该学生的专业" + major.getNodeValue());
                NodeList msgs = student.getChildNodes();//student节点下的所有子节点
                for (int j = 0; j < msgs.getLength(); j++) {
                    Node item = msgs.item(j);//遍历student的子节点
                    if (item.getNodeType() == Node.ELEMENT_NODE) {//判断该节点的类型是不是一个元素，student的元素节点包含name sex和age
                        String nodeName = item.getNodeName();//得到该元素的名称，用于区分该元素的子节点的值的意义
                        String value = item.getFirstChild().getNodeValue();//得到该元素的第一个子节点的值
                        if (nodeName.equals("name")) {
                            System.out.println("该学生的名称" + value);
                            person.setName(value);
                        } else if (nodeName.equals("age")) {
                            System.out.println("该学生的年龄" + value);
                            person.setAge(value);
                        } else if (nodeName.equals("sex")) {
                            System.out.println("该学生的性别" + value);
                            person.setSex(value);
                        }
                    }
                }
                clazz.add(person);
            }
            for (Student student : clazz) {
                System.out.println("得到学生的信息 姓名" + student.getName() + "学号:" + student.getNum() + " 专业:" + student.getMajor());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
