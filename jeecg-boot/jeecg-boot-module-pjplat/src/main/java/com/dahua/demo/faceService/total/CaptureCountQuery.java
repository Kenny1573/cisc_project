package com.dahua.demo.faceService.total;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 抓拍总数查询接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中condition中channelCodes，startCapTime，endCapTime等参数的值
 * 本demo的content中，只是给出了一个例子，可以根据参数选填的情况，自由组合content中参数内容
 */
public class CaptureCountQuery extends BaseUserInfo {

    public static final String ACTION = "/faceService/query/capture/count";

    private static String count(String ip, int port, String token) throws Exception{
        /**
         * condition:类型object 。查询条件。必填。
         * channelCodes:类型array[string] 。通道id列表。选填。
         * startCapTime:类型string 。抓拍开始时间。选填。
         * endCapTime:类型string 。抓拍结束时间。选填。
         */
        String content = "{\n" +
                "\"condition\":{\n" +
                "\"channelCodes\":[\"174847716B1AGEO716BT05S\"],\n" +
                "\"startCapTime\":\"20180808T073828Z\",\n" +
                "\"endCapTime\":\"20180810T073828Z\"\n" +
                "}\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = count(ip, Integer.valueOf(port), token);
    }
}
