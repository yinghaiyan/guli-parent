package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestExcel {
    public static void main(String[] args) {
//        实现Excel写的操作
//        1设置写入文件夹地址和Excel文件i名称

//        String filename="/Users/angela/Desktop/谷粒学院/工作簿1.xlsx";
//
//        EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

//        实现excel读操作
        String filename="/Users/angela/Desktop/谷粒学院/工作簿1.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();


    }

    private static List<DemoData> getData(){
        ArrayList<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("lucy"+i);
            list.add(data);
        }
        return list;
    }
}
