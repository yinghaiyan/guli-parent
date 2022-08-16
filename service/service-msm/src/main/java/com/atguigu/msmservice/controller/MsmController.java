package com.atguigu.msmservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.msmservice.Utils.RandomUtil;
import com.atguigu.msmservice.service.MsmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
public class MsmController {
    @Autowired
    private MsmService msmService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){

//        从redis获取验证码，如果获取直接返回
        String params = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(params)){
            return R.ok();
        }
/**    如果redis获取不到，
      生成随机值，传递腾讯云进行发送
 */
        params= RandomUtil.getFourBitRandom();
//        String[] parm={code,"5"};
        Boolean isSend=msmService.send(params,phone);
        if (isSend){
    /**      发送成功
            设置有效时间
     */
            redisTemplate.opsForValue().set(phone, params,5, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }

    }
}
