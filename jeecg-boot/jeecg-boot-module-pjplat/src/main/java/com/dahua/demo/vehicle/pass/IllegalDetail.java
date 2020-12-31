package com.dahua.demo.vehicle.pass;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 违章详情接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为recordId的值
 */
public class IllegalDetail extends BaseUserInfo {
    //recordId : 类型string ，必填。违章记录索引号。
    public static String recordId = "3701010200200000002602201809191900422262020001";

    public static final String ACTION = "/vehicleService/rest/illegal/query/"+recordId;

    private static String illegalDetail(String ip, int port, String token) throws Exception{
        String content = "";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = illegalDetail(ip, Integer.valueOf(port), token);
    }
}
