package com.dahua.demo.faceService.capture;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 报警记录详情接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为alarmId的值
 */
public class AlarmDetail extends BaseUserInfo {
    //alarmId:类型string 。报警id。必填。
    public static String alarmId = "1011533800603275529";

    public static final String ACTION = "/faceService/query/alarm/"+alarmId;

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
