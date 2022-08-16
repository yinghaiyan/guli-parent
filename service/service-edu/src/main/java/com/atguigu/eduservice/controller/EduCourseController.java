package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-06-06
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

    @Autowired
    private EduCourseService courseService;

    @GetMapping
    public R getCourseList(){
        List<EduCourse> list = courseService.list(null);
        return R.ok().data("list",list);
    }
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){

        String id =courseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);
    }

    /**    根据课程id查询课程基本信息
     *
     * @param courseId
     * @return
     */
    @GetMapping("getCourseInfo/{courseId}")
    public R getCourseInfo(@PathVariable String courseId){
       CourseInfoVo courseInfoVo= courseService.getCourseInfo(courseId);
       return R.ok().data("courseInfoVo",courseInfoVo);
    }

    /**    修改课程信息
     *
     * @param courseInfoVo
     * @return
     */
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    /**    根据课程ID查询课程确认信息
     *
     * @param id
     * @return
     */
    @GetMapping("getPublishCourseInfo/{id}")
    public R getPublishCourseInfo(@PathVariable String id){
         CoursePublishVo coursePublishVo =courseService.publishCourseInfo(id);
        return R.ok().data("publishCourse",coursePublishVo);
    }

    /**    课程最终发布，修改课程状态
     *
     * @param id
     * @return
     */
    @PostMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    /**    删除课程
     *
     * @param courseId
     * @return
     */
    @DeleteMapping("{courseId}")
    public R deteleCourse(@PathVariable String courseId){
        courseService.removeCourse(courseId);
        return R.ok();
    }

}

