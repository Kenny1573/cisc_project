package com.dahua.demo.vehicle.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 审核布控接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为recordId和content中operation参数的内容
 */
public class AuditSurveillance extends BaseUserInfo {
    //recordId : 类型string ，必填。过车记录索引号。
    public static String recordId = "15deaa6cfd0540c7b17c9c2f1a039ed6";

    public static final String ACTION = "/vehicleService/rest/surveillance/audit/"+recordId;

    private static String audit(String ip, int port, String token) throws Exception{
        /**
         * operation : 类型string 。操作。
         * 布控操作描述
         * pass 通过（状态变为“布控中”）
         * notPass 不通过（状态变为“不通过”）
         */
        String content = "{" +
                "\"operation\":\"pass\"" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = audit(ip, Integer.valueOf(port), token);
    }
}
