package com.dahua.demo.vehicle.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 撤销布控接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为recordId和content中revokeReason参数的内容
 */
public class RevokeSurveillance extends BaseUserInfo {
    //recordId : 类型string ，必填。布控编号。
    public static String  recordId="5938eabafe2344e29cfb81d7af1c4b56";

    public static final String ACTION = "/vehicleService/rest/surveillance/revoke/"+recordId;

    private static String revoke(String ip, int port, String token) throws Exception{
        //revokeReason : 类型string 。撤控原因，最长不超过128字符，Web服务存，布控服务不存。
        String content = "{" +
                "\"revokeReason\" : \"撤控原因\"" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.PUT,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = revoke(ip, Integer.valueOf(port), token);
    }
}
