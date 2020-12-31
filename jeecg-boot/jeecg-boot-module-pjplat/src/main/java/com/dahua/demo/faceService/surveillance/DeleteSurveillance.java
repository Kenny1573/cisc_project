package com.dahua.demo.faceService.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 撤销布控接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为repositoryId和content中channelCode参数的值
 */
public class DeleteSurveillance extends BaseUserInfo {
    //repositoryId : 类型string。人像库 id，必填。
    public static String  repositoryId="1";

    public static final String ACTION = "/faceService/surveillance/"+repositoryId;

    private static String delete(String ip, int port, String token) throws Exception{
        //channelCode : 类型string 。Saas通道id，必填。
        String content = "?channelCode=123";
        String response=HttpTestUtils.httpRequest(HttpEnum.DELETE,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = delete(ip, Integer.valueOf(port), token);
    }
}
