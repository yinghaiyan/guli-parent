package com.atguigu.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;

import java.util.List;

public class testvod {
    public static void main(String[] args) throws ClientException {
//        上传视频方法
//        String accessKeyId = "LTAI5tB1namrXEdbh4gy9myp";
//        String accessKeySecret = "UL7s7m1pvwCBiPVplBsBUIXyM4SSJv";
//
//        String title = "6 - What If I Want to Move Faster - upload by sdk";   //上传之后文件名称
//        String fileName = "/Users/angela/Desktop/谷粒学院/项目资料/1-阿里云上传测试视频/6 - What If I Want to Move Faster.mp4";  //本地文件路径和名称
//        //上传视频的方法
//        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
//        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
//        request.setPartSize(2 * 1024 * 1024L);
//        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
//        request.setTaskNum(1);
//
//        UploadVideoImpl uploader = new UploadVideoImpl();
//        UploadVideoResponse response = uploader.uploadVideo(request);
//
//        if (response.isSuccess()) {
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//        } else {
//            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
//            System.out.print("VideoId=" + response.getVideoId() + "\n");
//            System.out.print("ErrorCode=" + response.getCode() + "\n");
//            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
//        }

            getPlayAuth();
    }

    public static void getPlayAuth() throws ClientException {
        //        根据视屏的ID获取视屏播放凭证
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tB1namrXEdbh4gy9myp", "UL7s7m1pvwCBiPVplBsBUIXyM4SSJv");
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        request.setVideoId("79177c508de5457bbc84032ddf772358");
        response = client.getAcsResponse(request);
        System.out.println("playauth: "+response.getPlayAuth());
    }
    public static void getPlayUrl()throws ClientException{
        //        根据视屏ID获取视屏播放地址
//        创建初始化对象
        DefaultAcsClient client = InitObject.initVodClient("LTAI5tB1namrXEdbh4gy9myp", "UL7s7m1pvwCBiPVplBsBUIXyM4SSJv");

        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response=new GetPlayInfoResponse();

        request.setVideoId("395164ce2aa64ab0830e8355dd88ecb9");
        response = client.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }
}

