package com.atguigu.educms.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educms.entity.CrmBanner;
import com.atguigu.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 后台管理接口
 * </p>
 *
 * @author testjava
 * @since 2022-06-23
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {

    @Autowired
    private CrmBannerService bannerService;

    /**   分页查询banner
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page,@PathVariable long limit){
        Page<CrmBanner> pageBanner = new Page<CrmBanner>(page, limit);
        bannerService.page(pageBanner,null);
        return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }

//    添加banner
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }

    @GetMapping("get/{id}")
    public R get(@PathVariable String id){
        CrmBanner banner=bannerService.getById(id);
        return R.ok();
    }


//    修改banner
    @PostMapping("update")
    public R updateById(@RequestBody CrmBanner banner){
        bannerService.updateById(banner);
        return R.ok();
    }

//    删除banner
    @DeleteMapping("remove/{id}")
    public R remove(@PathVariable String id){
        bannerService.removeById(id);
        return R.ok();
    }
}

