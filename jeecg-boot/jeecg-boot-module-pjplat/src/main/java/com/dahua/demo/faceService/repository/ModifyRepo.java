package com.dahua.demo.faceService.repository;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 人像库修改接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为repositoryId和content中name，memo，rank等参数的值
 */
public class ModifyRepo extends BaseUserInfo {
    //repositoryId:类型string 。库id。必填。
    public static String repositoryId = "906754048";

    public static final String ACTION = "/faceService/repository/"+repositoryId;

    private static String modify(String ip, int port, String token) throws Exception{
        /**
         * name: 类型string 。库名称，最长50位。必填。
         * memo: 类型string 。备注。最长128位。选填。
         * rank: 类似int 。库等级。1-A，2-B，3-C，4-D。选填。
         */
        String content = "{\n" +
                "\"name\" : \"test\",\n" +
                "\"memo\" : \"test\",\n" +
                "\"rank\" : 1\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = modify(ip, Integer.valueOf(port), token);
    }
}

