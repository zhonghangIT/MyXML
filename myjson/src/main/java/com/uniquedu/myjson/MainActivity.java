package com.uniquedu.myjson;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button mButtonParseOriginal;
    private static final String TAG = "json解析";
    private Button mButtonToJsonOriginal;
    private Button mButtonParseGson;
    private Button mButtonToJsonGson;
    private Button mButtonParseArrayGson;
    private Button mButtonParseFast;
    private Button mButtonToJsonFast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButtonParseOriginal = (Button) findViewById(R.id.button_parsejson_android);
        mButtonParseOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clazz clazz = parseJSONOriginal();
                Log.d(TAG, clazz.getClazzName() + clazz.getClazzNum() + clazz.getTeacher().getName());
            }
        });
        mButtonToJsonOriginal = (Button) findViewById(R.id.button_toJson_andorid);
        mButtonToJsonOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJsonOriginal();
            }
        });
        mButtonParseGson = (Button) findViewById(R.id.button_parsejosn_gson);
        mButtonParseGson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJsonGson();
            }
        });
        mButtonParseArrayGson = (Button) findViewById(R.id.button_parsejosnarray_gson);
        mButtonParseArrayGson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseArrayGson();
            }
        });
        mButtonToJsonGson = (Button) findViewById(R.id.button_tojson_gson);
        mButtonToJsonGson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJsonUseGson();
            }
        });
        mButtonParseFast= (Button) findViewById(R.id.button_parsejson_fast);
        mButtonParseFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseJsonFast();
            }
        });
        mButtonToJsonFast= (Button) findViewById(R.id.button_tojson_fast);
        mButtonToJsonFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createJsonUseFast();
            }
        });
    }

    /**
     * 使用fastjson的方式生成json数据
     */
    private void createJsonUseFast() {
        Clazz clazz=createClazz();
        String json= JSON.toJSONString(clazz);
        Log.d(TAG,json);
    }

    /**
     * 使用fastjson解析json数据
     */
    private void parseJsonFast() {
        String content=readFromAssets("json");
        //使用fastjson进行解析,注意的地方，Java Bean中必须存在无参构造器
        Clazz clazz= JSON.parseObject(content,Clazz.class);
        Log.d(TAG, clazz.getClazzName());
        Log.d(TAG, clazz.getClazzNum());
        Log.d(TAG, clazz.getTeacher().toString());
        List<Student> students = clazz.getStudents();
        for (int i = 0; i < students.size(); i++) {
            Log.d(TAG, students.get(i).toString());
        }
    }

    /**
     * 使用GSON的方式生成json
     */
    private void createJsonUseGson() {
        //使用GSON的方式生成一个json数据
        Clazz clazz = createClazz();
        Gson gson = new Gson();
        String json = gson.toJson(clazz);
        Log.d(TAG, "使用GSON生成的数据" + json);
    }

    /**
     * 使用GSON解析json的集合类型
     */
    private void parseArrayGson() {
        String content = readFromAssets("jsonarray");
        //使用GSON解析JSONArray
        Gson gson = new Gson();
        Type type = new TypeToken<List<Student>>() {
        }.getType();
        List<Student> students = gson.fromJson(content, type);
        for (int i = 0; i < students.size(); i++) {
            Log.d(TAG, students.get(i).toString());
        }
    }

    /**
     * 使用Gson的方式解析JSON数据
     */
    private void parseJsonGson() {
        //使用GSON的方式解析json数据
        String content = readFromAssets("json");
        //需要注意的地方，必须有和json数据对应的Java Bean
        Gson gson = new Gson();
        Clazz clazz = gson.fromJson(content, Clazz.class);//此时解析已经完成
        Log.d(TAG, clazz.getClazzName());
        Log.d(TAG, clazz.getClazzNum());
        Log.d(TAG, clazz.getTeacher().toString());
        List<Student> students = clazz.getStudents();
        for (int i = 0; i < students.size(); i++) {
            Log.d(TAG, students.get(i).toString());
        }
    }

    /**
     * 原生的方式生成json数据
     */
    private void createJsonOriginal() {
        //原生的方式生成json

        Clazz clazz = createClazz();
        Teacher teacher = clazz.getTeacher();
        List<Student> students = clazz.getStudents();
        JSONObject obj = new JSONObject();//生成最外层的JSONObject
        try {
            obj.put("clazzName", clazz.getClazzName());//添加属性
            obj.put("clazzNum", clazz.getClazzNum());//添加属性
            JSONObject teacherObj = new JSONObject();//创建Teacher的JSONObject
            teacherObj.put("name", teacher.getName());
            teacherObj.put("major", teacher.getMajor());
            teacherObj.put("position", teacher.getPosition());
            obj.put("teacher", teacherObj);//将teacherObj添加到obj
            JSONArray studentArray = new JSONArray();
            for (int i = 0; i < students.size(); i++) {
                JSONObject student = new JSONObject();
                student.put("studentName", students.get(i).getName());
                student.put("studentAge", students.get(i).getAge());
                student.put("studentSex", students.get(i).getSex());
                studentArray.put(student);
            }
            obj.put("students", studentArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, obj.toString());
    }

    /**
     * 原生解析方式
     */
    private Clazz parseJSONOriginal() {
        //原生的解析方式
        //将assets中的数据读取出来
        String content = readFromAssets("json");
        Clazz clazzObj = new Clazz();
        try {
            JSONObject clazz = new JSONObject(content);
            clazzObj.setClazzName(clazz.getString("clazzName"));
            clazzObj.setClazzNum(clazz.getString("clazzNum"));
            Log.d(TAG, "班级名称：" + clazz.getString("clazzName"));
            Log.d(TAG, "班级编号：" + clazz.getString("clazzNum"));
            Teacher teacherObj = new Teacher();
            JSONObject teacher = clazz.getJSONObject("teacher");
            teacherObj.setName(teacher.getString("name"));
            teacherObj.setMajor(teacher.getString("major"));
            teacherObj.setPosition(teacher.getString("position"));
            clazzObj.setTeacher(teacherObj);
            Log.d(TAG, "老师的名字：" + teacher.getString("name"));
            Log.d(TAG, "老师的专业：" + teacher.getString("major"));
            Log.d(TAG, "老师的职位：" + teacher.getString("position"));
            List<Student> studentsArray = new ArrayList<>();
            JSONArray students = clazz.getJSONArray("students");
            for (int i = 0; i < students.length(); i++) {
                Student studentObj = new Student();
                JSONObject student = students.getJSONObject(i);
                studentObj.setName(student.getString("name"));
                studentObj.setAge(student.getString("age"));
                studentObj.setSex(student.getString("sex"));
                Log.d(TAG, "学生名字：" + student.getString("name"));
                Log.d(TAG, "学生年龄：" + student.getString("age"));
                Log.d(TAG, "学生性别：" + student.getString("sex"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return clazzObj;
    }

    /**
     * @param name 读取的assets文件下的文件名称
     * @return 将assets中的json数据读取成字符串
     */
    private String readFromAssets(String name) {
        AssetManager manager = getAssets();
        String content = "";
        try {
            InputStream in = manager.open(name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                content += line;
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private Clazz createClazz() {
        Teacher teacher = new Teacher("王老师", "计算机", "班主任");
        List<Student> students = new ArrayList<Student>();
        students.add(new Student("张三", "28", "男"));
        students.add(new Student("李四", "23", "女"));
        students.add(new Student("王五", "25", "男"));
        Clazz clazz = new Clazz("一年级二班", "20160711", teacher, students);
        return clazz;
    }
}
