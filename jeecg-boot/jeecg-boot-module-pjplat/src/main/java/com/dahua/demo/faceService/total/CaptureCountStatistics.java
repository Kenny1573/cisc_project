package com.dahua.demo.faceService.total;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 抓拍总数统计接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中startTime，endTime等参数的值
 */
public class CaptureCountStatistics extends BaseUserInfo {

    public static final String ACTION = "/faceService/statistics/capture";

    private static String count(String ip, int port, String token) throws Exception{
        /**
         * startTime:类型string 。 开始时间；选填。
         * endTime:类型string 。 结束时间；选填。
         */
        String content = "{\n" +
                "\"startTime\":\"2018-03-01\",\n" +
                "\"endTime\" : \"2018-03-01\"\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = count(ip, Integer.valueOf(port), token);
    }
}
