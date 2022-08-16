package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;

import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.service_base.exceptionhandler.GuliException;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author angela
 */
@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin

public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;


    /**    获取扫描人信息，添加数据
     *
     * @return
     */
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //        使用httpClient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
//           从accessTokenInfo字符串获取出来两个值，access-token和openID
//            转换成map集合
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token  = String.valueOf(mapAccessToken.get("access_token"));
            String openid= String.valueOf(mapAccessToken.get("openid"));

            UcenterMember member= memberService.getOpenIdMember(openid);
//           空表示表里面没有相同的微信数据
            if (member == null) {
                //            拿着access-token和openID再去请求微信提供固定的地址，获取到扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
//            发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
//           获取返回userInfo字符串扫描人信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname= String.valueOf(userInfoMap.get("nickname"));
                String headimgurl= String.valueOf(userInfoMap.get("headimgurl"));

                member=new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            throw new GuliException(20001,"登录失败");
        }
    }
    /**   生成微信二维码
     *
     * @param session
     * @return
     */
    @GetMapping("login")
    public String getWxCode(HttpSession session){
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
        "?appid=%s" +
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=snsapi_login" +
        "&state=%s" +
        "#wechat_redirect";
//        对redirect_url进行URLEncoder编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl=URLEncoder.encode(redirectUrl,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        设置%S的值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_REDIRECT_URL,
                "atguigu"
        );
        return "redirect:"+url;
    }
}
