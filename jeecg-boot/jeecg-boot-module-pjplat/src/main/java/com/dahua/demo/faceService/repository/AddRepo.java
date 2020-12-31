package com.dahua.demo.faceService.repository;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 人像库新增接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中name，type，memo等参数的值
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class AddRepo extends BaseUserInfo {

    public static final String ACTION = "/faceService/repository";

    private static String add(String ip, int port, String token) throws Exception{
        /**
         * name: 类型string ，必填。库名称。
         * type: 类型int ，必填。库类型。0-黑名单，1-白名单，2-静态库
         * memo: 类型string ，选填。备注。
         * version 1.20
         * rank: 类似int ，选填。用户自定义库等级，定制功能，可以不填。1-A，2-B，3-C，4-D
         */
        String content = "{\n" +
                "\"name\" : \"test\",\n" +
                "\"type\" : 1,\n" +
                "\"memo\" : \"test\"\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = add(ip, Integer.valueOf(port), token);
    }
}
