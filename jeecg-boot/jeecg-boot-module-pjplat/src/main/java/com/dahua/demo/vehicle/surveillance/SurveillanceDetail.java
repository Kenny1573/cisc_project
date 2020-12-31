package com.dahua.demo.vehicle.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 布控详情接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为recordId的值
 */
public class SurveillanceDetail extends BaseUserInfo {
    //recordId : 类型string ，必填。布控记录号。
    public static String  recordId="91e853bd1b5d44c4a240dc682583fc5c";

    public static final String ACTION = "/vehicleService/rest/surveillance/"+recordId;

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
