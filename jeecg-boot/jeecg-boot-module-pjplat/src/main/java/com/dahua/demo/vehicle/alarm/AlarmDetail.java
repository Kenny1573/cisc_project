package com.dahua.demo.vehicle.alarm;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 报警详情接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为 recordId
 */
public class AlarmDetail extends BaseUserInfo {
    //recordId : 类型string ，必填。报警记录唯一ID。
    public static String recordId = "62b384ff84104e2e8654bfe344fa6efc";

    public static final String ACTION = "/vehicleService/rest/alarm/"+recordId;

    private static String detail(String ip, int port, String token) throws Exception{
        String content = "";
        String response=HttpTestUtils.httpRequest(HttpEnum.GET,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = detail(ip, Integer.valueOf(port), token);
    }
}
