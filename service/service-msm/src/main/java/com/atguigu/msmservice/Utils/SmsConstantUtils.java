package com.atguigu.msmservice.Utils;

import lombok.Data;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SmsConstantUtils implements InitializingBean {

    /**读取配置文件内容
     *
     */
    @Value("${tencentcloud.sms.secretId}")
     private String secretId;
    @Value("${tencentcloud.sms.secretKey}")
    private String secretKey;
 
    @Value("${tencentcloud.sms.sdkAppId}")
    private String sdkAppId;
 
    @Value("${tencentcloud.sms.signName}")
    private String signName;
 
    @Value("${tencentcloud.sms.templateId}")
    private String templateId;
 
    //定义公开静态常量
    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String SDKAPP_ID;
    public static String SIGN_NAME;
    public static String TEMPLATED_ID;
 
    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID=secretId;
        SECRET_KEY=secretKey;
        SDKAPP_ID=sdkAppId;
        SIGN_NAME=signName;
        TEMPLATED_ID=templateId;
    }
}

