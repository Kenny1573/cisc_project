package com.dahua.demo.faceService.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 布控查询接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中repositoryId，channelCode，start，limit等参数的值
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class QuerySurveillance extends BaseUserInfo {
    public static final String ACTION = "/faceService/surveillance";

    private static String query(String ip, int port, String token) throws Exception{
        /**
         * repositoryId : 类型string。人像库 id，选填。
         * channelCode : 类型string 。Saas通道id，选填。
         * start : 类型int 。从第几个结果开始返回，最小值为1，不填默认为1。选填。
         * limit : 类型int 。返回至多多少结果，最大值为1024，不填默认为512。选填。
         */
        String content = "?repositoryId=1" +
                        "&channelCode=1" +
                        "&start=1" +
                        "&limit=100";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = query(ip, Integer.valueOf(port), token);
    }
}
