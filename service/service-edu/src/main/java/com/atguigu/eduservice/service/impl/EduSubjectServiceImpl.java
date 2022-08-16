package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-06
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void saveSubject(MultipartFile file,EduSubjectService eduSubjectService) {
       try {
           InputStream in = file.getInputStream();
           EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();
       } catch (IOException e) {
           e.printStackTrace();
       }

    }

    @Override
    public List<OneSubject> getAllOneTwoSubject() {
//        1.查询出所有一级分类
        QueryWrapper<EduSubject> wrapperOne=new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
//        2。查询出所有二级分类
        QueryWrapper<EduSubject> wrapperTwo=new QueryWrapper<>();
        wrapperOne.ne("parent_id","0");
        List<EduSubject> TwoSubjectList = baseMapper.selectList(wrapperTwo);
        
        List<OneSubject> finalSubjectList= new ArrayList<>();
//        3。封装一级分类
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject = oneSubjectList.get(i);
            OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject,oneSubject);
            finalSubjectList.add(oneSubject);
            List<TwoSubject> twoFinalSubjectList =new ArrayList<>();
            for (int m = 0; m <TwoSubjectList.size(); m++) {
                EduSubject tSubject = TwoSubjectList.get(m);
                if (tSubject.getParentId().equals(eduSubject.getId())){
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(tSubject,twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            oneSubject.setChildren(twoFinalSubjectList);
        }

//        4。封装二级分类
        return finalSubjectList;
    }


    public List<OneSubject> getAllSubjectByTree() {
        //查询所有科目数据
        List<EduSubject> eduSubjectList = baseMapper.selectList(null);
        //过滤出1级科目
        List<EduSubject> oneList = eduSubjectList.stream()
                .filter((x) -> "0".equals(x.getParentId())).collect(Collectors.toList());
        List<OneSubject> finalOneList = new ArrayList<>();
        for (EduSubject eduSubject : oneList) {
                OneSubject oneSubject = new OneSubject();
                //将eduSubject转化为oneSubject
                BeanUtils.copyProperties(eduSubject, oneSubject);
                //找出所有数据中父id等于当前oneSubject id的数据并转化为twoSubject数据
                List<TwoSubject> children = eduSubjectList.stream()
                        .filter(x -> x.getParentId().equals(eduSubject.getId()))
                        .map(temp -> {
                            TwoSubject twoSubject = new TwoSubject();
                             BeanUtils.copyProperties(temp, twoSubject);
                           return twoSubject;
                        }) .collect(Collectors.toList());
                //将twoSubject集合加入到oneSubject
                    oneSubject.setChildren(children);
                     finalOneList.add(oneSubject);
            }
            return finalOneList;
        }
}



