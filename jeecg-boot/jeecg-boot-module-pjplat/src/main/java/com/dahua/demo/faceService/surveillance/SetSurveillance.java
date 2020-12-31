package com.dahua.demo.faceService.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 布控设置接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中repositoryId，channelCode，threshold等参数的值
 */


public class SetSurveillance extends BaseUserInfo {

    public static final String ACTION = "/faceService/surveillance";

    private static String set(String ip, int port, String token) throws Exception{
        /**
         * repositoryId : 类型string 。人像库 id，必填。
         * channelCode : 类型string 。Saas通道id，必填。
         * threshold : 类型double 。布控阈值，取值范围为[0-1],必填。
         * level : 类型int 。布控等级。选填。
         */
        String content = "{\n" +
                "\"repositoryId\" : \"899405827\",\n" +
                "\"channelCode\" : \"174847716B1AGT09OLO67L0\",\n" +
                "\"threshold\" : 0.8,\n" +
                "\"level\" : 1\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = set(ip, Integer.valueOf(port), token);
    }
}
