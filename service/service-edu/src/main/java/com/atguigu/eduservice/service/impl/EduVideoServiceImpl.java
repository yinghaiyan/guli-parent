package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClident;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-06-06
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    @Autowired
    private VodClident vodClident;
    @Override
    public void removeVideoByCourseId(String courseId) {
//        1根据课程ID查询所有的视频ID
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("video_course_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(queryWrapper);
        List<String> collect = eduVideoList.stream()
                                            .map(EduVideo::getVideoSourceId)
                                            .filter(Objects::nonNull)
                                            .collect(Collectors.toList());
        //多个视频ID删除多个视屏
        vodClident.deleteBatch(collect);
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
