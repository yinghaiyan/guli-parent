package com.atguigu.demo;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class run {

    @Test
    public void run(){
        String path = "C:\\Users\\15231\\Desktop\\note.txt";
        File file = new File(path);
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                boolean isNum = s.matches("【0-9】+");
                if(!isNum){
                    result.append( System.lineSeparator() + s);
                }
//                result.append( System.lineSeparator() + s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(result.toString());
    }
}

