package com.dahua.demo.faceService.total;


import com.dahua.demo.util.BaseUserInfo;
import com.dahua.demo.util.HttpEnum;
import com.dahua.demo.util.HttpTestUtils;

/**
 * 人像库总数统计
 * 注意：使用前请根据实际情况修改请求的参数，修改完后调用main方法即可
 * 需要修改的地方为content中types参数的值
 */
public class RepoTotal extends BaseUserInfo {

    public static final String ACTION = "/faceService/statistics/repository";

    private static String total(String ip, int port, String token) throws Exception{
        /**
         *   目前接口只支持统计全部,types为空。
         *   types:类型array[int] 。必填。库类型,0-黑名单；1-白名单；2-静态库；为空时查询所有。
         */

        String content = "{\n" +
                "\"types\" :[]\n" +
                "}";
        String response=HttpTestUtils.httpRequest(HttpEnum.POST,ip,port,ACTION,token,content);
        System.out.println(response);
        return response;
    }

    public static void main(String[] args) throws Exception {
        String rsp = total(ip, Integer.valueOf(port), token);
    }
}
