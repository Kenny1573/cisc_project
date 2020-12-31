package com.dahua.demo.faceService.repository;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 人像库查询接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中start，limit，isSurvey，keyword等参数的值
 */
public class GetRepo extends BaseUserInfo {

    public static final String ACTION = "/faceService/repository";

    private static String get(String ip, int port, String token) throws Exception{
        /**
         * start:类型int 。从第几个结果开始返回，不填默认为1。选填。
         * version 1.20
         * limit:类型int 。返回至多多少结果，不填默认为512。选填。
         * keyword:类型string 。查询关键字，可模糊查询库名称或备注。选填。
         * type:类型int 。0-黑名单；1-白名单；2-静态库；3-红名单库（非system用户不能查询红名单）；不填默认查询所有
         * 类型。选填。
         * isSurvey:类型int 。是否布控。0-全部，1-未布控，2-已布控。默认为0。选填。
         */
        String content = "?start=1" +
                        "&limit=100" +
                        "&isSurvey=0" +
                        "&keyword=a" +
                        "&type=0";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = get(ip, Integer.valueOf(port), token);
    }
}

