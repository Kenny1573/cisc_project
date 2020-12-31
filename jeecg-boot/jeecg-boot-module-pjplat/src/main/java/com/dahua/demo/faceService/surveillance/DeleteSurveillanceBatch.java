package com.dahua.demo.faceService.surveillance;

import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 布控批量删除接口
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方content中repositoryId，channelCode参数的内容
 */
public class DeleteSurveillanceBatch extends BaseUserInfo {

    public static final String ACTION = "/faceService/surveillance/async";

    private static String delete(String ip, int port, String token) throws Exception{
        /**
         * repositoryId : 类型string。人像库 id。
         * channelCode : 类型string。Saas通道id。
         * 只传repositoryId时删一个库的布控，只传channelCode时删除一个通道的布控，repositoryId和channelCode都传时删除一条布控。
         */
        String content = "?repositoryId=899405827" +
                         "&channelCode=174847716B1AGT09OLO67L0";
        String response=HttpTestUtils.httpRequest(HttpEnum.DELETE,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = delete(ip, Integer.valueOf(port), token);
    }
}
