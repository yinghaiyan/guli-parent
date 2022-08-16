package com.atguigu.eduservice.controller;



import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.atguigu.commonutils.R;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-05-26
 */

@Api(description = "讲师管理")
@RestController
@RequestMapping("/eduservice/edu-teacher")
@CrossOrigin
public class EduTeacherController {

    @Autowired
    private EduTeacherService eduTeacherService;
//    查询讲师表所有数据
//    rest风格
    @ApiOperation(value = "讲师列表")
    @GetMapping("/findAll")
    public R findAllTeacher(){
        List<EduTeacher> list = eduTeacherService.list(null);
        return R.ok().data("items", list);
    }
//    逻辑删除讲师方法

    @ApiOperation("讲师删除")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name ="id",value = "讲师ID" ,required = true) @PathVariable String id){
        boolean byId = eduTeacherService.removeById(id);
        if(byId) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping("{page}/{limit}")
    public R pageList(
        @ApiParam(name = "page", value = "当前页码", required = true)
        @PathVariable Long page,
        @ApiParam(name = "limit", value = "每页记录数", required = true)
        @PathVariable Long limit){
        Page<EduTeacher> eduTeacherPage = new Page<>(page, limit);
        eduTeacherService.page(eduTeacherPage,null);
        List<EduTeacher> records = eduTeacherPage.getRecords();
        long total = eduTeacherPage.getTotal();
        return  R.ok().data("total", total).data("rows", records);
    }
//    条件查询带分页方法
    @ApiOperation(value = "分页讲师列表")
    @PostMapping("pageTeacherCondition/{page}/{limit}")
    public R pageTeacherCondition(@PathVariable Long page, @PathVariable Long limit, @RequestBody(required = false) TeacherQuery teacherQuery){
        Page<EduTeacher> eduTeacherPage = new Page<>(page,limit);
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
//        构建条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        if (!ObjectUtils.isEmpty(name)){
            wrapper.like("name",name);
        }
        if (!ObjectUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if (!ObjectUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if (!ObjectUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }
        //排序
        wrapper.orderByDesc("gmt_create");
        eduTeacherService.page(eduTeacherPage,wrapper);
        List<EduTeacher> records = eduTeacherPage.getRecords();
        long total = eduTeacherPage.getTotal();
        return  R.ok().data("total", total).data("rows", records);
    }


    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = eduTeacherService.save(eduTeacher);
        if (save){
            return R.ok();
        }else {
            return R.ok();
        }
    }
/**
 *  根据讲师ID进行查询
  */

    @GetMapping("eduTeacher/{id}")
    public R  getTeacher(@PathVariable String id){
        EduTeacher eduTeacher = eduTeacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    /**    讲师修改功能
     *
     * @param eduTeacher
     * @return
     */
    @ApiOperation(value = "根据ID修改讲师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        boolean updateById = eduTeacherService.updateById(eduTeacher);
        if (updateById){
            return R.ok();
        }else {
            return R.ok();
        }
    }

}

